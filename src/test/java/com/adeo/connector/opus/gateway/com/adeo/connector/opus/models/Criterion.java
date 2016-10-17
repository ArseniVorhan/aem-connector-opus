package com.adeo.connector.opus.gateway.com.adeo.connector.opus.models;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Identifier;
import com.adeo.connector.opus.annotations.ModelType;

import java.util.List;

/**
 * Created by stievena on 14/10/16.
 */
public class Criterion {
    @Identifier
    private String id;
    @Field("/foundation/v2/attributes/name")
    private String name;
    @ModelType(modelClass = Segment.class, path = "segments")
    private List<Segment> segments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
