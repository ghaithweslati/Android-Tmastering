package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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

public class AboutProjet extends Fragment {

    TextView titTxt,dateDebTxt,dateFinTxt,description,etat,clientTxt,chefdeprojet;
    Bundle bundle;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_projet,container,false);


        titTxt = (TextView) rootView.findViewById(R.id.titreInfoTxt);
        dateDebTxt = (TextView) rootView.findViewById(R.id.dateDebInfoTxt);
        dateFinTxt = (TextView) rootView.findViewById(R.id.dateFinInfoTxt);
        description = (TextView) rootView.findViewById(R.id.descInfoProj);
        etat = (TextView) rootView.findViewById(R.id.etatInfoTxt);
        clientTxt = (TextView) rootView.findViewById(R.id.clientInfoTxt);
        chefdeprojet = (TextView) rootView.findViewById(R.id.chefInfoTxt);

        bundle=getActivity().getIntent().getExtras();
        etat.setText(bundle.getString("etat"));

        new afficherProjet().execute();

        return rootView;
    }




    private class afficherChefProjet extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;
        String url;

        @Override
        protected void onPreExecute() {

            JSON_URL = "https://tmastering.000webhostapp.com/afficher_chefdeprojet.php?id=" + bundle.getString("id");
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
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(0);
                            final int id = jo.getInt("id");
                            String nom = jo.getString("nom");
                            String prenom = jo.getString("prenom");
                            chefdeprojet.setText(nom+" "+prenom);
                            chefdeprojet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getContext(), UtilisateurActivity.class);
                                    i.putExtra("id", String.valueOf(id));
                                    startActivity(i);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            });
                        }
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


                if (bundle.getString("etat").equals("En attente"))
                    url = "afficher_projets_enattente.php";
                else if (bundle.getString("etat").equals("En cours"))
                    url = "afficher_projets_encours.php";
                else
                    url = "afficher_projets_termines.php";


            JSON_URL = "https://tmastering.000webhostapp.com/"+url+"?id="+bundle.getString("id");


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
                        new afficherChefProjet().execute();
                        json_string = result;
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                            JSONObject jo = jsonArray.getJSONObject(0);
                            String dateDeb = jo.getString("date_deb");
                            String dateFin = jo.getString("date_fin");
                            String nom = jo.getString("proj_nom");
                            String desc = jo.getString("description");
                            String client = jo.getString("client");
                            final String id = jo.getString("id_clt");
                            int perc=0;
                            String dateDebReel,dateFinReel;
                            getActivity().setTitle(nom);
                            titTxt.setText(nom);
                            dateDebTxt.setText(dateDeb+" - "+dateFin+" (Planifié)" );
                            description.setText(desc);
                            clientTxt.setText(client);
                            clientTxt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getContext(), UtilisateurActivity.class);
                                    i.putExtra("id",id);
                                    startActivity(i);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            });
                            if(etat.getText().toString().equals("En attente"))
                            {
                                dateFinTxt.setText("Non spécifié (Réel)");
                                etat.setText(etat.getText().toString()+" (0%)");
                            }
                            else if(etat.getText().toString().equals("En cours")) {
                                perc = jo.getInt("perc");
                                etat.setText(etat.getText().toString()+" ("+perc+"%)");
                                dateDebReel = jo.getString("date_deb_reel");
                                dateFinTxt.setText(dateDebReel+" - Non spécifié (Réel)");
                            }
                            else
                            {
                                perc = jo.getInt("perc");
                                etat.setText(etat.getText().toString()+" ("+perc+"%)");
                                dateDebReel = jo.getString("date_deb_reel");
                                dateFinReel = jo.getString("date_fin_reel");
                                dateFinTxt.setText(dateDebReel+" - "+dateFinReel+" (Réel)");
                            }
                    }
                } while (result == null);
            } catch (JSONException e) {
Toast.makeText(getContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
