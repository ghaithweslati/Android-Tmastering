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

import com.example.hp.tmastering.beans.document;

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

public class DocumentProjet extends Fragment {

    GridView listDoc;
    Bundle bundle;
    private DocumentAdapter documentAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.document_projet,container,false);


        listDoc = (GridView) rootView.findViewById(R.id.listDoc);

        bundle=getActivity().getIntent().getExtras();

        new afficherDocuments().execute();

        return rootView;
    }

    private class afficherDocuments extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_documents.php?id_proj="+bundle.getString("id");
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
                    if(result!=null) {
                        json_string = result;
                        documentAdapter = new DocumentAdapter(getContext(), R.layout.document_row_layout);
                        listDoc.setAdapter(documentAdapter);
                        listDoc.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        document doc = (document) parent.getItemAtPosition(position);
                                        String url = "http://docs.google.com/viewer?url=https://tmastering.000webhostapp.com/documents/"+doc.getNom();
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);

                                    }
                                });
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("document");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            String nom = jo.getString("nom");
                            String path = jo.getString("path");
                            document doc = new document(nom,path);
                            documentAdapter.add(doc);
                        }
                    }
                }while (result==null);
            } catch (JSONException e) {
                Toast.makeText(getContext(),"Pas de documents",Toast.LENGTH_LONG).show();
            }
        }
    }
}




