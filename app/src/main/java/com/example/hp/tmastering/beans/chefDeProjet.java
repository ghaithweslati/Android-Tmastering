package com.example.hp.tmastering.beans;

/**
 * Created by HP on 05/02/2018.
 */

public class chefDeProjet extends user {


    public chefDeProjet(int id) {
        super(id);
    }

    public chefDeProjet(int id, String nom, String prenom) {
        super(id,nom,prenom);
    }

    public chefDeProjet(int id, String login, String password, String nom, String prenom, String tel, String adresse, String email, String photoProfil, boolean etat, String role) {
        super(id, login, password, nom, prenom, tel, adresse, email, photoProfil, etat, role);
    }
}
