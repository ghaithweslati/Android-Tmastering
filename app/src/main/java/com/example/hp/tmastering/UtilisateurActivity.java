package com.example.hp.tmastering;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.projet;
import com.example.hp.tmastering.beans.user;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UtilisateurActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView imageView;
    private TextView profession,username,tel,email,adresse,nomPren;
    private LinearLayout timesheetLayout;
    private LinearLayout userLayout;
    private LinearLayout deconnLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateur);
        imageView = (ImageView) findViewById(R.id.imgUser);
        profession = (TextView) findViewById(R.id.roleUser);
        tel = (TextView) findViewById(R.id.telUser);
        email = (TextView) findViewById(R.id.mailUser);
        adresse = (TextView) findViewById(R.id.adrUser);
        username = (TextView) findViewById(R.id.loginUser);
        nomPren = (TextView) findViewById(R.id.nomUser);
        deconnLayout = (LinearLayout) findViewById(R.id.deconnxion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new afficherProfil().execute();

        timesheetLayout = (LinearLayout) findViewById(R.id.timesheetLayout);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);


        deconnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

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
        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet")||new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Client"))
            ticketPage.setVisibility(View.VISIBLE);

        ImageView projetPage = (ImageView) findViewById(R.id.projetPage);
        projetPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProjetsActivity.class));
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

        timesheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TimeSheetActivity.class));
            }
        });

        settingPage.setImageResource(R.drawable.settingmenu);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_utilisateur, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if(id== R.id.action_edit)
        {   Intent i = null;
            if(Integer.parseInt(getIntent().getExtras().getString("id"))==new DatabaseHandler(getApplicationContext()).getIdUser() )
                i = new Intent(getApplicationContext(), ModifierProfilActivity.class);
            else
            {
            if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Admin"))
                i = new Intent(getApplicationContext(), AjouterUserActivity.class);
                i.putExtra("id", getIntent().getExtras().getString("id"));
            }
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }








    public  class afficherProfil extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php?id="+getIntent().getStringExtra("id");
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
                        json_string=result;
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        final JSONObject jo = jsonArray.getJSONObject(0);
                        final String nom = jo.getString("nom");
                        final String prenom = jo.getString("prenom");
                        final String rol = jo.getString("role");
                        String password = jo.getString("password");
                        String login = jo.getString("login");
                        String image_profil = jo.getString("image_profil");
                        String tele = jo.getString("tel");
                        String adress = jo.getString("adresse");
                        String raison_sociale = jo.getString("raison_sociale");
                        boolean et = true;
                        String etat = jo.getString("etat");
                        if (etat.equals("0"))
                            et = false;
                        String mail = jo.getString("email");

                        Picasso.with(getApplicationContext()).load("https://tmastering.000webhostapp.com/"+image_profil)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), imageBitmap);
                                        imageDrawable.setCircular(true);
                                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                        imageView.setImageDrawable(imageDrawable);
                                    }
                                    @Override
                                    public void onError() {
                                    }
                                });

                        if(rol.equals("Client"))
                            profession.setText(rol+" - "+raison_sociale);
                        else
                            profession.setText(rol);

                        tel.setText(tele);
                        email.setText(mail);
                        adresse.setText(adress);
                        username.setText(login);
                        nomPren.setText(nom+" "+prenom);
                        UtilisateurActivity.this.setTitle(nom+" "+prenom);
                    }
                } while (result == null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
