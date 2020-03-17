package com.example.hp.tmastering;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.tmastering.beans.DatabaseHandler;

public class ProjetsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FloatingActionButton nouvProj;
    private TextView fab2;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private LinearLayout timesheetLayout;
    private LinearLayout userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        timesheetLayout = (LinearLayout) findViewById(R.id.timesheetLayout);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);

        timesheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TimeSheetActivity.class));
            }
        });

        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Admin"))
            userLayout.setVisibility(View.VISIBLE);
        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet")||new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Operateur"))
            timesheetLayout.setVisibility(View.VISIBLE);




        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        nouvProj = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (TextView) findViewById(R.id.fab2);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        nouvProj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),NouveauProjetActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        LinearLayout ticketPage = (LinearLayout) findViewById(R.id.ticketLayout);
        ticketPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TicketsActivity.class));
            }
        });

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
                Intent intent = new Intent(getApplicationContext(),DashboardAdminActivity.class);
                startActivity(intent);
            }
        });


        projetPage.setImageResource(R.drawable.projetmenu);


        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet"))
        {
            nouvProj.setVisibility(View.GONE);
            fab2.setVisibility(View.GONE);
            userPage.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_projets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            ProjetsFragment pf = new ProjetsFragment();
            switch (position) {
                case 1: {
                    pf.setUrl("afficher_projets_encours.php");
                    break;
                }
                case 2: {
                    pf.setUrl("afficher_projets_termines.php");
                    break;
                }
                default: {
                    pf.setUrl("afficher_projets_enattente.php");
                    break;
                }
            }
            return pf;
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
                    return "En Attente";
                case 1:
                    return "En cours";
                case 2:
                    return "Terminés";
            }
            return null;
        }
    }
}
