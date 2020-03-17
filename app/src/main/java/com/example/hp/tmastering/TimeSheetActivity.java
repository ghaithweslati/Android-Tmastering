package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.activite;
import com.example.hp.tmastering.beans.operateur;
import com.example.hp.tmastering.beans.tache;
import com.example.hp.tmastering.beans.timesheet;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeSheetActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private CompactCalendarView calendarView;
    private LinearLayout dropLayout;
    private TimesheetAdapter timesheetAdapter;
    private ListView lstAct;
    private ListView listView;
    private MesTachesAdapter mesTachesAdapter;
    private TextView alertDispo;
    private LinearLayout timesheetLayout;
    private LinearLayout userLayout;
    private Spinner lstOp;
    private List<operateur> lstOper = new ArrayList<operateur>();
    private List<String> lstNomOper = new ArrayList<String>();
    private afficherActivites af = null;;
    private ProgressDialog progressDialog;
    private int selected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_time_sheet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        calendarView = (CompactCalendarView) findViewById(R.id.calendar);
        dropLayout = (LinearLayout) findViewById(R.id.dropLayout);
        listView = (ListView) findViewById(R.id.lstTaches);
        lstAct = (ListView) findViewById(R.id.lstAct);
        alertDispo = (TextView) findViewById(R.id.alertDispo);

        timesheetLayout = (LinearLayout) findViewById(R.id.timesheetLayout);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);
        lstOp = (Spinner) findViewById(R.id.lstOp);

        LinearLayout ticketPage = (LinearLayout) findViewById(R.id.ticketLayout);
        ticketPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TicketsActivity.class));
            }
        });

        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Admin"))
            userLayout.setVisibility(View.VISIBLE);
        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet")||new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Operateur"))
            timesheetLayout.setVisibility(View.VISIBLE);

        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet")||new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Client")) {
            listView.setVisibility(View.GONE);
            lstOp.setVisibility(View.VISIBLE);
            ticketPage.setVisibility(View.VISIBLE);
            new afficher_operateurs().execute();
        }

        lstOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected=i;
                new afficherActivites(new operateur(lstOper.get(i).getId())).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Calendar mCalendar = Calendar.getInstance();
        final String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRANCE);

        this.setTitle(month+" ▾");

        new afficherTaches().execute();

        calendarView.setUseThreeLetterAbbreviation(true);


        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
              }

            @Override
            public void onMonthScroll(Date date) {
                TimeSheetActivity.this.setTitle(date.toLocaleString().substring(0,3)+" ▾");
                if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet"))
                        af = new afficherActivites(lstOper.get(selected));
                else
                        af =  new afficherActivites(new operateur(new DatabaseHandler(getApplicationContext()).getIdUser()));
                Calendar c1 = Calendar.getInstance();
                c1.setTime(date);
                af.setCalendar(c1);
                af.execute();
            }
        });


        dropLayout.setVisibility(View.GONE);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

           if(dropLayout.getVisibility()== View.VISIBLE)
            {
                TimeSheetActivity.this.setTitle(month+" ▾");
                dropLayout.setVisibility(View.GONE);
            }
            else
            {
                TimeSheetActivity.this.setTitle(month+" ▴");
                dropLayout.setVisibility(View.VISIBLE);
            }
            }
        });

        ImageView projetPage = (ImageView) findViewById(R.id.projetPage);
        projetPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProjetsActivity.class));
            }
        });

        timesheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TimeSheetActivity.class));
            }
        });

        ImageView userPage = (ImageView) findViewById(R.id.userPage);
        userPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UtilisateursActivity.class));
            }
        });

        ImageView settingPage = (ImageView) findViewById(R.id.settingPage);
        settingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),UtilisateurActivity.class);
                intent.putExtra("id",String.valueOf(new DatabaseHandler(getApplicationContext()).getIdUser()));
                startActivity(intent);
            }
        });

        ImageView housePage = (ImageView) findViewById(R.id.housePage);
        housePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DashboardAdminActivity.class);
                startActivity(intent);
            }
        });



 /*      if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet"))
            af = new afficherActivites(lstOper.get(selected));
        else
            af =  new afficherActivites(new operateur(new DatabaseHandler(getApplicationContext()).getIdUser()));
        af.execute();*/

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.time_sheet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    private class afficherActivites extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;
        private Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        private operateur operateur;

        public afficherActivites(com.example.hp.tmastering.beans.operateur operateur) {
            this.operateur = operateur;
        }


        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_activites.php?operateur="+this.operateur.getId();
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

            if (result != null) {

                json_string = result;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                calendarView.removeAllEvents();


                timesheetAdapter = new TimesheetAdapter(getApplicationContext(), R.layout.timesheet_row_layout);
                lstAct.setAdapter(timesheetAdapter);

                lstAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Operateur")) {
                            final timesheet timesheet = (timesheet) adapterView.getItemAtPosition(i);

                            if (timesheet.getDate().before(new Date())) {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimeSheetActivity.this);
                                LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View mView = li.inflate(R.layout.heure_travail, null);

                                TextView nomTache = (TextView) mView.findViewById(R.id.nomProjDlg);
                                Button moinBtn = (Button) mView.findViewById(R.id.moinBtn);
                                Button plusBtn = (Button) mView.findViewById(R.id.plusBtn);
                                final EditText heure = (EditText) mView.findViewById(R.id.heureTravailTxt);
                                TextView fermer = (TextView) mView.findViewById(R.id.ferDlg);
                                TextView save = (TextView) mView.findViewById(R.id.saveDlg);

                                moinBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!heure.getText().toString().equals("0")) {
                                            final int houre = Integer.parseInt(heure.getText().toString());
                                            heure.setText(String.valueOf(houre - 1));
                                        }
                                    }
                                });

                                plusBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!heure.getText().toString().equals("8")) {
                                            final int houre = Integer.parseInt(heure.getText().toString());
                                            heure.setText(String.valueOf(houre + 1));
                                        }
                                    }
                                });


                                alertDialogBuilder.setView(mView);
                                final AlertDialog dialog = alertDialogBuilder.create();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                                if (format.format(timesheet.getDate()).equals(format.format(new Date())))
                                    if (mesTachesAdapter.getItem(0).getDateDebReel() != null)
                                        dialog.show();
                                    else
                                        Toast.makeText(getApplicationContext(), "Vous n'avez pas une tâche en cours", Toast.LENGTH_LONG).show();
                                else if (timesheet.getActivite() == null)
                                    Toast.makeText(getApplicationContext(), "Vous ne pouvez pas remplir une date précedente", Toast.LENGTH_LONG).show();
                                else
                                    dialog.show();


                                if (timesheet.getActivite() != null) {
                                    nomTache.setText(timesheet.getActivite().getTache().getTitre());
                                    heure.setText(String.valueOf(timesheet.getActivite().getNb_heure()));
                                } else {
                                    nomTache.setText(mesTachesAdapter.getItem(0).getTitre());
                                }


                                fermer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });


                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (timesheet.getActivite() != null) {
                                            timesheet.getActivite().setNb_heure(Integer.parseInt(heure.getText().toString()));
                                            new modifierHeure(timesheet.getActivite()).execute();
                                        } else {
                                            timesheet.setActivite(new activite(Integer.parseInt(heure.getText().toString()), mesTachesAdapter.getItem(0)));
                                            new ajouterActivite(timesheet.getActivite()).execute();
                                        }
                                        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet"))
                                            af = new afficherActivites(lstOper.get(lstOp.getSelectedItemPosition()));
                                        else
                                            af =  new afficherActivites(new operateur(new DatabaseHandler(getApplicationContext()).getIdUser()));
                                        af.execute();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });


                for (int k = 1; k <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); k++) {
                    calendar.set(Calendar.DAY_OF_MONTH, k);
                    timesheet timesheet = new timesheet(calendar.getTime());
                    try {
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("activite");
                        for (int g = 0; g < jsonArray.length(); g++) {
                            JSONObject jo = jsonArray.getJSONObject(g);
                            final int id = jo.getInt("id");
                            String date = jo.getString("date");
                            final int nb_heure = jo.getInt("nb_heure");
                            String formatted = format.format(timesheet.getDate());
                            final String nom = jo.getString("titre");
                            if (date.equals(formatted)) {
                                Event event = new Event(0xFF0000FF, calendar.getTimeInMillis(), nom);
                                calendarView.addEvent(event);
                                activite activite = new activite(id,date,nb_heure,new tache(nom));
                                timesheet.setActivite(activite);
                            }
                        }
                    }
                    catch (JSONException ex) {
                    }
                    if(!(timesheet.getDate().toString().split(" ")[0].equals("Sun")||timesheet.getDate().toString().split(" ")[0].equals("Sat")))
                    timesheetAdapter.add(timesheet);
                }
                lstAct.setSelection(Integer.parseInt(new Date().toString().split(" ")[2]) - 1);
            }
        }
    }




    private class afficherTaches extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_mes_taches.php";
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
                        mesTachesAdapter = new MesTachesAdapter(getApplicationContext(), R.layout.mestaches_row_layout);
                        listView.setAdapter(mesTachesAdapter);
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("tache");
                                JSONObject jo = jsonArray.getJSONObject(0);
                                tache tache = new tache();
                                int id = jo.getInt("id");
                                String nomTach = jo.getString("titre");
                                int avancement = jo.getInt("avancement");
                                String date_deb_reel = jo.getString("date_deb_reel");
                                tache.setId(id);
                                tache.setTitre(nomTach);
                                tache.setAvancement(avancement);
                                if (date_deb_reel.equals("null") == false)
                                    tache.setDateDebReel(date_deb_reel);
                                mesTachesAdapter.add(tache);
                    }
                } while (result == null);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Vous n'avez pas des tâches à faire", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_LONG).show();
                listView.setVisibility(View.GONE);
                alertDispo.setVisibility(View.VISIBLE);

            }
        }
    }




    private class modifierHeure extends AsyncTask<Void, Void, Void> {

        private activite activite;

        public modifierHeure(activite activite) {
            this.activite=activite;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest(activite);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


    }

    private void makeRequest(final  activite activite) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_heure.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(activite.getId()));
                    map.put("heure", String.valueOf(activite.getNb_heure()));
                    return map;
                }
        };
        requestQueue.add(request);
    }




    private class ajouterActivite extends AsyncTask<Void, Void, Void> {

        private activite activite;

        public ajouterActivite(activite activite) {
            this.activite=activite;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2(activite);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


    }

    private void makeRequest2(final  activite activite) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/ajouter_activite.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_tache", String.valueOf(activite.getTache().getId()));
                map.put("heure", String.valueOf(activite.getNb_heure()));
                return map;
            }
        };
        requestQueue.add(request);
    }


    private class afficher_operateurs extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php?role=Operateur";
            progressDialog = new ProgressDialog(TimeSheetActivity.this);
            progressDialog.setMessage("\tChargement des opérateur..");
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
                        json_string = result;
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        progressDialog.dismiss();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id");
                            String nom = jo.getString("nom");
                            String prenom = jo.getString("prenom");
                            operateur operateur = new operateur(id,nom,prenom);
                            lstOper.add(operateur);
                            lstNomOper.add(operateur.getNom()+" "+operateur.getPrenom());
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_style, lstNomOper);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    lstOp.setAdapter(dataAdapter);

                }while (result==null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
