package com.adeo.connector.opus.gateways;

import com.adobe.connector.gateways.http.RestRequest;

import java.util.Arrays;

public abstract class OpusRequest extends RestRequest {
    protected String[] arguments;

    @Override
    protected String[] getParameters() {
        return this.arguments;
    }

    public OpusRequest(String... arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "OpusRequest{" +
                "arguments=" + Arrays.toString(arguments) +
                '}';
    }
}
