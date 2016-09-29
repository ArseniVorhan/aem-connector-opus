package com.adeo.connector.opus.gateways;

import com.adobe.connector.gateways.ConnectorRequest;

public class OpusRequest implements ConnectorRequest {

    private String[] parameters;

    @Override
    public String getName() {
        return "com.adeo.connector.opus.gateways.OpusRequest";
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String... parameters) {
        this.parameters = parameters;
    }
}
