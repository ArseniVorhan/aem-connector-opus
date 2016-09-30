package com.adeo.connector.opus.gateways;


import com.adobe.connector.gateways.http.RestResponse;

import java.io.Serializable;

/**
 * Opus's Reponse Object.
 *
 * @author kassa
 */
public abstract class OpusResponse<T extends Serializable> extends RestResponse {

    protected T model;
    protected Class<T> modelClass;

    @Override
    public T getModel() {
        return model;
    }

    public OpusResponse(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public Class<T> getModelClass() {
        return modelClass;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
