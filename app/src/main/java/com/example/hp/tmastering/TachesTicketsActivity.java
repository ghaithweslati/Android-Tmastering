package com.example.hp.tmastering;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.module;
import com.example.hp.tmastering.beans.operateur;
import com.example.hp.tmastering.beans.tache;
import com.example.hp.tmastering.beans.tacheTicket;
import com.example.hp.tmastering.beans.ticket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TachesTicketsActivity extends AppCompatActivity {

    private  TacheAdapter tacheAdapter;
    private ListView lstTache;
    private DatePickerDialog datePickerDialog;
    private Spinner spinOp;
    private ProgressDialog progressDialogOper;
    private List<operateur> lstOper = new ArrayList<operateur>();
    private List<String> lstNomOper = new ArrayList<String>();
    private WebView webView;
    private TextView nomTach;
    private TextView dateDebTach;
    private TextView dateFinTach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taches_tickets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.gantTache);
        webView.getSettings().setJavaScriptEnabled(true);

        lstTache = (ListView) findViewById(R.id.lstTache);

        this.setTitle((getIntent().getStringExtra("nom")));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                View mView = inflater.inflate(R.layout.tache_layout,null);

                nomTach  = (TextView) mView.findViewById(R.id.nomTach);
                dateDebTach = (TextView) mView.findViewById(R.id.dateDebTach);
                dateFinTach = (TextView) mView.findViewById(R.id.dateFinTach);
                spinOp = (Spinner) mView.findViewById(R.id.spinOp);

                new afficher_operateurs().execute();


                dateDebTach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int _year = calendar.get(Calendar.YEAR);
                        int _month = calendar.get(Calendar.MONTH);
                        int _day = calendar.get(Calendar.DAY_OF_YEAR);
                        datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateDebTach.setText(year + "-" + (month+1) + "-" + dayOfMonth);;
                            }
                        },_year,_month,_day);
                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                        datePickerDialog.show();
                    }
                });


                dateFinTach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int _year = calendar.get(Calendar.YEAR);
                        int _month = calendar.get(Calendar.MONTH);
                        int _day = calendar.get(Calendar.DAY_OF_YEAR);

                        datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateFinTach.setText(year + "-" + (month+1) + "-" + dayOfMonth);;
                            }
                        },_year,_month,_day);
                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                        datePickerDialog.show();
                    }
                });




                TextView fermer = (TextView) mView.findViewById(R.id.ferDlg);
                final TextView save = (TextView) mView.findViewById(R.id.saveDlg);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                dialog.show();


                fermer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        operateur operateur = lstOper.get(spinOp.getSelectedItemPosition());
                        ticket ticket = new ticket(Integer.parseInt(getIntent().getStringExtra("id")));
                        tacheTicket tacheTick = new tacheTicket(nomTach.getText().toString(),dateDebTach.getText().toString(),dateFinTach.getText().toString(),operateur,ticket);
                        if(verif()) {
                            new ajouter_tacheTick(tacheTick).execute();
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

        new afficher_taches().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        return super.onOptionsItemSelected(item);
    }



    private class afficher_operateurs extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php?role=Operateur";
            progressDialogOper = new ProgressDialog(getApplicationContext());
            progressDialogOper.setMessage("\tChargement des operateurs..");
            progressDialogOper.setCancelable(false);
            progressDialogOper.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            progressDialogOper.show();
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
                        progressDialogOper.dismiss();
                        json_string = result;
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        lstNomOper.clear();
                        lstOper.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id");
                            String nom = jo.getString("nom");
                            String prenom = jo.getString("prenom");
                            String image_profil = jo.getString("image_profil");
                            operateur operateur = new operateur(id,nom,prenom,image_profil);
                            lstOper.add(operateur);
                            lstNomOper.add(operateur.getNom()+" "+operateur.getPrenom());
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, lstNomOper);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinOp.setAdapter(dataAdapter);
                }while (result==null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    private class afficher_taches extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_taches.php?id_ticket="+getIntent().getStringExtra("id");
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
                        tacheAdapter = new TacheAdapter(getApplicationContext(), R.layout.tache_row);
                        lstTache.setAdapter(tacheAdapter);
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("tache");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id_tache");
                            String titre = jo.getString("tache_nom");
                            String dateDebPlan = jo.getString("date_deb_plan");
                            String dateFinPlan = jo.getString("date_fin_plan");
                            String image_profil = jo.getString("image_profil");
                            int id_operateur = jo.getInt("id_operateur");
                            String nom = jo.getString("user_nom");
                            String prenom = jo.getString("prenom");

                            tacheAdapter.add(new tache(id,titre,dateDebPlan,dateFinPlan,new operateur(id_operateur,nom,prenom,image_profil)));
                        }
                        webView.loadUrl(
                                "https://tmastering.000webhostapp.com/gant_taches.php?id_ticket="+getIntent().getStringExtra("id"));

                    }
                } while (result == null);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Pas de tache pour ce ticket", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class ajouter_tacheTick extends AsyncTask<Void, Void, Void> {

        private tacheTicket tacheTick;

        public ajouter_tacheTick(tacheTicket tacheTick) {
            this.tacheTick = tacheTick;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest(tacheTick);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }

    private void makeRequest(final tacheTicket tacheTick) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/ajouter_tache.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Tache ajouté",Toast.LENGTH_LONG).show();
                        new afficher_taches().execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("date_deb_plan", tacheTick.getDateDeb());
                map.put("date_fin_plan", tacheTick.getDateFin());
                map.put("titre",tacheTick.getTitre());
                map.put("id_operateur", String.valueOf(tacheTick.getOperateur().getId()));
                map.put("id_ticket", String.valueOf(tacheTick.getTicket().getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }



    public boolean verif()
    {
        boolean res=true;
        if(dateDebTach.getText().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Choisis la date début du tache",Toast.LENGTH_LONG).show();
            res=false;
        }

        if(dateFinTach.getText().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Choisis la date fin du tache",Toast.LENGTH_LONG).show();
            res=false;
        }
        else
        {
            Date dateDeb = null;
            Date dateFin = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateDeb = format.parse(dateDebTach.getText().toString());
                dateFin = format.parse(dateFinTach.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dateFin.before(dateDeb))
            {
                Toast.makeText(getApplicationContext(),"la date fin doit être aprés la date du début",Toast.LENGTH_LONG).show();
                res = false;
            }
        }

        if(nomTach.getText().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Saisi le nom du tache",Toast.LENGTH_LONG).show();
            res=false;
        }

        return res;
    }

}
