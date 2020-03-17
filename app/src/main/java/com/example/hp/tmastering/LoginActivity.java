package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.user;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText login, pass;
    Button conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        login = (EditText) findViewById(R.id.loginTxt);
        pass = (EditText) findViewById(R.id.passTxt);
        conn = (Button) findViewById(R.id.connBtn);

        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncLogin().execute();

            }
        });

    }


    private class AsyncLogin extends AsyncTask<Void, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        String json_string;
        String JSON_URL;


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
        protected void onPreExecute() {
            super.onPreExecute();
            JSON_URL = "https://tmastering.000webhostapp.com/login.php?login="+login.getText().toString()+"&password="+pass.getText().toString();
            pdLoading.setMessage("\tConnexion...");
            pdLoading.show();

        }



        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();
            try {
                if (result != null) {
                    json_string = result;
                    JSONObject jsonObject = new JSONObject(json_string);
                    int resultat = jsonObject.getInt("success");
                    switch (resultat) {
                        case 0: Toast.makeText(LoginActivity.this, "Mot de passe ou login incorrecte", Toast.LENGTH_LONG).show();break;
                        case 1 : {
                            Intent i = null;
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                            JSONObject jo = jsonArray.getJSONObject(0);
                            int id = jo.getInt("id");
                            String role = jo.getString("role");
                            boolean etat=true;
                           if(jo.getInt("etat")==0)
                               etat=false;
                            if(etat) {
                                Class redirect = null;
                                if (role.equals("Admin") || role.equals("Chef de projet"))
                                    redirect = ProjetsActivity.class;
                                else if (role.equals("Client"))
                                    redirect = TicketsActivity.class;
                                else if (role.equals("Operateur"))
                                    redirect = TimeSheetActivity.class;


                                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                db.addUser(new user(id, role));

                                startActivity(new Intent(getApplicationContext(), redirect));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                            else
                                Toast.makeText(getApplicationContext(),"Votre compte est bloquée",Toast.LENGTH_LONG).show();

                            break;
                        }
                        default:
                            Toast.makeText(getApplicationContext(),"Erreur de connexion", Toast.LENGTH_LONG).show();break;
                    }
                }
            }catch (JSONException ex)
            {
                Toast.makeText(getApplicationContext(),ex.getMessage().toString()+"\n"+result, Toast.LENGTH_LONG).show();
            }

        }

    }
}
