package com.example.hp.tmastering.beans;

/**
 * Created by HP on 26/03/2018.
 */

public class activite {

    private int id;
    private int nb_heure;
    private String date;
    private tache tache;

    public activite(int id, String date, int nb_heure, com.example.hp.tmastering.beans.tache tache) {
        this.id = id;
        this.nb_heure = nb_heure;
        this.date = date;
        this.tache = tache;
    }

    public activite(int nb_heure, com.example.hp.tmastering.beans.tache tache) {
        this.nb_heure = nb_heure;
        this.tache = tache;
    }

    public activite(com.example.hp.tmastering.beans.tache tache) {
        this.tache = tache;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNb_heure() {
        return nb_heure;
    }

    public void setNb_heure(int nb_heure) {
        this.nb_heure = nb_heure;
    }

    public com.example.hp.tmastering.beans.tache getTache() {
        return tache;
    }

    public void setTache(com.example.hp.tmastering.beans.tache tache) {
        this.tache = tache;
    }
}


