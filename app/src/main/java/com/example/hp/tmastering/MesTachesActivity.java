package com.example.hp.tmastering;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.tmastering.beans.activite;
import com.example.hp.tmastering.beans.tache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MesTachesActivity extends AppCompatActivity {

    private ListView listView;
    private MesTachesAdapter mesTachesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mestaches);

        this.setTitle("Mes tâches");
        listView = (ListView) findViewById(R.id.lstAct);

        new afficherActivites().execute();
    }




    private class afficherActivites extends AsyncTask<Void, Void, String> {
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
                        listView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                /*        projet proj = (projet) parent.getItemAtPosition(position);
                                        Intent i = new Intent(getApplicationContext(), ProjetActivity.class);
                                        i.putExtra("id", String.valueOf(proj.getId()));
                                        i.putExtra("titre", proj.getNom());
                                        i.putExtra("dateDeb", proj.getDateDeb());
                                        i.putExtra("dateFin", proj.getDateFin());
                                        i.putExtra("description", proj.getDescription());
                                        startActivity(i);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                                    }
                                });
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("tache");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            tache tache = new tache();
                            int  id = jo.getInt("id");
                            String nomTach = jo.getString("titre");
                            int avancement = jo.getInt("avancement");
                            tache.setId(id);
                            tache.setTitre(nomTach);
                            tache.setAvancement(avancement);
                            mesTachesAdapter.add(tache);
                        }
                    }
                } while (result == null);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        }
    }
}
