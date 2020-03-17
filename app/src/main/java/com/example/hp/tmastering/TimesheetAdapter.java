package com.example.hp.tmastering;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.tmastering.beans.ticket;
import com.example.hp.tmastering.beans.timesheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 06/02/2018.
 */

public class TimesheetAdapter extends ArrayAdapter {
    List list = new ArrayList();

    public TimesheetAdapter(Context context, int resource) {
        super(context, resource);
    }


    public void add(timesheet object) {
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
    public timesheet getItem(int position) {
        return (timesheet) list.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row = convertView;
        TimeSheetHolder timeSheetHolder;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.timesheet_row_layout, parent, false);
            timeSheetHolder = new TimeSheetHolder();
            timeSheetHolder.moisTime=(TextView) row.findViewById(R.id.moisTime);
            timeSheetHolder.joursTime =(TextView) row.findViewById(R.id.joursTime);
            timeSheetHolder.actTime=(TextView) row.findViewById(R.id.nomTachTime);
            row.setTag(timeSheetHolder);
        } else {
            timeSheetHolder = (TimeSheetHolder) row.getTag();
        }
        final timesheet time = (timesheet) this.getItem(position);
        String[] exploded=time.getDate().toString().split(" ");

        timeSheetHolder.moisTime.setText(exploded[0]);
        timeSheetHolder.joursTime.setText(exploded[2]);
        if(time.getActivite()!=null)
            timeSheetHolder.actTime.setText(time.getActivite().getTache().getTitre()+" ("+time.getActivite().getNb_heure()+"h)");
        else
            timeSheetHolder.actTime.setText("");


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


        if(time.getDate().before(new Date()))
        {
            timeSheetHolder.moisTime.setTextColor(Color.GRAY);
            timeSheetHolder.joursTime.setTextColor(Color.GRAY);
            timeSheetHolder.actTime.setBackgroundColor(Color.rgb(0, 127, 255));
        }
        else
        {
            timeSheetHolder.moisTime.setTextColor(Color.BLACK);
            timeSheetHolder.joursTime.setTextColor(Color.BLACK);
            timeSheetHolder.actTime.setBackgroundColor(Color.WHITE);
        }
        if(format.format(time.getDate()).equals(format.format(new Date()))) {
            timeSheetHolder.moisTime.setTextColor(Color.BLUE);
            timeSheetHolder.joursTime.setTextColor(Color.BLUE);
        }


        return row;
    }
    static class TimeSheetHolder {
        TextView moisTime;
        TextView joursTime;
        TextView actTime;
    }



}