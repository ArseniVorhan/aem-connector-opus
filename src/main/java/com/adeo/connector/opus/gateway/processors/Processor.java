package com.adeo.connector.opus.gateway.processors;

import com.adeo.connector.opus.gateway.OpusResponse;
import com.adobe.connector.ConnectorRequest;

/**
 * Created by stievena on 29/09/16.
 */
public interface Processor {
    String getName();

    <T> OpusResponse<T> process(byte[] data, ConnectorRequest request, Class<T> modelClass);
}
