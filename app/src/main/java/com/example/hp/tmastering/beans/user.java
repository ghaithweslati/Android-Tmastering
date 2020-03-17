package com.example.hp.tmastering.beans;

import java.io.Serializable;

/**
 * Created by HP on 05/02/2018.
 */

public class user implements Serializable {

   private int id;
    private String login;
    private String password;
    private String nom;
    private String prenom;
    private String tel;
    private String adresse;
    private String email;
    private String photoProfil;
    private boolean etat;
    private String role;

    public user(int id, String role)  {
        this.id = id;
        this.role = role;
    }

    public user(int id, String login, String password, String nom, String prenom, String tel, String adresse, String email, String photoProfil, boolean etat, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.email = email;
        this.photoProfil = photoProfil;
        this.etat = etat;
        this.role = role;
    }

    public user(int id) {
        this.id = id;
    }

    public user(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public user(int id, String nom, String prenom, String photoProfil) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.photoProfil=photoProfil;
    }


    public user( String photoProfil) {
        this.photoProfil=photoProfil;
    }


    public user(int id, String login, String nom, String prenom, String password, String adresse, String email, String tel) {
        this.id = id;
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.adresse = adresse;
        this.email = email;
        this.tel = tel;
    }

    public user(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }
}
