package com.adeo.connector.opus.gateway.com.adeo.connector.opus.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Multivalue;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
public class ProductMask implements Serializable {

    @Mask
    @Field("caracs")
    @Multivalue(keyField = "href")
    private List<AbstractMap.SimpleImmutableEntry> caracs;

    @Mask
    @Field("head")
    private String head;

    public List<AbstractMap.SimpleImmutableEntry> getCaracs() {
        return caracs;
    }

    public void setCaracs(List<AbstractMap.SimpleImmutableEntry> caracs) {
        this.caracs = caracs;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
