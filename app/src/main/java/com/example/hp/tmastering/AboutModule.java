package com.example.hp.tmastering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by HP on 05/03/2018.
 */

public class AboutModule extends Fragment {

    TextView titTxt,dateDebTxt,dateFinTxt,description;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_module,container,false);


        titTxt = (TextView) rootView.findViewById(R.id.titreMiss);
        dateDebTxt = (TextView) rootView.findViewById(R.id.dateDebMiss);
        dateFinTxt = (TextView) rootView.findViewById(R.id.dateFinMiss);
        description = (TextView) rootView.findViewById(R.id.perMiss);

        bundle=getActivity().getIntent().getExtras();

        getActivity().setTitle(bundle.getString("titre"));

        titTxt.setText(bundle.getString("titre"));
        dateDebTxt.setText(bundle.getString("dateDeb"));
        description.setText(bundle.getString("periorite"));
        dateFinTxt.setText(bundle.getString("dateFin"));

        return rootView;
    }
}
