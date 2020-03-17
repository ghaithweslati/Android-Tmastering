package com.example.hp.tmastering;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
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
import com.example.hp.tmastering.beans.projet;

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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModulesActivity extends AppCompatActivity {

    private ModuleAdapter moduleAdapter;
    private ListView lstModules;
    private DatePickerDialog datePickerDialog;
    private projet prj;
    private WebView webView;
    private TextView nomMod;
    private TextView dateDebMod;
    private TextView dateFinMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        webView = (WebView) findViewById(R.id.gantMod);
        webView.getSettings().setJavaScriptEnabled(true);
        this.setTitle(getIntent().getStringExtra("titre"));

        prj = new projet(getIntent().getExtras().getString("date_deb"),getIntent().getExtras().getString("date_fin"));


        lstModules = (ListView) findViewById(R.id.lstMod);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        new afficher_modules().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getApplicationContext());
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView = inflater.inflate(R.layout.module_layout, null);
                nomMod = (TextView) mView.findViewById(R.id.nomMod);
                dateDebMod = (TextView) mView.findViewById(R.id.dateDebMod);
                dateFinMod = (TextView) mView.findViewById(R.id.dateFinMod);


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




                dateDebMod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date dateDeb = null;
                        Date dateFin = null;

                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            dateDeb = format.parse(prj.getDateDeb());
                            dateFin = format.parse(prj.getDateFin());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        int _year = calendar.get(Calendar.YEAR);
                        int _month = dateDeb.getMonth();
                        int _day = dateFin.getDay();

                        datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateDebMod.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        },_year,_month,_day);
                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        datePickerDialog.getDatePicker().setMinDate(dateDeb.getTime());
                        datePickerDialog.getDatePicker().setMaxDate(dateFin.getTime());
                        datePickerDialog.show();


                    }
                });


                dateFinMod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date dateDeb = null;
                        Date dateFin = null;
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            dateDeb = format.parse(prj.getDateDeb());
                            dateFin = format.parse(prj.getDateFin());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        int _year = calendar.get(Calendar.YEAR);
                        int _month = dateDeb.getMonth();
                        int _day = dateDeb.getDay();

                        datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateFinMod.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                            }
                        },_year,_month,_day);
                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                        datePickerDialog.getDatePicker().setMinDate(dateDeb.getTime());
                        datePickerDialog.getDatePicker().setMaxDate(dateFin.getTime());
                        datePickerDialog.show();
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String nom = nomMod.getText().toString();
                        String dateDeb = dateDebMod.getText().toString();
                        String dateFin = dateFinMod.getText().toString();
                        projet projet = new projet(Integer.parseInt(getIntent().getStringExtra("id")));

                        if(verif()) {
                            new ajouter_module(new module(nom, dateDeb, dateFin, projet)).execute();
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

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


    private class afficher_modules extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_modules.php?id_proj="+getIntent().getStringExtra("id");
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
                    moduleAdapter = new ModuleAdapter(getApplicationContext(), R.layout.module_row);
                    lstModules.setAdapter(moduleAdapter);
                    lstModules.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    module module = (module) parent.getItemAtPosition(position);
                                    Intent i = new Intent(getApplicationContext(), TachesActivity.class);
                                    i.putExtra("id", String.valueOf(module.getId()));
                                    i.putExtra("nom", module.getNom());
                                    i.putExtra("dateDeb", module.getDateDeb());
                                    i.putExtra("dateFin", module.getDateFin());
                                    startActivity(i);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            });
                    JSONObject jsonObject = new JSONObject(json_string);
                    JSONArray jsonArray = jsonObject.getJSONArray("module");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        projet proj = new projet(jo.getInt("id_projet"), jo.getString("proj_date_deb"), jo.getString("proj_date_fin"));
                        int id = jo.getInt("id");
                        String nom = jo.getString("nom");
                        String dateDeb = jo.getString("date_deb");
                        String dateFin = jo.getString("date_fin");
                        moduleAdapter.add(new module(id, nom, dateDeb, dateFin, proj));
                    }
                    ModulesActivity.this.setTitle(jsonArray.getJSONObject(0).getString("proj_nom"));
                    webView.loadUrl(
                            "https://tmastering.000webhostapp.com/gant_modules.php?id_proj="+getIntent().getStringExtra("id"));
                }
            } while (result == null);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"Pas de module dans ce projet", Toast.LENGTH_LONG).show();
        }
    }
}


    private class ajouter_module extends AsyncTask<Void, Void, Void> {

        private module module;

        public ajouter_module(module module) {
            this.module = module;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2(module);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }


    private void makeRequest2(final module module) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/ajouter_module.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),"Module ajouté",Toast.LENGTH_LONG).show();
                        new afficher_modules().execute();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nom", module.getNom());
                map.put("date_deb", module.getDateDeb());
                map.put("date_fin", module.getDateFin());
                map.put("id_projet", String.valueOf(module.getProjet().getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }


    public boolean verif()
    {
        boolean res=true;
        if(dateDebMod.getText().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Choisis la date début du module",Toast.LENGTH_LONG).show();
            res=false;
        }

        if(dateFinMod.getText().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Choisis la date fin du module",Toast.LENGTH_LONG).show();
            res=false;
        }
        else
        {
            Date dateDeb = null;
            Date dateFin = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateDeb = format.parse(dateDebMod.getText().toString());
                dateFin = format.parse(dateFinMod.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dateFin.before(dateDeb))
            {
                Toast.makeText(getApplicationContext(),"la date fin doit être aprés la date du début",Toast.LENGTH_LONG).show();
                res = false;
            }
        }

        if(nomMod.getText().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Saisi le nom du module",Toast.LENGTH_LONG).show();
            res=false;
        }

        return res;
    }



}
