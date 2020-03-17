package com.example.hp.tmastering;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.projet;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
//import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity {


    //   private float[] yData = {33f, 33f, 33f};
    private String[] xData = {"En attente", "En cours", "Terminé"};
    PieChart pieChart;
    BarChart barChart;
    LineChart lineChart;
    private LinearLayout timesheetLayout;
    private LinearLayout userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques_admin);

        timesheetLayout = (LinearLayout) findViewById(R.id.timesheetLayout);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);

        timesheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TimeSheetActivity.class));
            }
        });

        if (new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Admin"))
            userLayout.setVisibility(View.VISIBLE);
        if (new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet") || new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Operateur"))
            timesheetLayout.setVisibility(View.VISIBLE);

        this.setTitle("Tableau de bord");
        lineChart = (LineChart) findViewById(R.id.idLineChart);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);



        barChart = (BarChart) findViewById(R.id.idBarChart);
        barChart.getDescription().setEnabled(false);

        pieChart = (PieChart) findViewById(R.id.idPieChart);
        pieChart = (PieChart) findViewById(R.id.idPieChart);

        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.setHoleRadius(30f);
        pieChart.setCenterText("Les projets");

        pieChart.setTransparentCircleAlpha(0);

        pieChart.setEntryLabelTextSize(15);
        pieChart.setEntryLabelColor(Color.WHITE);

        new initPiChart().execute();
        new initBarChart().execute();
        new initLineChart().execute();


        ImageView projetPage = (ImageView) findViewById(R.id.projetPage);
        projetPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProjetsActivity.class));
            }
        });

        ImageView userPage = (ImageView) findViewById(R.id.userPage);
        userPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UtilisateursActivity.class));
            }
        });

        ImageView settingPage = (ImageView) findViewById(R.id.settingPage);
        settingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UtilisateurActivity.class);
                intent.putExtra("id", String.valueOf(new DatabaseHandler(getApplicationContext()).getIdUser()));
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
        housePage.setImageResource(R.drawable.housemenu);

        LinearLayout ticketPage = (LinearLayout) findViewById(R.id.ticketLayout);
        ticketPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TicketsActivity.class));
            }
        });
        if (new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet") || new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Client"))
            ticketPage.setVisibility(View.VISIBLE);


    }


    private class initPiChart extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/dashboard_projets.php";
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
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("projet");
                        JSONObject jo = jsonArray.getJSONObject(0);
                        int cours = jo.getInt("cours");
                        int termine = jo.getInt("termine");
                        int attente = jo.getInt("attente");
                        int all = cours + termine + attente;

                        Float[] yData = {(attente * 100f) / all, (cours * 100f) / all, (termine * 100f) / all};

                        ArrayList<PieEntry> yEntrys = new ArrayList<>();

                        for (int i = 0; i < yData.length; i++) {
                            yEntrys.add(new PieEntry(yData[i], xData[i]));
                        }

                        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");


                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.rgb(70, 130, 180));
                        colors.add(Color.rgb(30, 144, 255));
                        colors.add(Color.rgb(65, 105, 225));


                        pieDataSet.setColors(colors);

                        pieDataSet.setValueTextSize(30);
                        pieDataSet.setValueTextColor(Color.WHITE);

                        Legend legend = pieChart.getLegend();
                        legend.setForm(Legend.LegendForm.CIRCLE);
                        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.animate();
                        pieChart.invalidate();

                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }


    private class initBarChart extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/statistiques_rendment.php";
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
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("activite");
                        ArrayList<BarEntry> barEntries = new ArrayList<>();
                        String[] operateurs = new String[30];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            float nb_heure = jo.getInt("nb_heure");
                            String operateur = jo.getString("prenom");
                            barEntries.add(new BarEntry(i, nb_heure));
                            operateurs[i] = operateur;
                        }
                        operateurs[jsonArray.length()] = "";


                        BarDataSet barDataSet = new BarDataSet(barEntries, "Opérateurs");
                        barDataSet.setColor(getResources().getColor(R.color.blue_ciel));

                        BarData theData = new BarData(barDataSet);
                        barChart.setData(theData);
                        barChart.setTouchEnabled(true);
                        barChart.setDragEnabled(true);
                        barChart.setScaleEnabled(true);

                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new MyAxisValueFormatter(operateurs));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                        xAxis.setGranularity(1);
                        //   xAxis.setCenterAxisLabels(true);
                        theData.setBarWidth(0.43f);

                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }
    }


    private class initLineChart extends AsyncTask<Void, Void, String> {
        String json_string;
        String JSON_URL;

        @Override
        protected void onPreExecute() {
            JSON_URL = "https://tmastering.000webhostapp.com/statistiques_rendment.php";
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
                        JSONObject jsonObject = new JSONObject(json_string);
                        JSONArray jsonArray = jsonObject.getJSONArray("activite");
                        ArrayList<Entry> yValues = new ArrayList<>();
                        String[] operateurs = new String[30];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            float nb_heure = jo.getInt("nb_heure");
                            String operateur = jo.getString("prenom");
                            yValues.add(new Entry(i, nb_heure));
                            operateurs[i]=operateur;
                        }

                        operateurs[jsonArray.length()] = "";

                        LineDataSet set1 = new LineDataSet(yValues,"Les ticket");
                        set1.setFillAlpha(110);
                        set1.setColor(Color.BLUE);
                        set1.setLineWidth(3f);
                        set1.setValueTextSize(10f);

                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);

                        LineData data = new LineData(dataSets);

                        lineChart.setData(data);

                        XAxis xAxis =lineChart.getXAxis();
                        xAxis.setValueFormatter(new MyAxisValueFormatter(operateurs));
                        xAxis.setGranularity(1);

                    }
                } while (result == null);
            } catch (JSONException e) {

            }
        }

    }

    public class MyAxisValueFormatter implements IAxisValueFormatter
    {
        private String[] mValue;

        public MyAxisValueFormatter(String[] mValues) {
            this.mValue = mValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValue[(int)value];
        }

    }
}
