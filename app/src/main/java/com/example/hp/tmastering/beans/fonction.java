package com.example.hp.tmastering.beans;

import java.util.List;

/**
 * Created by HP on 05/02/2018.
 */

public class fonction {

    private int id;
    private String nom;

    public fonction() {

    }

    public fonction(int id) {
        this.id = id;
    }


    public fonction(int id, String nom) {
        this.id = id;
        this.nom=nom;
    }

    public fonction(String nom) {
        this.nom = nom;
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

}
