package com.example.hp.tmastering;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.tmastering.beans.activite;
import com.example.hp.tmastering.beans.tache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP on 06/02/2018.
 */

public class MesTachesAdapter extends ArrayAdapter {
    ArrayList<tache> list = new ArrayList<tache>();

    public MesTachesAdapter(Context context, int resource) {
        super(context, resource);
    }



    public void add(tache object) {
        super.add(object);
        list.add(object);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public tache getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        final TacheHolder tacheHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.mestaches_row_layout, parent, false);
            tacheHolder = new TacheHolder();
            tacheHolder.progress = (ProgressBar) row.findViewById(R.id.progTach);
            tacheHolder.percent = (TextView) row.findViewById(R.id.percentTach);
            tacheHolder.titre = (TextView) row.findViewById(R.id.titreTask);
            tacheHolder.seekBar = (SeekBar) row.findViewById(R.id.seekbar);
            tacheHolder.editache = (ImageView) row.findViewById(R.id.editTach);
            tacheHolder.startTache = (ImageView) row.findViewById(R.id.startTach);
            tacheHolder.saveProg = (ImageView) row.findViewById(R.id.saveprog);
            tacheHolder.cancProg = (ImageView) row.findViewById(R.id.cancprog);
            tacheHolder.editLayout = (RelativeLayout) row.findViewById(R.id.editLayout);
            tacheHolder.infoLayout = (RelativeLayout) row.findViewById(R.id.infoLayout);


            row.setTag(tacheHolder);
        } else {
            tacheHolder = (TacheHolder) row.getTag();
        }

        final tache tach = this.getItem(position);
        tacheHolder.progress.setProgress(tach.getAvancement());
        tacheHolder.percent.setText(tach.getAvancement()+"%");
        tacheHolder.titre.setText(tach.getTitre());

        if(tach.getDateDebReel()!=null)
            tacheHolder.startTache.setVisibility(View.GONE);
        else
            tacheHolder.editache.setVisibility(View.GONE);

       tacheHolder.editache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacheHolder.editLayout.setVisibility(View.VISIBLE);
                tacheHolder.infoLayout.setVisibility(View.GONE);
            }
        });

        tacheHolder.startTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            new commencerTache(tach).execute();
            tacheHolder.startTache.setVisibility(View.GONE);
            tacheHolder.editache.setVisibility(View.VISIBLE);
            }
        });


        tacheHolder.saveProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tach.setAvancement(tacheHolder.progress.getProgress());
                tacheHolder.editLayout.setVisibility(View.GONE);
                tacheHolder.infoLayout.setVisibility(View.VISIBLE);
                new modifier_avancement(tach).execute();
            }
        });

        tacheHolder.cancProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacheHolder.editLayout.setVisibility(View.GONE);
                tacheHolder.infoLayout.setVisibility(View.VISIBLE);
                tacheHolder.progress.setProgress(tach.getAvancement());
                tacheHolder.seekBar.setProgress(tach.getAvancement());
                tacheHolder.percent.setText(tach.getAvancement()+"%");
            }
        });


        tacheHolder.seekBar.setProgress(tach.getAvancement());

        tacheHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tacheHolder.progress.setProgress(i);
                tacheHolder.percent.setText(i+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return row;
    }



    private class modifier_avancement extends AsyncTask<Void, Void, Void> {

        private tache tache;

        public modifier_avancement(com.example.hp.tmastering.beans.tache tache) {
            this.tache = tache;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            makeRequest2(this.tache);
        }

    }

    private void makeRequest2(final  tache tache) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, "https://tmastering.000webhostapp.com/modifier_avancement_tache.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(tache.getAvancement()==100)
                            getContext().startActivity(new Intent(getContext(),TimeSheetActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",  String.valueOf(tache.getId()));
                map.put("avancement", String.valueOf(tache.getAvancement()));
                return map;
            }
        };
        requestQueue.add(request);
    }




    private class commencerTache extends AsyncTask<Void, Void, Void> {

        private tache tache;

        public commencerTache(com.example.hp.tmastering.beans.tache tache) {
            this.tache = tache;
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

    private void makeRequest(final  tache tache) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, "https://tmastering.000webhostapp.com/commencer_tache.php?id="+tache.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
        };
        requestQueue.add(request);
    }



    static class TacheHolder {
        ProgressBar progress;
        TextView percent;
        TextView titre;
        SeekBar seekBar;
        ImageView editache,startTache,saveProg,cancProg;
        RelativeLayout editLayout,infoLayout;
    }

}