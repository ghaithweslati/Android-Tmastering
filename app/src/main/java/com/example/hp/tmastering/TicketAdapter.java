package com.example.hp.tmastering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.tmastering.beans.ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 06/02/2018.
 */

public class TicketAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public TicketAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(ticket object) {
        super.add(object);
        list.add(object);
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
    public ticket getItem(int position) {
        return (ticket) list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        TicketHolder ticketHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            ticketHolder = new TicketHolder();
            ticketHolder.titreProbTxt=(TextView) row.findViewById(R.id.titreProbTxt);
            ticketHolder.imageProb=(ImageView) row.findViewById(R.id.imageProb);
            ticketHolder.descTick=(TextView) row.findViewById(R.id.descTick);
            row.setTag(ticketHolder);
        } else {
            ticketHolder = (TicketHolder) row.getTag();
        }
        ticket tic = this.getItem(position);
        ticketHolder.titreProbTxt.setText(tic.getTitre());
        ticketHolder.descTick.setText(tic.getDescription());

        return row;
    }
    static class TicketHolder {
        TextView titreProbTxt;
        ImageView imageProb;
        TextView descTick;
    }



}