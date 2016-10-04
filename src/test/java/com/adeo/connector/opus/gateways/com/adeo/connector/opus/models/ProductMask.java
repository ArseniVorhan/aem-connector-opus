package com.adeo.connector.opus.gateways.com.adeo.connector.opus.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Multivalue;
import com.adeo.connector.opus.models.MultivalueBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
public class ProductMask implements Serializable {

    @Mask
    @Field("caracs")
    @Multivalue(keyField = "href")
    private List<MultivalueBean> caracs;

    @Mask
    @Field("head")
    private String head;

    public List<MultivalueBean> getCaracs() {
        return caracs;
    }

    public void setCaracs(List<MultivalueBean> caracs) {
        this.caracs = caracs;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }
}
