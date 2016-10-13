package com.adeo.connector.opus;

import com.adeo.connector.opus.gateway.OpusRequest;

import java.util.Map;

/**
 * Created by stievena on 29/09/16.
 */
public class FamilyRequest extends OpusRequest {

    public FamilyRequest(Map<String, String> headers, String... arguments) {
        super(headers, arguments);
    }

    public FamilyRequest(String... arguments) {
        super(null, arguments);
    }
}
