package com.example.hp.tmastering;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.client;
import com.example.hp.tmastering.beans.projet;
import com.example.hp.tmastering.beans.ticket;

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

public class TicketsFragment extends Fragment {

    ListView listTick;
    TicketAdapter ticketAdapter;
    Bundle bundle;
    private View rootView;
    private String url;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.ticket_fragment, container, false);


        listTick = (ListView) rootView.findViewById(R.id.lstTick);

        bundle = getActivity().getIntent().getExtras();

        new afficherTickets().execute();

        return rootView;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private class afficherTickets extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/"+url;
            if(new DatabaseHandler(getContext()).getRoleUser().equals("Client"))
                JSON_URL+="?id_user="+new DatabaseHandler(getContext()).getIdUser();
            else
                JSON_URL+="?chef_projet="+new DatabaseHandler(getContext()).getIdUser();
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
                        ticketAdapter = new TicketAdapter(getContext(), R.layout.row_layout);
                        listTick.setAdapter(ticketAdapter);
                        listTick.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        ticket tick = (ticket) parent.getItemAtPosition(position);
                                        Intent i = new Intent(getContext(), TicketActivity.class);
                                        i.putExtra("id", String.valueOf(tick.getId()));
                                        if(url.contains("afficher_tickets_encours.php"))
                                            i.putExtra("etat", "En cours");
                                        else if(url.contains("afficher_tickets_termines.php"))
                                            i.putExtra("etat", "Terminé");
                                        else
                                            i.putExtra("etat","En attente");
                                        i.putExtra("date",tick.getDate());
                                        i.putExtra("id",String.valueOf(tick.getId()));
                                        i.putExtra("titre",tick.getTitre());
                                        i.putExtra("description",tick.getDescription());
                                        i.putExtra("idProj",String.valueOf(tick.getProjet().getId()));
                                        i.putExtra("projet",tick.getProjet().getNom());
                                        i.putExtra("image",tick.getImage());
                                        if(tick.isUrgence())
                                            i.putExtra("urgence","urgent");
                                        else
                                            i.putExtra("urgence","n'est pas urgent");
                                        getActivity().startActivity(i);
                                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    }
                                });
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("ticket");
                       for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id");
                            String date = jo.getString("date");
                            String titre = jo.getString("titre");
                            String description = jo.getString("description");
                           int idProj = jo.getInt("id_projet");
                           String nomProj = jo.getString("proj_name");
                           String nomClt = jo.getString("nom_client");
                           int idClt = jo.getInt("id_client");
                           String prenomClt = jo.getString("prenom_client");
                           String urgence = jo.getString("urgence");
                           String image = jo.getString("image");
                           boolean urg=false;
                           if(urgence.equals("1"))
                               urg=true;
                           ticket ticket = new ticket(id,new client(idClt,prenomClt,nomClt),new projet(idProj,nomProj),titre,description,date,image,urg);
                           /* if(url.equals("afficher_projets_encours.php"))
                            {
                                projet.setAvancement(jo.getInt("perc"));
                                projetAdapter.setEnCours();
                            }*/
                            ticketAdapter.add(ticket);

                        }
                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }
}

