package com.example.hp.tmastering.beans;

/**
 * Created by HP on 13/02/2018.
 */

public class commentaire {

    private int id;
    private String text;
    private String date;
    private ticket ticket;
    private com.example.hp.tmastering.beans.client client;

    public commentaire(String text, com.example.hp.tmastering.beans.ticket ticket, com.example.hp.tmastering.beans.client client) {
        this.text = text;
        this.ticket = ticket;
        this.client = client;
    }

    public commentaire(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public com.example.hp.tmastering.beans.ticket getTicket() {
        return ticket;
    }

    public void setTicket(com.example.hp.tmastering.beans.ticket ticket) {
        this.ticket = ticket;
    }

    public com.example.hp.tmastering.beans.client getClient() {
        return client;
    }

    public void setClient(com.example.hp.tmastering.beans.client client) {
        this.client = client;
    }
}
