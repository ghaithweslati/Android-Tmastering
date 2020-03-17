package com.example.hp.tmastering.beans;

import java.util.List;

/**
 * Created by HP on 14/02/2018.
 */

public class projet {

    private int id;
    private String nom;
    private String dateDeb;
    private String dateFin;
    private String description;
    private client client;
    private chefDeProjet chefDeProjet;
    private int avancement;

    public projet() {
    }

    public projet(String dateDeb, String dateFin) {
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public projet(int id, String dateDeb, String dateFin) {
        this.id = id;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public projet(String dateDeb, String nom, String dateFin, String description, com.example.hp.tmastering.beans.client client, com.example.hp.tmastering.beans.chefDeProjet chefDeProjet) {
        this.dateDeb = dateDeb;
        this.nom = nom;
        this.dateFin = dateFin;
        this.description = description;
        this.client = client;
        this.chefDeProjet = chefDeProjet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public com.example.hp.tmastering.beans.chefDeProjet getChefDeProjet() {
        return chefDeProjet;
    }

    public void setChefDeProjet(com.example.hp.tmastering.beans.chefDeProjet chefDeProjet) {
        this.chefDeProjet = chefDeProjet;
    }

    public projet(int id) {
        this.id = id;
    }

    public projet(int id, String nom, String dateDeb, String dateFin, String description) {
        this.id = id;
        this.nom = nom;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.description=description;
    }

    public projet(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public projet(String nom) {
        this.nom = nom;
    }

    public projet(String nom, String dateDeb, String dateFin) {
        this.nom = nom;
        this.dateDeb=dateDeb;
        this.dateFin=dateFin;
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


    public int rechProj(String nom, List<projet> lstProj)
    {
        int i=0;
        while(i<lstProj.size() && lstProj.get(i).getNom().equals(nom)==false)
            i++;
        return lstProj.get(i).getId();
    }


    public int getAvancement() {
        return avancement;
    }

    public void setAvancement(int avancement) {
        this.avancement = avancement;
    }

    public com.example.hp.tmastering.beans.client getClient() {
        return client;
    }

    public void setClient(com.example.hp.tmastering.beans.client client) {
        this.client = client;
    }
}
