package com.adeo.connector.opus.gateways.com.adeo.connector.opus.models;

import com.adeo.connector.opus.annotations.Field;

import java.io.Serializable;

/**
 * Created by stievena on 29/09/16.
 */
public class ProductModel implements Serializable {



    @Field("/foundation/v2/attributes/1/contexts/lang/RU")
    private String designation;
    @Field("/foundation/v2/attributes/1445/contexts/lang/RU")
    private String resistanceFeu;

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getResistanceFeu() {
        return resistanceFeu;
    }

    public void setResistanceFeu(String resistanceFeu) {
        this.resistanceFeu = resistanceFeu;
    }
}
