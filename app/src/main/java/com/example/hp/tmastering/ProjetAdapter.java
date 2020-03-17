package com.example.hp.tmastering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hp.tmastering.beans.projet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 06/02/2018.
 */

public class ProjetAdapter extends ArrayAdapter {
    List list = new ArrayList();

    private boolean enCours=false;

    public ProjetAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(projet object) {
        super.add(object);
        list.add(object);
    }

    public void setEnCours()
    {
        this.enCours=true;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        ProjetHolder projetHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.projet_row_layout, parent, false);
            projetHolder = new ProjetHolder();
            projetHolder.descProjTxt = (TextView) row.findViewById(R.id.projDescTxt);
            projetHolder.titreProjTxt =(TextView) row.findViewById(R.id.projTitreTxt);
            projetHolder.imgProj =(ImageView) row.findViewById(R.id.imageProj);
            projetHolder.frameLayout = (FrameLayout) row.findViewById(R.id.frame);
            projetHolder.progressBar =(ProgressBar) row.findViewById(R.id.progProj);
            projetHolder.percent =(TextView) row.findViewById(R.id.perProj);
            row.setTag(projetHolder);
        } else {
            projetHolder = (ProjetHolder) row.getTag();
        }
        projet proj = (projet) this.getItem(position);
        projetHolder.titreProjTxt.setText(proj.getNom());
        projetHolder.descProjTxt.setText(proj.getDescription());
        if(enCours)
        {
            projetHolder.imgProj.setVisibility(View.GONE);
            projetHolder.frameLayout.setVisibility(View.VISIBLE);
            projetHolder.progressBar.setProgress(proj.getAvancement());
            projetHolder.percent.setText(String.valueOf(proj.getAvancement()));
        }

        return row;
    }
    static class ProjetHolder {
        TextView titreProjTxt;
        TextView descProjTxt;
        ImageView imgProj;
        FrameLayout frameLayout;
        ProgressBar progressBar;
        TextView percent;
    }
}