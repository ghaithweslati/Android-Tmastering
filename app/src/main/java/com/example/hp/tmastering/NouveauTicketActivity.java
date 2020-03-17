package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.client;
import com.example.hp.tmastering.beans.projet;
import com.example.hp.tmastering.beans.ticket;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NouveauTicketActivity extends AppCompatActivity
{

    EditText titreTxt,descTxt;
    ImageView capBtn;

    private ticket tick = new ticket();
    private List<projet> lstProj = new ArrayList<projet>();
    private List<String> lstProjNom = new ArrayList<String>();

    Bitmap bitmap;
    private static final int CODE_GALLERY=999;
    private Switch urgSwitch;
    private Spinner projMenu;
    private ProgressDialog pdLoading ;
    private Uri filePath=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setContentView(R.layout.activity_acceuil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titreTxt = (EditText) findViewById(R.id.titreTxt);
        descTxt = (EditText) findViewById(R.id.descTxt);
        capBtn = (ImageView) findViewById(R.id.capBtn);
        urgSwitch = (Switch) findViewById(R.id.urgSwitch);
        projMenu = (Spinner) findViewById(R.id.projMenu);


        capBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/");
                startActivityForResult(Intent.createChooser(i, "Choisir la capture"), CODE_GALLERY);
            }
        });

        tick.setUrgence(false);


        if(getIntent().getExtras()!=null)
        {
            Bundle bundle = getIntent().getExtras();
            this.setTitle("Modification du ticket");
            titreTxt.setText((bundle.getString("titre")));
            descTxt.setText((bundle.getString("description")));
            if(!bundle.getString("image").equals("null"))
                Picasso.with(getApplicationContext()).load("https://tmastering.000webhostapp.com/" + bundle.getString("image")).into(capBtn);
            if(bundle.getString("urgence").equals("urgent"))
                urgSwitch.setChecked(true);
        }

        new afficherProjet().execute();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==CODE_GALLERY&&data!=null) {
            try {
                filePath = data.getData();

                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                capBtn.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                  }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.acceuil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id== R.id.insTicket)
        {
            if(verif()) {
            if(urgSwitch.isChecked())
                tick.setUrgence(true);
            tick.setProjet(lstProj.get(projMenu.getSelectedItemPosition()));
            tick.setTitre(titreTxt.getText().toString());
            tick.setDescription(descTxt.getText().toString());
            if(urgSwitch.isChecked())
                tick.setUrgence(true);
            else
                tick.setUrgence(false);

                new ajoutTicket().execute();
                Intent i = new Intent(getApplicationContext(), TicketsActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

//                new modifieTicket().execute();

        }
        else
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        return super.onOptionsItemSelected(item);
    }




    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodedImage;
    }

    private class ajoutTicket extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(NouveauTicketActivity.this);
             pdLoading.setMessage("\tAjout du ticket...");
            pdLoading.show();

        }


    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/ajouter_ticket.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pdLoading.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                if(tick.isUrgence())
                    map.put("urgence","1");
                else
                    map.put("urgence","0");
                map.put("id_projet", String.valueOf(tick.getProjet().getId()));
                map.put("titre", tick.getTitre());
                map.put("description", tick.getDescription());
               if(filePath!=null)
                    map.put("image", imageToString(bitmap));
                return map;
            }
        };
        requestQueue.add(request);
    }



    private class modifieTicket extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(NouveauTicketActivity.this);
            pdLoading.setMessage("\tModification du ticket...");
            pdLoading.show();

        }

    }


    private void makeRequest2() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_ticket.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Ticket modifié", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",getIntent().getStringExtra("id"));
                if(tick.isUrgence())
                    map.put("urgence","1");
                else
                    map.put("urgence","0");
                map.put("id_projet", String.valueOf(tick.getProjet().getId()));
                map.put("titre", tick.getTitre());
                map.put("description", tick.getDescription());
               if(filePath!=null)
                    map.put("image", imageToString(bitmap));
                return map;
            }
        };
        requestQueue.add(request);
    }



    private class afficherProjet extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_projets_termines.php?client="+new DatabaseHandler(getApplicationContext()).getIdUser();
            pdLoading = new ProgressDialog(NouveauTicketActivity.this);
            pdLoading.setMessage("\tChargement des projets..");
            pdLoading.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                StringBuilder JSON_DATA = new StringBuilder();
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((json_string = reader.readLine()) != null) {
                    JSON_DATA.append(json_string).append("\n");
                }
                return JSON_DATA.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                do {
                    if (result != null) {
                        json_string = result;
                        lstProj.clear();
                        pdLoading.dismiss();
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id_proj = jo.getInt("proj_id");
                            String nom = jo.getString("proj_nom");
                            projet projet = new projet(id_proj, nom);
                            lstProj.add(projet);
                            lstProjNom.add(nom);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, lstProjNom);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            projMenu.setAdapter(dataAdapter);
                        }
                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }

    private boolean verif()
    {
        boolean res=true;
        if(projMenu.getCount()==0)
        {
            res=false;
            Toast.makeText(getApplicationContext(),"Vous n'avez pas un projet terminé",Toast.LENGTH_LONG).show();
        }
        if(titreTxt.getText().toString().length()==0)
        {
            res=false;
            Toast.makeText(getApplicationContext(),"Saisie le titre",Toast.LENGTH_LONG).show();
        }
        if(descTxt.getText().toString().length()==0)
        {
            res=false;
            Toast.makeText(getApplicationContext(),"Saisie la description",Toast.LENGTH_LONG).show();
        }
        return res;
    }


}
