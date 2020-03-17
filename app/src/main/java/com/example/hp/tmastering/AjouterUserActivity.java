package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.chefDeProjet;
import com.example.hp.tmastering.beans.client;
import com.example.hp.tmastering.beans.fonction;
import com.example.hp.tmastering.beans.user;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AjouterUserActivity extends AppCompatActivity {

    private EditText addNom, addPrenom, addLogin, addPass1, addPass2, addEmail, addAdresse, addTel, addRaison;
    private TextView backProf, saveProf;
    private ImageView imageProf;
    private RadioButton radClt, radOp, radChef;
    private static final int CODE_GALLERY = 999;
    private Bitmap bitmap = null;
    private ProgressDialog pdLoading;
    private Switch etatswitch;
    private RelativeLayout etatLayout;
    private Spinner lstFonc;
    private ProgressDialog progressDialog;
    private ProgressDialog progress;
    private ArrayList<fonction> lstFon = new ArrayList<fonction>();
    private user user;
    private int idfonction=-1;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_user);

        getSupportActionBar().hide();

        backProf = (TextView) findViewById(R.id.backProf);
        saveProf = (TextView) findViewById(R.id.saveProf);
        addNom = (EditText) findViewById(R.id.addNom);
        addPrenom = (EditText) findViewById(R.id.addPrenom);
        addLogin = (EditText) findViewById(R.id.addLogin);
        addPass1 = (EditText) findViewById(R.id.addPassword1);
        addPass2 = (EditText) findViewById(R.id.addPassword2);
        addEmail = (EditText) findViewById(R.id.addEmail);
        addAdresse = (EditText) findViewById(R.id.addAdresse);
        addTel = (EditText) findViewById(R.id.addTel);
        addRaison = (EditText) findViewById(R.id.addRaison);
        imageProf = (ImageView) findViewById(R.id.imageProf);
        radClt = (RadioButton) findViewById(R.id.radClt);
        radOp = (RadioButton) findViewById(R.id.radOp);
        radChef = (RadioButton) findViewById(R.id.radChef);
        etatswitch = (Switch) findViewById(R.id.etatSwitch);
        etatLayout = (RelativeLayout) findViewById(R.id.etatLayout);
        lstFonc = (Spinner) findViewById(R.id.lstFonc);

        radChef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
            }
        });

        radOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                lstFonc.setVisibility(View.VISIBLE);
                }
        });

        radClt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                addRaison.setVisibility(View.VISIBLE);
            }
        });


        backProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        saveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = 0;
                if (getIntent().getExtras() != null)
                    id = Integer.parseInt(getIntent().getStringExtra("id"));
                String login = addLogin.getText().toString();
                String nom = addNom.getText().toString();
                String prenom = addPrenom.getText().toString();
                String tel = addTel.getText().toString();
                String adresse = addAdresse.getText().toString();
                String email = addEmail.getText().toString();
                String password = addPass1.getText().toString();
                user = new user(id, login, nom, prenom, password, adresse, email, tel);
                if (radOp.isChecked())
                    user.setRole("Operateur");
                else if (radChef.isChecked())
                    user.setRole("Chef de projet");
                else
                    user.setRole("Client");

                if (verif())
                    if (getIntent().getExtras() == null)
                        new ajouter_user().execute();
                    else
                        new modifier_utilisateur().execute();

            }
        });

        imageProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/");
                startActivityForResult(Intent.createChooser(i, "Choisir la capture"), CODE_GALLERY);
            }
        });

        if(getIntent().getExtras()!=null) {
            etatLayout.setVisibility(View.VISIBLE);
            etatswitch.setVisibility(View.VISIBLE);
        }

        lstFonc.setVisibility(View.GONE);

        if(getIntent().getExtras()!=null) {
            new initialiser().execute();
            init();
        }
        new chargementFonctions().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CODE_GALLERY&&data!=null) {
            try {
                filePath = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageProf.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private class ajouter_user extends AsyncTask<Void, Void, Void> {

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
            pdLoading = new ProgressDialog(AjouterUserActivity.this);
            pdLoading.setMessage("\nAjout de nouveau utilisateur...");
            pdLoading.show();
        }

    }

    private void makeRequest2() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/ajouter_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pdLoading.dismiss();
                        Toast.makeText(getApplicationContext(),"Utilisateur ajouté",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),UtilisateursActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nom", user.getNom());
                map.put("prenom", user.getPrenom());
                map.put("login", user.getLogin());
                map.put("role", user.getRole());
                map.put("password", user.getPassword());
                map.put("tel", user.getTel());
                map.put("adresse", user.getAdresse());
                map.put("email", user.getEmail());
                if (bitmap != null)
                    map.put("image_profil", imageToString(bitmap));
                if (addRaison.getVisibility() == View.VISIBLE)
                    map.put("raison_sociale", addRaison.getText().toString());
                if (lstFonc.getVisibility() == View.VISIBLE)
                    map.put("fonction", String.valueOf(lstFon.get(lstFonc.getSelectedItemPosition()).getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }


    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }





    private class chargementFonctions extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_fonctions.php";
            progressDialog = new ProgressDialog(AjouterUserActivity.this);
            progressDialog.setMessage("\tChargement des fonctions..");
            progressDialog.show();
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
                    json_string = result;
                    progressDialog.dismiss();
                    ArrayList<String> lstFonNom = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(json_string);
                    JSONArray jsonArray = jsonObject.getJSONArray("fonction");
                    int index=0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(jo.getString("id"));
                        String nom = jo.getString("nom");
                        lstFonNom.add(nom);
                        lstFon.add(new fonction(id,nom));
                        if(idfonction!=-1)
                            if(id==idfonction)
                                index=i;
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, lstFonNom);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lstFonc.setAdapter(dataAdapter);
                    lstFonc.setSelection(index);
                    lstFonc.setVisibility(View.GONE);

                }while (result==null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public  class initialiser extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php?id="+getIntent().getStringExtra("id");
            progress = new ProgressDialog(AjouterUserActivity.this);
            progress.setMessage("\tChargement des informations..");
            progress.show();

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
                        progress.dismiss();
                        json_string = result;
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jo = jsonArray.getJSONObject(i);
                            final int id = jo.getInt("id");
                            final String nom = jo.getString("nom");
                            final String prenom = jo.getString("prenom");
                            final String rol = jo.getString("role");
                            String password = jo.getString("password");
                            String login = jo.getString("login");
                            String image_profil = jo.getString("image_profil");
                            String tel = jo.getString("tel");
                            String adresse = jo.getString("adresse");
                            String raison_sociale = jo.getString("raison_sociale");
                            String etat = jo.getString("etat");
                            String email = jo.getString("email");
                            if(rol.equals("Operateur"))
                            idfonction = jo.getInt("id_fonction");
                            addNom.setText(nom);
                            addPrenom.setText(prenom);
                            if(rol.equals("Chef de projet")) {
                                radChef.setChecked(true);
                            }
                            else if(rol.equals("Operateur")) {
                                radOp.setChecked(true);
                                lstFonc.setVisibility(View.VISIBLE);
                            }
                            else {
                                radClt.setChecked(true);
                                addRaison.setVisibility(View.VISIBLE);
                                addRaison.setText(raison_sociale);
                            }
                            addLogin.setText(login);
                            addTel.setText(tel);
                            addAdresse.setText(adresse);
                            if(etat.equals("0"))
                                etatswitch.setChecked(false);
                            else
                                etatswitch.setChecked(true);

                            Picasso.with(getApplicationContext()).load("https://tmastering.000webhostapp.com/"+image_profil).into(imageProf);

                            addEmail.setText(email);
                        }
                    }
                } while (result == null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private class modifier_utilisateur extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest3();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(AjouterUserActivity.this);
            pdLoading.setMessage("\nModification d'utilisateur...");
            pdLoading.show();

        }

    }

    private void makeRequest3() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_profil.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pdLoading.dismiss();
                        Intent i = new Intent(getApplicationContext(),UtilisateurActivity.class);
                        i.putExtra("id",String.valueOf(getIntent().getStringExtra("id")));
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Toast.makeText(getApplicationContext(),"Modification terminé",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdLoading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",  String.valueOf(user.getId()));
                map.put("nom", user.getNom());
                map.put("prenom", user.getPrenom());
                map.put("login", user.getLogin());
                if(user.getPassword().length()>0)
                    map.put("password", user.getPassword());
                map.put("tel", user.getTel());
                map.put("adresse", user.getAdresse());
                map.put("email", user.getEmail());
                map.put("role", user.getRole());
                if(filePath!=null)
                    map.put("image_profil", imageToString(bitmap));
                if(addRaison.getVisibility()== View.VISIBLE)
                    map.put("raison_sociale", addRaison.getText().toString());
                if(lstFonc.getVisibility()== View.VISIBLE)
                    map.put("fonction", String.valueOf(lstFon.get(lstFonc.getSelectedItemPosition()).getId()));
                if(etatswitch.isChecked())
                    map.put("etat", "1");
                else
                    map.put("etat", "0");
                return map;
            }
        };
        requestQueue.add(request);
    }




    private void init() {
        addRaison.setVisibility(View.GONE);
        lstFonc.setVisibility(View.GONE);
    }

    public boolean verif()
    {
        boolean res=true;
        if(addNom.getText().toString().length()==0||addPrenom.getText().toString().length()==0||addAdresse.getText().toString().length()==0||addTel.getText().toString().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Tous les champs doivent être remplis",Toast.LENGTH_LONG).show();
            res = false;
        }

        if(addRaison.getVisibility()==View.VISIBLE)
        {
            if(addRaison.getText().toString().length()==0)
            {
                Toast.makeText(getApplicationContext(),"Tous les champs doivent être remplis",Toast.LENGTH_LONG).show();
                return false;
            }
        }

            if (addPass1.getText().toString().equals(addPass2.getText().toString()) == false) {
                Toast.makeText(getApplicationContext(), "les deux mots de passe doivent être identique", Toast.LENGTH_LONG).show();
                addPass1.setTextColor(getResources().getColor(R.color.red));
                addPass2.setTextColor(getResources().getColor(R.color.red));
                res = false;
            }
            if (addPass1.getText().toString().length() < 6) {
                Toast.makeText(getApplicationContext(), "la taille du mot de passe doit être suppérieur à 6", Toast.LENGTH_LONG).show();
                addPass1.setTextColor(getResources().getColor(R.color.red));
                res = false;
            }
            if (addPass2.getText().toString().length() < 6) {
                Toast.makeText(getApplicationContext(), "la taille du mot de passe doit être suppérieur à 6", Toast.LENGTH_LONG).show();
                addPass2.setTextColor(getResources().getColor(R.color.red));
                res = false;
            }

        return res;
    }

}
