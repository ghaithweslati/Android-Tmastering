package com.example.hp.tmastering.beans;

/**
 * Created by HP on 05/02/2018.
 */

public class operateur extends user  {


    public operateur(int id) {
        super(id);
    }

    public operateur(int id, String nom, String prenom) {
        super(id,nom,prenom);
    }

    public operateur(int id, String nom, String prenom, String photodeprofil) {
        super(id,nom,prenom,photodeprofil);
    }


    public operateur(String photodeprofil) {
        super(photodeprofil);
    }


    public operateur(int id, String login, String password, String nom, String prenom, String tel, String adresse, String email, String photoProfil, boolean etat, String role) {
        super(id, login, password, nom, prenom, tel, adresse, email, photoProfil, etat, role);
    }
}
