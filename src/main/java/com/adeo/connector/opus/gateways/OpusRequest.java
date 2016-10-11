package com.adeo.connector.opus.gateways;

import com.adobe.connector.ConnectorRequest;

import java.util.Map;

public class OpusRequest extends ConnectorRequest {

    protected String[] parameters;
    protected Map<String, String> headers;

    public OpusRequest(Map<String, String> headers, String... parameters) {
        this.parameters = parameters;
        this.headers = headers;
    }

    public String[] getParameters() {
        return this.parameters;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

}
