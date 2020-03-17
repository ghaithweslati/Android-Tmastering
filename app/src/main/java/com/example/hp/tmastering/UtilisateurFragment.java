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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hp.tmastering.beans.user;

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

public class UtilisateurFragment extends Fragment {

    private  ProfilAdapter profilAdapter;
    private GridView lstView;
    private View rootView;
    private String role="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_utilisateurs,container,false);


            lstView = (GridView) rootView.findViewById(R.id.lstUser);

        new initialiser().execute();

        return rootView;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public  class initialiser extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php";

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
                        profilAdapter = new ProfilAdapter(getContext(), R.layout.profil_row_layout);
                        lstView.setAdapter(profilAdapter);

                        lstView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                user user = (user) parent.getItemAtPosition(position);
                                Intent intent = new Intent(getContext(), UtilisateurActivity.class);
                                intent.putExtra("id",String.valueOf(user.getId()));
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });

                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            final JSONObject jo = jsonArray.getJSONObject(i);
                            final int id = Integer.parseInt(jo.getString("id"));
                            final String nom = jo.getString("nom");
                            final String prenom = jo.getString("prenom");
                            final String rol = jo.getString("role");
                            String password = jo.getString("password");
                            String login = jo.getString("login");
                            String image_profil = jo.getString("image_profil");
                            String tel = jo.getString("tel");
                            String adresse = jo.getString("adresse");
                            String raison_sociale = jo.getString("raison_sociale");
                            boolean et = true;
                            String etat = jo.getString("etat");
                            if (etat.equals("0"))
                                et = false;
                            String email = jo.getString("email");

                            if(role!="") {
                                if (role.equals(rol)) {
                                    profilAdapter.add(new user(id, login, password, nom, prenom, tel, adresse, email, image_profil, et, rol));
                                }
                            }
                            else
                                profilAdapter.add(new user(id, login, password, nom, prenom, tel, adresse, email, image_profil, et, rol));
                        }
                    }
                } while (result == null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
