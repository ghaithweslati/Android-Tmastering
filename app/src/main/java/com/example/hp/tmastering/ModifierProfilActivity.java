package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.user;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifierProfilActivity extends AppCompatActivity {

    private EditText setNom,setPrenom,setLogin,setPass1,setPass2,setEmail,setAdresse,setTel,setRaison;
    private TextView backProf,saveProf;
    private ImageView imageProf;
    private LinearLayout raisonLayout;
    private static final int CODE_GALLERY=999;
    private user user;
    private Bitmap bitmap = null;
    private ProgressDialog pdLoading ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil);

        getSupportActionBar().hide();

        backProf = (TextView) findViewById(R.id.backProf);
        saveProf = (TextView) findViewById(R.id.saveProf);
        setNom = (EditText) findViewById(R.id.setNom);
        setPrenom = (EditText) findViewById(R.id.setPrenom);
        setLogin = (EditText) findViewById(R.id.setLogin);
        setPass1 = (EditText) findViewById(R.id.setPassword1);
        setPass2 = (EditText) findViewById(R.id.setPassword2);
        setEmail = (EditText) findViewById(R.id.setEmail);
        setAdresse = (EditText) findViewById(R.id.setAdresse);
        setTel = (EditText) findViewById(R.id.setTel);
        setRaison = (EditText) findViewById(R.id.setRaison);
        imageProf = (ImageView) findViewById(R.id.imageProf);
        raisonLayout = (LinearLayout) findViewById(R.id.raisonLayout);

        new afficherInformations().execute();

        backProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        saveProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = new DatabaseHandler(getApplicationContext()).getIdUser();
                String login = setLogin.getText().toString();
                String nom = setNom.getText().toString();
                String prenom = setPrenom.getText().toString();
                String tel = setTel.getText().toString();
                String adresse = setAdresse.getText().toString();
                String email = setEmail.getText().toString();
                String password = setPass1.getText().toString();
                user = new user(id,login,nom,prenom,password,adresse,email,tel);
                if(verif()) {
                    new modifier_profil().execute();
                }
            }
        });

        imageProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/");
                startActivityForResult(Intent.createChooser(i, "Choisir la capture"), CODE_GALLERY);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==CODE_GALLERY&&data!=null) {
            try {
                Uri filePath = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageProf.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private class afficherInformations extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php?id="+new DatabaseHandler(getApplicationContext()).getIdUser();
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
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            String nom = jo.getString("nom");
                            String prenom = jo.getString("prenom");
                            String login = jo.getString("login");
                            String tel = jo.getString("tel");
                            String adresse = jo.getString("adresse");
                            String email = jo.getString("email");
                            String imageProfil = jo.getString("image_profil");
                            String raisonSociale = jo.getString("raison_sociale");

                            setNom.setText(nom);
                            setPrenom.setText(prenom);
                            setLogin.setText(login);

                            setAdresse.setText(adresse);
                            setTel.setText(tel);
                            setEmail.setText(email);

                            if(raisonSociale.equals("null"))
                                raisonLayout.setVisibility(View.GONE);
                            else
                                setRaison.setText(raisonSociale);

                            Picasso.with(getApplicationContext()).load("https://tmastering.000webhostapp.com/"+imageProfil).into(imageProf);


                        }
                    }
                }while (result==null);
            }

            catch (JSONException e1) {
                e1.printStackTrace();
            } while (result == null);
        }
    }



    private class modifier_profil extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(ModifierProfilActivity.this);
            pdLoading.setMessage("\nModification du profil...");
            pdLoading.show();

        }

    }

    private void makeRequest2() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_profil.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pdLoading.dismiss();
                        Intent i = new Intent(getApplicationContext(),UtilisateurActivity.class);
                        i.putExtra("id",String.valueOf(new DatabaseHandler(getApplicationContext()).getIdUser()));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"Modification terminé",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdLoading.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",  String.valueOf(user.getId()));
                map.put("nom", user.getNom());
                map.put("prenom", user.getPrenom());
                map.put("login", user.getLogin());
                map.put("password", user.getPassword());
                map.put("tel", user.getTel());
                map.put("adresse", user.getAdresse());
                map.put("email", user.getEmail());
                if(bitmap!=null)
                    map.put("image_profil", imageToString(bitmap));
                if(raisonLayout.getVisibility()== View.VISIBLE)
                    map.put("raison_social", setRaison.getText().toString());
                return map;
            }
        };
        requestQueue.add(request);
    }



    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodedImage;
    }

    public boolean verif()
    {
        boolean res=true;
        if(setNom.getText().toString().length()==0||setPrenom.getText().toString().length()==0||setAdresse.getText().toString().length()==0||setTel.getText().toString().length()==0)
        {
            Toast.makeText(getApplicationContext(),"Tous les champs doivent être remplis",Toast.LENGTH_LONG).show();
            res = false;
        }


        if(setPass1.getText().toString().equals(setPass2.getText().toString())==false)
        {
            Toast.makeText(getApplicationContext(),"les deux mots de passe doivent être identique",Toast.LENGTH_LONG).show();
            setPass1.setTextColor(getResources().getColor(R.color.red));
            setPass2.setTextColor(getResources().getColor(R.color.red));
            res = false;
        }
        if(setPass1.getText().toString().length()<6)
        {
            Toast.makeText(getApplicationContext(),"la taille du mot de passe doit être suppérieur à 6",Toast.LENGTH_LONG).show();
            setPass1.setTextColor(getResources().getColor(R.color.red));
            res = false;
        }
        if(setPass2.getText().toString().length()<6)
        {
            Toast.makeText(getApplicationContext(),"la taille du mot de passe doit être suppérieur à 6",Toast.LENGTH_LONG).show();
            setPass2.setTextColor(getResources().getColor(R.color.red));
            res = false;
        }


        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = setEmail.getText().toString();

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches()==false)
        {
            Toast.makeText(getApplicationContext(),"Adresse invalide",Toast.LENGTH_LONG).show();
            setAdresse.setTextColor(getResources().getColor(R.color.red));
        }
        return res;
    }

}


