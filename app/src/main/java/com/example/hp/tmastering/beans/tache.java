package com.example.hp.tmastering.beans;

import java.io.Serializable;

/**
 * Created by HP on 16/02/2018.
 */

public class tache implements Serializable {

    int id;
    private String titre;
    private com.example.hp.tmastering.beans.operateur operateur;
    private String dateDeb;
    private String dateFin;
    private String dateDebReel;
    private String dateFinReel;
    private int avancement;

    public tache()
    {

    }




    public tache(String titre, String dateDeb, String dateFin) {
        this.titre = titre;
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
    }

    public tache(int id,String titre,String dateDeb,String datefin, com.example.hp.tmastering.beans.operateur operateur) {
        this.id=id;
        this.titre = titre;
        this.dateDeb=dateDeb;
        this.dateFin=datefin;
        this.operateur = operateur;
    }

    public tache(String titre,String dateDeb,String datefin, com.example.hp.tmastering.beans.operateur operateur) {
        this.titre = titre;
        this.dateDeb=dateDeb;
        this.dateFin=datefin;
        this.operateur = operateur;
    }



    public tache(String titre) {
        this.titre = titre;
    }

    public tache(int id) {
        this.id = id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public com.example.hp.tmastering.beans.operateur getOperateur() {
        return operateur;
    }

    public void setOperateur(com.example.hp.tmastering.beans.operateur operateur) {
        this.operateur = operateur;
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

    public int getAvancement() {
        return avancement;
    }

    public void setAvancement(int avancement) {
        this.avancement = avancement;
    }

    public String getDateDebReel() {
        return dateDebReel;
    }

    public void setDateDebReel(String dateDebReel) {
        this.dateDebReel = dateDebReel;
    }

    public String getDateFinReel() {
        return dateFinReel;
    }

    public void setDateFinReel(String dateFinReel) {
        this.dateFinReel = dateFinReel;
    }
}
