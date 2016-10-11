package com.adeo.connector.opus.gateways;


import com.adobe.connector.ConnectorResponse;

import java.util.List;

/**
 * Opus's Reponse Object.
 *
 * @author kassa
 */
public class OpusResponse<T> extends ConnectorResponse<T> {

    private final static OpusResponse NO_RESPONSE = new OpusResponse(null);

    private int status;
    private String message;
    private List<T> results;

    public OpusResponse(List<T> results) {
        this.results = results;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public List<T> getResults() {
        return results;
    }

    public static OpusResponse makeNoResponse() {
        return NO_RESPONSE;
    }
}
