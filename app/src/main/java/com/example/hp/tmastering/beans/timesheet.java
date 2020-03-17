package com.example.hp.tmastering.beans;

import android.text.format.Time;

import java.util.Date;

/**
 * Created by HP on 12/05/2018.
 */

public class timesheet {

    private Date date;
    private activite activite;


    public timesheet(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public com.example.hp.tmastering.beans.activite getActivite() {
        return activite;
    }

    public void setActivite(com.example.hp.tmastering.beans.activite activite) {
        this.activite = activite;
    }
}
