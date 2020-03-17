package com.example.hp.tmastering.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HP on 06/04/2018.
 */

public class module implements Serializable {

    private int id;
    private String nom;
    private String dateDeb;
    private String dateFin;
    private projet projet;
    private ArrayList<tache> listTache = new ArrayList<tache>();



    public module(String titre) {
        this.nom = titre;
    }

    public module(int id, String nom, String dateDeb, String dateFin, com.example.hp.tmastering.beans.projet projet) {
        this.id = id;
        this.nom = nom;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.projet = projet;
    }

    public module( String nom, String dateDeb, String dateFin, com.example.hp.tmastering.beans.projet projet) {
        this.nom = nom;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.projet = projet;
    }

    public module(String dateDeb, String dateFin) {
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public module(int id, String nom, projet projet) {
        this.id = id;
        this.nom = nom;
        this.projet = projet;
    }

    public module(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateDeb() {
        return dateDeb;
    }

    public void setDateDeb(String dateDeb) {
        this.dateDeb = dateDeb;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public com.example.hp.tmastering.beans.projet getProjet() {
        return projet;
    }

    public void setProjet(com.example.hp.tmastering.beans.projet projet) {
        this.projet = projet;
    }

    public ArrayList<tache> getListTache() {
        return listTache;
    }

    public void setListTache(ArrayList<tache> listTache) {
        this.listTache = listTache;
    }
}
