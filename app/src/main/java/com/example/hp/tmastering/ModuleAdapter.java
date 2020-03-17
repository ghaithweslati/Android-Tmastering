package com.example.hp.tmastering;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.module;

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

public class ModuleAdapter extends ArrayAdapter {
    List list = new ArrayList();
    private DatePickerDialog datePickerDialog;
    private TextView nomMod;
    private TextView dateDebMod;
    private TextView dateFinMod;
    private boolean editable=true;


    public ModuleAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(module object) {
        super.add(object);
        list.add(object);
    }

    public void del(Object object) {
        super.remove(object);
        list.remove(object);
    }

    public void setEditable() {
        this.editable = false;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public module getItem(int position) {
        return (module) list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final  ModuleHolder moduleHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.module_row, parent, false);
            moduleHolder = new ModuleHolder();
            moduleHolder.nomMod=(TextView) row.findViewById(R.id.nomModule);
            moduleHolder.setMod = (ImageView) row.findViewById(R.id.settingModule);

            moduleHolder.setMod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(getContext(),  moduleHolder.setMod);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_setting, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().toString().equals("Supprimer")) {
                                new supprimer_module(getItem(position)).execute();
                                del((getItem(position)));
                            }
                            else
                            {

                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                                View mView = inflater.inflate(R.layout.module_layout,null);

                                nomMod = (TextView) mView.findViewById(R.id.nomMod);
                                dateDebMod = (TextView) mView.findViewById(R.id.dateDebMod);
                                dateFinMod = (TextView) mView.findViewById(R.id.dateFinMod);
                                TextView fermer = (TextView) mView.findViewById(R.id.ferDlg);
                                final TextView save = (TextView) mView.findViewById(R.id.saveDlg);

                                mBuilder.setView(mView);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                dialog.show();


                                if(getItem(position)!=null)
                                {
                                    nomMod.setText(getItem(position).getNom());
                                    dateDebMod.setText(getItem(position).getDateDeb());
                                    dateFinMod.setText(getItem(position).getDateFin());
                                }

                                fermer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

                                dateDebMod.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Date dateDeb = null;
                                        Date dateFin = null;

                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            dateDeb = format.parse(getItem(position).getProjet().getDateDeb());
                                            dateFin = format.parse(getItem(position).getProjet().getDateFin());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Calendar calendar = Calendar.getInstance();
                                        int _year = calendar.get(Calendar.YEAR);
                                        int _month = dateDeb.getMonth();
                                        int _day = dateDeb.getDay();

                                        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                dateDebMod.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                                            }
                                        },_year,_month,_day);
                                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                        datePickerDialog.getDatePicker().setMinDate(dateDeb.getTime());
                                        datePickerDialog.getDatePicker().setMaxDate(dateFin.getTime());
                                        datePickerDialog.show();


                                    }
                                });


                                dateFinMod.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Date dateDeb = null;
                                        Date dateFin = null;
                                        try {
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                            dateDeb = format.parse(getItem(position).getProjet().getDateDeb());
                                            dateFin = format.parse(getItem(position).getProjet().getDateFin());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        Calendar calendar = Calendar.getInstance();
                                        int _year = calendar.get(Calendar.YEAR);
                                        int _month = dateDeb.getMonth();
                                        int _day = dateDeb.getDay();

                                        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                dateFinMod.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                                            }
                                        },_year,_month,_day);
                                        datePickerDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                        datePickerDialog.getDatePicker().setMinDate(dateDeb.getTime());
                                        datePickerDialog.getDatePicker().setMaxDate(dateFin.getTime());
                                        datePickerDialog.show();
                                    }
                                });


                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getItem(position).setNom(nomMod.getText().toString());
                                        getItem(position).setDateDeb(dateDebMod.getText().toString());
                                        getItem(position).setDateFin(dateFinMod.getText().toString());
                                        moduleHolder.nomMod.setText(nomMod.getText().toString());

                                        if(verif()) {
                                            new modifier_module(getItem(position)).execute();
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


            row.setTag(moduleHolder);
        } else {
            moduleHolder = (ModuleHolder) row.getTag();
        }

        module module =  this.getItem(position);
        moduleHolder.nomMod.setText(module.getNom());
        if(editable==false)
            moduleHolder.setMod.setVisibility(View.GONE);

// Create an ArrayAdapter using the string array and a default spinner layout

        return row;
    }


    static class ModuleHolder {
        TextView nomMod;
        ImageView setMod;
    }


    private class supprimer_module extends AsyncTask<Void, Void, Void> {

        private module module;

        public supprimer_module(module module) {
            this.module = module;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2(module);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }


    private void makeRequest2(final module module) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/supprimer_module.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Module supprimé",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_module",String.valueOf(module.getId()));
                return map;
            }
        };
        requestQueue.add(request);
    }



    private class modifier_module extends AsyncTask<Void, Void, Void> {

        private module module;

        public modifier_module(module module) {
            this.module = module;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest(module);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

    }


    private void makeRequest(final module module) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://diabmeeties.000webhostapp.com/supprimer_user.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Module modifié",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",String.valueOf(module.getId()));
                map.put("nom", module.getNom());
                map.put("date_deb", module.getDateDeb());
                map.put("date_fin", module.getDateFin());
                return map;
            }
        };
        requestQueue.add(request);
    }


    public boolean verif()
    {
        boolean res=true;
        if(dateDebMod.getText().length()==0)
        {
            Toast.makeText(getContext(),"Choisis la date début du module",Toast.LENGTH_LONG).show();
            res=false;
        }

        if(dateFinMod.getText().length()==0)
        {
            Toast.makeText(getContext(),"Choisis la date fin du module",Toast.LENGTH_LONG).show();
            res=false;
        }
        else
        {
            Date dateDeb = null;
            Date dateFin = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                dateDeb = format.parse(dateDebMod.getText().toString());
                dateFin = format.parse(dateFinMod.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dateFin.before(dateDeb))
            {
                Toast.makeText(getContext(),"la date fin doit être aprés la date du début",Toast.LENGTH_LONG).show();
                res = false;
            }
        }

        if(nomMod.getText().length()==0)
        {
            Toast.makeText(getContext(),"Saisi le nom du module",Toast.LENGTH_LONG).show();
            res=false;
        }

        return res;
    }


}