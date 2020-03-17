package com.example.hp.tmastering;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.document;
import com.example.hp.tmastering.beans.projet;

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

public class ProjetsFragment extends Fragment {

    ListView listProj;
    ProjetAdapter projetAdapter;
    Bundle bundle;
    private View rootView;
    private String url;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.projet_en_attente, container, false);


        listProj = (ListView) rootView.findViewById(R.id.lstProjAtt);

        bundle = getActivity().getIntent().getExtras();

        new afficherProjet().execute();

        return rootView;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private class afficherProjet extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/"+url;
            if(new DatabaseHandler(getContext()).getRoleUser().equals("Chef de projet"))
                JSON_URL+="?role="+new DatabaseHandler(getContext()).getIdUser();
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
                        projetAdapter = new ProjetAdapter(getContext(), R.layout.projet_row_layout);
                        listProj.setAdapter(projetAdapter);
                        listProj.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        projet proj = (projet) parent.getItemAtPosition(position);
                                        Intent i = new Intent(getContext(), ProjetActivity.class);
                                        i.putExtra("id", String.valueOf(proj.getId()));
                                        if(url.contains("afficher_projets_encours.php"))
                                            i.putExtra("etat", "En cours");
                                        else if(url.contains("afficher_projets_termines.php"))
                                            i.putExtra("etat", "Terminé");
                                        else
                                            i.putExtra("etat","En attente");
                                        i.putExtra("date_deb", String.valueOf(proj.getDateDeb()));
                                        i.putExtra("date_fin", String.valueOf(proj.getDateFin()));
                                        getActivity().startActivity(i);
                                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                });
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id_proj = jo.getInt("proj_id");
                            String dateDeb = jo.getString("date_deb");
                            String dateFin = jo.getString("date_fin");
                            String nom = jo.getString("proj_nom");
                            String description = jo.getString("description");
                            projet projet = new projet(id_proj, nom, dateDeb, dateFin, description);
                            if(url.equals("afficher_projets_encours.php"))
                            {
                                projet.setAvancement(jo.getInt("perc"));
                                projetAdapter.setEnCours();
                            }
                            projetAdapter.add(projet);
                        }
                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }
}

