package com.example.hp.tmastering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by HP on 05/03/2018.
 */

public class AboutTicket extends Fragment {

    TextView titTxt,dateTxt,servTxt,urgTxt,descTxt,etatTxt;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_ticket,container,false);


        titTxt = (TextView) rootView.findViewById(R.id.titreInfoTxt);
        dateTxt = (TextView) rootView.findViewById(R.id.dateInfoTxt);
        servTxt = (TextView) rootView.findViewById(R.id.servInfoTxt);
        urgTxt = (TextView) rootView.findViewById(R.id.urgInfoTxt);
        descTxt = (TextView) rootView.findViewById(R.id.descInfoTxt);
        etatTxt = (TextView) rootView.findViewById(R.id.etatInfoTxt);

        bundle=getActivity().getIntent().getExtras();

        getActivity().setTitle(bundle.getString("titre"));

        titTxt.setText(bundle.getString("titre"));
        dateTxt.setText(bundle.getString("date"));
        servTxt.setText("Projet "+bundle.getString("projet"));
        urgTxt.setText(bundle.getString("urgence"));
        descTxt.setText(bundle.getString("description"));
        etatTxt.setText(bundle.getString("etat"));

        servTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ProjetActivity.class);
                i.putExtra("id",bundle.getString("idProj"));
                i.putExtra("etat","Terminé");
                getActivity().startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        return rootView;
    }
}
