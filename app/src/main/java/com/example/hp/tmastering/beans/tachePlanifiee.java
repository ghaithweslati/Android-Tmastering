package com.example.hp.tmastering.beans;

/**
 * Created by HP on 08/06/2018.
 */

public class tachePlanifiee extends tache{

    private module module;

    public tachePlanifiee(String titre, String dateDeb, String datefin, com.example.hp.tmastering.beans.operateur operateur, com.example.hp.tmastering.beans.module module) {
        super(titre, dateDeb, datefin, operateur);
        this.module = module;
    }

    public tachePlanifiee(int id, String titre, String dateDeb, String datefin, com.example.hp.tmastering.beans.operateur operateur, com.example.hp.tmastering.beans.module module) {
        super(id, titre, dateDeb, datefin, operateur);
        this.module = module;
    }

    public com.example.hp.tmastering.beans.module getModule() {
        return module;
    }

    public void setModule(com.example.hp.tmastering.beans.module module) {
        this.module = module;
    }
}
