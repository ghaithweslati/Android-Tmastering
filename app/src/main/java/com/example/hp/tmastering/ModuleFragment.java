package com.example.hp.tmastering;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

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

/**
 * Created by HP on 05/03/2018.
 */

public class ModuleFragment extends Fragment {

    Bundle bundle;
    private View rootView;
    private WebView webView;
    private ModuleAdapter moduleAdapter;
    private ListView lstModules;
    private projet prj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gantt_modules,container,false);
        bundle=getActivity().getIntent().getExtras();
        lstModules = (ListView) rootView.findViewById(R.id.lstMod);
        webView = (WebView) rootView.findViewById(R.id.gantMod);
        webView.getSettings().setJavaScriptEnabled(true);
        new afficher_modules().execute();
        return rootView;
    }



    private class afficher_modules extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_modules.php?id_proj="+bundle.getString("id");
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
                        moduleAdapter = new ModuleAdapter(getContext(), R.layout.module_row);
                        moduleAdapter.setEditable();
                        lstModules.setAdapter(moduleAdapter);
                        lstModules.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        module module = (module) parent.getItemAtPosition(position);
                                        webView.loadUrl(
                                                "https://tmastering.000webhostapp.com/gant_taches.php?id_module="+module.getId());

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
                            prj = new projet(jo.getString("proj_date_deb"), jo.getString("proj_date_fin"));
                        }
                        webView.loadUrl(
                                "https://tmastering.000webhostapp.com/gant_modules.php?id_proj="+bundle.getString("id"));
                    }
                } while (result == null);
            } catch (JSONException e) {
                Toast.makeText(getContext(),"Pas de module dans ce projet", Toast.LENGTH_LONG).show();
            }
        }
    }

}




