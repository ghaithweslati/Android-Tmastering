package com.example.hp.tmastering.beans;

/**
 * Created by HP on 05/02/2018.
 */

public class client extends user  {

    private String raisonSocial;

    public client(int id) {
        super(id);
    }



    public client(int id, String nom, String prenom, String raisonSocial) {
        super(id, nom, prenom);
        this.raisonSocial=raisonSocial;
    }

    public client(int id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    public client(int id, String login, String password, String nom, String prenom, String tel, String adresse, String email, String photoProfil, boolean etat, String role, String raisonSocial) {
        super(id, login, password, nom, prenom, tel, adresse, email, photoProfil, etat, role);
        this.raisonSocial = raisonSocial;
    }

    public client(String nom, String prenom) {
        super(nom, prenom);
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    @Override
    public String toString() {
        return super.getNom()+" "+super.getPrenom()+" - "+this.raisonSocial;
    }
}
