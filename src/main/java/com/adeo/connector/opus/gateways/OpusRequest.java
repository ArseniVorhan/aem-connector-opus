package com.adeo.connector.opus.gateways;

import com.adobe.connector.gateways.http.RestRequest;

public abstract class OpusRequest extends RestRequest {
    protected String[] arguments;

    @Override
    protected String[] getParameters() {
        return this.arguments;
    }

    public OpusRequest(String... arguments) {
        this.arguments = arguments;
    }
}
