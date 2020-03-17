package com.example.hp.tmastering.beans;

/**
 * Created by HP on 22/04/2018.
 */

public class document {

    private int id;
    private String nom;
    private String path;
    private projet projet;

    public document() {
    }

    public document(String nom, String path) {
        this.nom = nom;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public com.example.hp.tmastering.beans.projet getProjet() {
        return projet;
    }

    public void setProjet(com.example.hp.tmastering.beans.projet projet) {
        this.projet = projet;
    }
}
