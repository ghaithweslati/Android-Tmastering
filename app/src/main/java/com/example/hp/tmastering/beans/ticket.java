package com.example.hp.tmastering.beans;

/**
 * Created by HP on 05/02/2018.
 */

public class ticket {

    private int id;
    private String titre;
    private String description;
    private String date;
    private String image;
    private boolean urgence;
    private projet projet;

    public ticket(int id) {
        this.id = id;
    }

    public ticket(int id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public ticket(int id, String titre, String date, String description, String image, boolean urgence) {
        this.id = id;
        this.titre = titre;
        this.date = date;
        this.description = description;
        this.image = image;
        this.urgence = urgence;
    }




    public ticket(int id, client cl, com.example.hp.tmastering.beans.projet projet, String titre, String description, String date, String image, boolean urgence) {
        this.id=id;

        this.projet=projet;
        this.titre = titre;
        this.description = description;
        this.date=date;
        this.image=image;
        this.urgence=urgence;
    }


    public ticket(String titre, String description, boolean urgence) {
        this.titre = titre;
        this.description = description;
        this.urgence=urgence;
    }

    public ticket(int id, com.example.hp.tmastering.beans.projet projet, String titre, String description, boolean urgence) {
        this.id=id;
        this.projet=projet;
        this.titre = titre;
        this.description = description;
        this.urgence=urgence;
    }

    public ticket() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isUrgence() {
        return urgence;
    }

    public void setUrgence(boolean urgence) {
        this.urgence = urgence;
    }

    public projet getProjet() {
        return projet;
    }

    public void setProjet(projet projet) {
        this.projet = projet;
    }
}

