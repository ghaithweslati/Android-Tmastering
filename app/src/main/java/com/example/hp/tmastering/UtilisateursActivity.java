package com.example.hp.tmastering;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hp.tmastering.beans.DatabaseHandler;

public class UtilisateursActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private LinearLayout timesheetLayout;
    private LinearLayout userLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilisateurs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        timesheetLayout = (LinearLayout) findViewById(R.id.timesheetLayout);
        userLayout = (LinearLayout) findViewById(R.id.userLayout);

        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Admin"))
            userLayout.setVisibility(View.VISIBLE);
        if(new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Chef de projet")||new DatabaseHandler(getApplicationContext()).getRoleUser().equals("Operateur"))
            timesheetLayout.setVisibility(View.VISIBLE);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AjouterUserActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        ImageView projetPage = (ImageView) findViewById(R.id.projetPage);
        projetPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ProjetsActivity.class));
            }
        });

        timesheetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TimeSheetActivity.class));
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



        userPage.setImageResource(R.drawable.usermenu);
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
            UtilisateurFragment uf = new UtilisateurFragment();

            switch (position)
            {
                case 1:uf.setRole("Client");break;
                case 2:uf.setRole("Operateur");break;
                case 3:uf.setRole("Chef de projet");break;
            }
            return uf;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tous";
                case 1:
                    return "Clients";
                case 2:
                    return "Operateurs";
                case 3:
                    return "Chefs de projet";
            }
            return null;
        }
    }
}
