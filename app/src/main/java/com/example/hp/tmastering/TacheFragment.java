package com.example.hp.tmastering;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.tmastering.beans.operateur;
import com.example.hp.tmastering.beans.tache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HP on 05/03/2018.
 */

public class TacheFragment extends Fragment {

    ListView listTach;
    Bundle bundle;
    private TacheAdapter tacheAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tache,container,false);


        listTach = (ListView) rootView.findViewById(R.id.listTach);

        bundle=getActivity().getIntent().getExtras();


        new afficher_taches().execute();

        return rootView;
    }



    private class afficher_taches extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_taches.php?id_mission="+getActivity().getIntent().getStringExtra("id");
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
                        tacheAdapter = new TacheAdapter(getContext(), R.layout.tache_row);
                        listTach.setAdapter(tacheAdapter);
                        listTach.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                     /*   module module = (module) parent.getItemAtPosition(position);
                                        Intent i = new Intent(getApplicationContext(), TachesActivity.class);
                                        i.putExtra("id", String.valueOf(module.getId()));
                                   /* i.putExtra("titre", proj.getNom());
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
                            int id = jo.getInt("id_tache");
                            String titre = jo.getString("titre");
                            String dateDebPlan = jo.getString("date_deb_plan");
                            String dateFinPlan = jo.getString("date_fin_plan");
                            String image_profil = jo.getString("image_profil");
                            int id_operateur = jo.getInt("id_operateur");
                            String nom = jo.getString("nom");
                            String prenom = jo.getString("prenom");

                            tacheAdapter.add(new tache(id,titre,dateDebPlan,dateFinPlan,new operateur(id_operateur,nom,prenom,image_profil)));
                        }
                    }
                } while (result == null);
            } catch (JSONException e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}




