package com.example.hp.tmastering;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.FilePath;
import com.example.hp.tmastering.beans.chefDeProjet;
import com.example.hp.tmastering.beans.client;
import com.example.hp.tmastering.beans.document;
import com.example.hp.tmastering.beans.projet;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


public class NouveauProjetActivity extends AppCompatActivity {

    private TextView backProj;
    private TextView saveProj;
    private EditText titreProj;
    private TextView dateDebProj;
    private TextView dateFinProj;
    private TextView clientProj;
    private DatePickerDialog datePickerDialog;
    private EditText descProj;
    private projet proj = new projet();
    private List<client> lstClt = new ArrayList<client>();
    private Bundle bundle;
    private SpinnerDialog spinnerDialog;
    private List<chefDeProjet> lstOper = new ArrayList<chefDeProjet>();
    private List<String> lstNomOper = new ArrayList<String>();
    private ProgressDialog progressDialog,progress, progressDialogClt;
    private Spinner spinChef;
    ImageView capBtn;
    private int idChefProjet=-1;

    Uri uri = null;

    public static final String PDF_UPLOAD_HTTP_URL = "https://tmastering.000webhostapp.com/file_upload.php";

    public int PDF_REQ_CODE = 1;

    private String PdfPathHolder, PdfID;
    private document doc = new document();

    private Date date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_projet);


        getSupportActionBar().hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        backProj = (TextView) findViewById(R.id.backProj);
        saveProj = (TextView) findViewById(R.id.saveProj);
        titreProj = (EditText) findViewById(R.id.newProjTxt);
        dateDebProj = (TextView) findViewById(R.id.dateDebProjTxt);
        dateFinProj = (TextView) findViewById(R.id.dateFinProjTxt);
        clientProj = (TextView) findViewById(R.id.clientProj);
        descProj = (EditText) findViewById(R.id.descProj);
        spinChef = (Spinner) findViewById(R.id.chefProjSpin);
        capBtn = (ImageView) findViewById(R.id.capBtn);
        bundle = getIntent().getExtras();

        if(bundle!=null)
            new afficherProjet().execute();
        new initialiser().execute();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }


        capBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
            //    intent.setType("application/pdf");
                intent.setType("image/*");

                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Choisir un document"), PDF_REQ_CODE);


            }
        });


        clientProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });



        backProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        dateDebProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDebProj.setTextColor(getResources().getColor(R.color.black));
                Calendar calendar = Calendar.getInstance();
                int _year = calendar.get(Calendar.YEAR);
                int _month = new Date().getMonth();
                int _day = new Date().getDay();

                datePickerDialog = new DatePickerDialog(NouveauProjetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        proj.setDateDeb(year + "-" + (month+1) + "-" + dayOfMonth);
                        dateDebProj.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, _year, _month, _day);


                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
            }
        });


        dateFinProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateFinProj.setTextColor(getResources().getColor(R.color.black));
                Calendar calendar = Calendar.getInstance();
                int _year = calendar.get(Calendar.YEAR);
                int _month = new Date().getMonth();
                int _day = new Date().getDay();

                datePickerDialog = new DatePickerDialog(NouveauProjetActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        proj.setDateFin(year + "-" + (month+1) + "-" + dayOfMonth);
                        dateFinProj.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, _year, _month, _day);

                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        saveProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    proj.setNom(titreProj.getText().toString());
                   chefDeProjet chefDeProjet = lstOper.get(spinChef.getSelectedItemPosition());
                    proj.setChefDeProjet(new chefDeProjet(chefDeProjet.getId()));
                    proj.setDescription(descProj.getText().toString());

                if(bundle!=null) {
                    proj.setId(Integer.parseInt(bundle.getString("id")));
                    proj.setDateDeb(dateDebProj.getText().toString());
                    proj.setDateFin(dateFinProj.getText().toString());
                        if (verif())
                            new modifier_projet().execute();
                }
                else
                        if(verif())
                           new ajoutProjet().execute();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uri.toString());
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplication().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        doc.setNom(displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                doc.setNom(displayName);
            }
        }


    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void PdfUploadFunction() {


        PdfPathHolder = FilePath.getPath(this, uri);

        if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();

        } else {

            try {

                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "pdf")
                        .addParameter("name", doc.getNom().substring(0,doc.getNom().lastIndexOf('.')))
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }




    private class initialiser extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php";
            progressDialogClt = new ProgressDialog(NouveauProjetActivity.this);
            progressDialogClt.setMessage("\tChargement..");
            progressDialogClt.setCancelable(false);
            progressDialogClt.show();
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
                    if (!lstClt.isEmpty())
                        lstClt.clear();
                    json_string = result;
                    progressDialogClt.dismiss();
                    ArrayList<String> lstClNom = new ArrayList<String>();
                    JSONObject jsonObject = new JSONObject(json_string);
                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                    int index=0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        int id = Integer.parseInt(jo.getString("id"));
                        String nom = jo.getString("nom");
                        String prenom = jo.getString("prenom");
                        String role = jo.getString("role");
                        if(role.equals("Chef de projet")) {
                            chefDeProjet chefDeProjet = new chefDeProjet(id,nom,prenom);
                            lstOper.add(chefDeProjet);
                            lstNomOper.add(chefDeProjet.getNom()+" "+chefDeProjet.getPrenom());
                        if(idChefProjet!=-1)
                            if(id==idChefProjet)
                                index=i;
                        }
                        else if(role.equals("Client")) {
                            String rs = jo.getString("raison_sociale");
                            client cl = new client(id, nom, prenom, rs);
                            lstClt.add(cl);
                            lstClNom.add(cl.toString());
                        }
                    }
                    spinnerDialog = new SpinnerDialog(NouveauProjetActivity.this,lstClNom,"Choisir un client");
                    spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                        @Override
                        public void onClick(String s, int i) {
                            clientProj.setTextColor(getResources().getColor(R.color.black));
                            clientProj.setText(s);
                            proj.setClient(lstClt.get(i));
                        }
                    });

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, lstNomOper);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinChef.setAdapter(dataAdapter);
                    spinChef.setSelection(index);
                }while (result==null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean verif() {

        boolean res=true;

        if(titreProj.getText().toString().length()==0||descProj.getText().toString().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Le champ titre est vide",Toast.LENGTH_LONG).show();
            titreProj.setHintTextColor(getResources().getColor(R.color.red));
            res = false;
        }

        if(descProj.getText().toString().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Le champ description est vide",Toast.LENGTH_LONG).show();
            descProj.setHintTextColor(getResources().getColor(R.color.red));
            res = false;
        }

        if(dateDebProj.getText().toString().equals("DD-MM-YYYY"))
        {
            Toast.makeText(getApplicationContext(),"Séléctionne la date début",Toast.LENGTH_LONG).show();
            dateDebProj.setTextColor(getResources().getColor(R.color.red));
            res = false;
        }

        if(dateFinProj.getText().toString().equals("DD-MM-YYYY"))
        {
            Toast.makeText(getApplicationContext(),"Séléctionne la date fin",Toast.LENGTH_LONG).show();
            dateFinProj.setTextColor(getResources().getColor(R.color.red));
            res = false;
        }
        else {
            Date dateDeb = null;
            Date dateFin = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateDeb = format.parse(proj.getDateDeb());
                dateFin = format.parse(proj.getDateFin());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dateFin.before(dateDeb)) {
                Toast.makeText(getApplicationContext(), "la date de fin doit être aprés la date du début", Toast.LENGTH_LONG).show();
                dateFinProj.setTextColor(getResources().getColor(R.color.red));
                res = false;
            }
        }

        if(clientProj.getText().toString().equals("Sélectionner un client"))
        {
            Toast.makeText(getApplicationContext(),"Sélectionne un client",Toast.LENGTH_LONG).show();
            clientProj.setTextColor(getResources().getColor(R.color.red));
            res = false;
        }


        return res;
    }






    private class ajoutProjet extends AsyncTask<Void, Void, Void> {

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
            progressDialog = new ProgressDialog(NouveauProjetActivity.this);
            progressDialog.setMessage("\tAjout du projet..");
            progressDialog.show();

        }
    }

    private void makeRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, "https://tmastering.000webhostapp.com/ajouter_projet.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if(uri!=null)
                            PdfUploadFunction();
                        startActivity(new Intent(getApplicationContext(), ProjetsActivity.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Toast.makeText(getApplicationContext(),"Projet Ajouté", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("titre", proj.getNom());
                map.put("description", String.valueOf(proj.getDescription()));
                map.put("date_deb", proj.getDateDeb());
                map.put("date_fin", proj.getDateFin());
                map.put("id_client", String.valueOf(proj.getClient().getId()));
                map.put("chef_projet", String.valueOf(proj.getChefDeProjet().getId()));
                if(uri!=null)
                    map.put("document",doc.getNom());
                return map;
            }
        };
        requestQueue.add(request);
    }


    private class modifier_projet extends AsyncTask<Void, Void, Void> {

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
            progressDialog = new ProgressDialog(NouveauProjetActivity.this);
            progressDialog.setMessage("\tModification du projet..");
            progressDialog.show();
        }
    }

    private void makeRequest2() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_projet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), ProjetActivity.class);
                        i.putExtra("id",getIntent().getStringExtra("id"));
                        i.putExtra("etat","En attente");
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        Toast.makeText(getApplicationContext(),"Projet modifié", Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",  String.valueOf(proj.getId()));
                map.put("titre", proj.getNom());
                map.put("description", String.valueOf(proj.getDescription()));
                map.put("date_deb", proj.getDateDeb());
                map.put("date_fin", proj.getDateFin());
                map.put("id_client", String.valueOf(proj.getClient().getId()));
                map.put("chef_projet", String.valueOf(proj.getChefDeProjet().getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }



    private class afficherChefProjet extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;
        String url;

        @Override
        protected void onPreExecute() {

            JSON_URL = "https://tmastering.000webhostapp.com/afficher_chefdeprojet.php?id=" + bundle.getString("id");

            progress = new ProgressDialog(getApplicationContext());
            progress.setMessage("\tChargement des informations..");
            progress.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                        JSONObject jo = jsonArray.getJSONObject(0);
                        idChefProjet = jo.getInt("id");

                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }


    private class afficherProjet extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;
        String url;

        @Override
        protected void onPreExecute() {

            if(bundle.getString("etat").equals("En attente"))
                url="afficher_projets_enattente.php";
            else if(bundle.getString("etat").equals("En cours"))
                url="afficher_projets_encours.php";
            else
                url="afficher_projets_termines.php";

            JSON_URL = "https://tmastering.000webhostapp.com/"+url+"?id="+bundle.getString("id");

            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setMessage("\tChargement des informations..");
            progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
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
                    if (result != null) {
                        progressDialog.dismiss();
                        new afficherChefProjet().execute();
                        json_string = result;
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(0);
                            String dateDeb = jo.getString("date_deb");
                            String dateFin = jo.getString("date_fin");
                            String nom = jo.getString("proj_nom");
                            String desc = jo.getString("description");
                            String client = jo.getString("client");
                            final int id = jo.getInt("id_clt");
                            titreProj.setText(nom);
                            dateDebProj.setText(dateDeb);
                            dateFinProj.setText(dateFin);
                            descProj.setText(desc);
                            clientProj.setText(client);
                            proj.setClient(new client(id));
                        }
                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }

}

