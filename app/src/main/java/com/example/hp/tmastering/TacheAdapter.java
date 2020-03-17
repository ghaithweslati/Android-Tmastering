package com.example.hp.tmastering;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.operateur;
import com.example.hp.tmastering.beans.tache;
import com.example.hp.tmastering.beans.tachePlanifiee;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 06/02/2018.
 */

public class TacheAdapter extends ArrayAdapter {
    ArrayList<tache> list = new ArrayList<tache>();
    private DatePickerDialog datePickerDialog;
    private List<operateur> lstOper = new ArrayList<operateur>();
    private List<String> lstNomOper = new ArrayList<String>();
    private ProgressDialog progressDialogOper;
    private Spinner spinOp;
    private TextView nomTach;
    private TextView dateDebTach;
    private TextView dateFinTach;

    public TacheAdapter(Context context, int resource) {
        super(context, resource);
    }



    public void add(tache object) {
        super.add(object);
        list.add(object);
    }

    public void del(tache object) {
        super.remove(object);
        list.remove(object);
    }





    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public tache getItem(int position) {
        return (tache) list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final TacheHolder tacheHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.tache_row, parent, false);
            tacheHolder = new TacheHolder();
            tacheHolder.titreTache = (TextView) row.findViewById(R.id.titTach);
            tacheHolder.imageTach = (ImageView) row.findViewById(R.id.imgTach);
            tacheHolder.settMission = (ImageView) row.findViewById(R.id.settTach);


            tacheHolder.settMission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(getContext(), tacheHolder.settMission);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_setting, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().toString().equals("Supprimer")) {
                                new supprimer_tache(getItem(position)).execute();
                                del(getItem(position));
                            }
                            else
                            {
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                View mView = inflater.inflate(R.layout.tache_layout,null);

                                nomTach  = (TextView) mView.findViewById(R.id.nomTach);
                                dateDebTach = (TextView) mView.findViewById(R.id.dateDebTach);
                                dateFinTach = (TextView) mView.findViewById(R.id.dateFinTach);
                                spinOp = (Spinner) mView.findViewById(R.id.spinOp);

                                new afficher_operateurs(getItem(position).getOperateur()).execute();

                                nomTach.setText(getItem(position).getTitre());
                                dateDebTach.setText(getItem(position).getDateDeb());
                                dateFinTach.setText(getItem(position).getDateFin());


                                dateDebTach.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Date dateDeb = new Date();
                                        Date dateFin = null;

                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            if(getItem(position) instanceof tachePlanifiee) {
                                                dateDeb = format.parse(((tachePlanifiee)getItem(position)).getModule().getDateDeb());
                                                dateFin = format.parse(((tachePlanifiee)getItem(position)).getModule().getDateFin());
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Calendar calendar = Calendar.getInstance();
                                        int _year = calendar.get(Calendar.YEAR);
                                        int _month = dateDeb.getMonth();
                                        int _day = dateFin.getDay();


                                        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                dateDebTach.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                                            }
                                        },_year,_month,_day);
                                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                        datePickerDialog.getDatePicker().setMinDate(dateDeb.getTime());
                                        if(getItem(position) instanceof tachePlanifiee)
                                            datePickerDialog.getDatePicker().setMaxDate(dateFin.getTime());
                                        datePickerDialog.show();
                                    }
                                });


                                dateFinTach.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Date dateDeb = new Date() ;
                                        Date dateFin = null;
                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            if(getItem(position) instanceof tachePlanifiee) {
                                                dateDeb = format.parse(((tachePlanifiee)getItem(position)).getModule().getDateDeb());
                                                dateFin = format.parse(((tachePlanifiee)getItem(position)).getModule().getDateFin());
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Calendar calendar = Calendar.getInstance();
                                        int _year = calendar.get(Calendar.YEAR);
                                        int _month = dateDeb.getMonth();
                                        int _day = dateFin.getDay();

                                        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                dateFinTach.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                                            }
                                        },_year,_month,_day);
                                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                        datePickerDialog.getDatePicker().setMinDate(dateDeb.getTime());
                                        if(getItem(position) instanceof tachePlanifiee)
                                            datePickerDialog.getDatePicker().setMaxDate(dateFin.getTime());
                                        datePickerDialog.show();
                                    }
                                });


                                TextView fermer = (TextView) mView.findViewById(R.id.ferDlg);
                                final TextView save = (TextView) mView.findViewById(R.id.saveDlg);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                dialog.show();

                                fermer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getItem(position).setTitre(nomTach.getText().toString());
                                        getItem(position).setDateDeb(dateDebTach.getText().toString());
                                        getItem(position).setDateFin(dateFinTach.getText().toString());

                                        tache tache = new tache(nomTach.getText().toString(),dateDebTach.getText().toString(),dateFinTach.getText().toString());
                                        operateur operateur = lstOper.get(spinOp.getSelectedItemPosition());
                                        tache.setOperateur(operateur);
                                        tache.setId(getItem(position).getId());
                                        tacheHolder.titreTache.setText(nomTach.getText().toString());
                                        Picasso.with(getContext()).load("https://tmastering.000webhostapp.com/"+tache.getOperateur().getPhotoProfil())
                                                .into(tacheHolder.imageTach, new Callback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        Bitmap imageBitmap = ((BitmapDrawable) tacheHolder.imageTach.getDrawable()).getBitmap();
                                                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), imageBitmap);
                                                        imageDrawable.setCircular(true);
                                                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                                        tacheHolder.imageTach.setImageDrawable(imageDrawable);
                                                    }
                                                    @Override
                                                    public void onError() {
                                                    }
                                                });
                                        if(verif()) {
                                            new modifier_tache(tache).execute();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }

                            return true;
                        }
                    });
                    popup.show();
                }
            });



            row.setTag(tacheHolder);
        } else {
            tacheHolder = (TacheHolder) row.getTag();
        }

        tache tache = (tache) this.getItem(position);
        tacheHolder.titreTache.setText(tache.getTitre());

        Picasso.with(getContext()).load("https://tmastering.000webhostapp.com/"+tache.getOperateur().getPhotoProfil())
                .into(tacheHolder.imageTach, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) tacheHolder.imageTach.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        tacheHolder.imageTach.setImageDrawable(imageDrawable);
                    }
                    @Override
                    public void onError() {
                    }
                });

// Create an ArrayAdapter using the string array and a default spinner layout

        return row;
    }


    static class TacheHolder {
        TextView titreTache;
        ImageView imageTach;
        ImageView settMission;
    }

    private class afficher_operateurs extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;
        operateur oper;

        public afficher_operateurs(operateur operateur) {
            this.oper = operateur;
        }

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/afficher_profils.php?role=Operateur";
            progressDialogOper = new ProgressDialog(getContext());
            progressDialogOper.setMessage("\tChargement des operateurs..");
            progressDialogOper.setCancelable(false);
            progressDialogOper.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            progressDialogOper.show();
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
                        progressDialogOper.dismiss();
                        json_string = result;
                        spinOp.setAdapter(null);
                        lstNomOper.clear();
                        lstOper.clear();
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("user");
                        int index =0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            int id = jo.getInt("id");
                            String nom = jo.getString("nom");
                            String prenom = jo.getString("prenom");
                            String image_profil = jo.getString("image_profil");
                            operateur operateur = new operateur(id,nom,prenom,image_profil);
                            lstOper.add(operateur);
                            lstNomOper.add(operateur.getNom()+" "+operateur.getPrenom());
                            if(id==oper.getId())
                                index = i;
                        }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_style, lstNomOper);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinOp.setAdapter(dataAdapter);
                    spinOp.setSelection(index);
                    }
                }while (result==null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    private class supprimer_tache extends AsyncTask<Void, Void, Void> {

        private tache tache;

        public supprimer_tache(tache tache) {
            this.tache=tache;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2(tache);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }


    private void makeRequest2(final tache tache) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/supprimer_tache.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Tache supprimé",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",String.valueOf(tache.getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }



    private class modifier_tache extends AsyncTask<Void, Void, Void> {

        private tache tache;

        public modifier_tache(tache tache) {
            this.tache=tache;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest(tache);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }


    private void makeRequest(final tache tache) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_tache.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Tache modifié",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",String.valueOf(tache.getId()));
                map.put("titre",tache.getTitre());
                map.put("date_deb_plan",tache.getDateDeb());
                map.put("date_fin_plan",tache.getDateFin());
                map.put("id_operateur",String.valueOf(tache.getOperateur().getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }





    public boolean verif()
    {
        boolean res=true;
        if(dateDebTach.getText().length()==0)
        {
            Toast.makeText(getContext(),"Choisis la date début du tache",Toast.LENGTH_LONG).show();
            res=false;
        }

        if(dateFinTach.getText().length()==0)
        {
            Toast.makeText(getContext(),"Choisis la date fin du tache",Toast.LENGTH_LONG).show();
            res=false;
        }
        else
        {
            Date dateDeb = null;
            Date dateFin = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateDeb = format.parse(dateDebTach.getText().toString());
                dateFin = format.parse(dateFinTach.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dateFin.before(dateDeb))
            {
                Toast.makeText(getContext(),"la date fin doit être aprés la date du début",Toast.LENGTH_LONG).show();
                res = false;
            }
        }

        if(nomTach.getText().length()==0)
        {
            Toast.makeText(getContext(),"Saisi le nom du tache",Toast.LENGTH_LONG).show();
            res=false;
        }

        return res;
    }

}