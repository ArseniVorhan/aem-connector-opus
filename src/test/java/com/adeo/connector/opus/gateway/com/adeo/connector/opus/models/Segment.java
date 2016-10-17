package com.adeo.connector.opus.gateway.com.adeo.connector.opus.models;

import com.adeo.connector.opus.annotations.Field;

/**
 * Created by stievena on 14/10/16.
 */
public class Segment {
    @Field("/foundation/v2/attributes/name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
