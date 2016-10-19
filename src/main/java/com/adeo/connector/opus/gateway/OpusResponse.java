package com.adeo.connector.opus.gateway;

import com.adobe.connector.RestResponse;

import java.util.List;

/**
 * Created by stievena on 19/10/16.
 */
public class OpusResponse<T> extends RestResponse<T> {
    public OpusResponse(List<T> results, int status, String message) {
        super(results, status, message);
    }
}
