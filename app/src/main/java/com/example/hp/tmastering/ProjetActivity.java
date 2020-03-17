package com.example.hp.tmastering;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hp.tmastering.beans.DatabaseHandler;
import com.example.hp.tmastering.beans.projet;

public class ProjetActivity extends AppCompatActivity {

    Bundle bundle;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private projet proj ;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        bundle = getIntent().getExtras();
        proj = new projet(Integer.parseInt(bundle.getString("id")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_projet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id== R.id.action_edit)
        {
            if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Admin")) {
                if(bundle.getString("etat").contains("En cours"))
                    Toast.makeText(getApplicationContext(),"Vous ne pouvez pas modifier un projet en cours",Toast.LENGTH_LONG).show();
                else if(bundle.getString("etat").contains("En attente")==false)
                    Toast.makeText(getApplicationContext(),"Vous ne pouvez pas modifier un projet terminé",Toast.LENGTH_LONG).show();
                else {
                    Intent i = new Intent(getApplicationContext(), NouveauProjetActivity.class);
                    i.putExtra("id", bundle.getString("id"));
                    i.putExtra("etat", bundle.getString("etat"));
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
            else if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet"))
            {
                Intent i = new Intent(getApplicationContext(), ModulesActivity.class);
                i.putExtra("id", bundle.getString("id"));
                i.putExtra("date_deb", bundle.getString("date_deb"));
                i.putExtra("date_fin", bundle.getString("date_fin"));
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        }
        else
        {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 1: return new ModuleFragment();
                case 2: return new DocumentProjet();
                default: return new AboutProjet();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "A PROPOS";
                case 1:
                    return "PLANIFICATION";
                case 2:
                    return "DOCUMENTS";
            }
            return null;
        }
    }

}
