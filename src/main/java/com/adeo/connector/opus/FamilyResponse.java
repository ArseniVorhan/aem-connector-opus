package com.adeo.connector.opus;

import com.adeo.connector.opus.gateways.OpusResponse;

import java.io.Serializable;

/**
 * Created by stievena on 29/09/16.
 */
public class FamilyResponse<T extends Serializable> extends OpusResponse<T> {

    public FamilyResponse(Class<T> modelClass) {
        super(modelClass);
    }

}
