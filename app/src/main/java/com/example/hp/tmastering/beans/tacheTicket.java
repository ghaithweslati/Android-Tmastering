package com.example.hp.tmastering.beans;

/**
 * Created by HP on 08/06/2018.
 */

public class tacheTicket extends tache {

    private ticket ticket;

    public tacheTicket(String titre, String dateDeb, String datefin, operateur operateur, ticket ticket) {
        super(titre, dateDeb, datefin, operateur);
        this.ticket = ticket;
    }



    public com.example.hp.tmastering.beans.ticket getTicket() {
        return ticket;
    }

    public void setTicket(com.example.hp.tmastering.beans.ticket ticket) {
        this.ticket = ticket;
    }

}
