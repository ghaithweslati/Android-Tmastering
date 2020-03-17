package com.example.hp.tmastering;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by HP on 05/03/2018.
 */

public class CaptureTicket extends Fragment {

    Bundle bundle;
    ImageView capImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.capture_ticket,container,false);
        capImg = (ImageView) rootView.findViewById(R.id.imgInfoTick);
        bundle=getActivity().getIntent().getExtras();
        if(bundle.getString("image").length()>0) {
            Picasso.with(rootView.getContext()).load("https://tmastering.000webhostapp.com/" + bundle.getString("image")).into(capImg);
        }
        return rootView;
    }
}
