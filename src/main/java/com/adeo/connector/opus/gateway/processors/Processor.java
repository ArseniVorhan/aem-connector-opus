package com.adeo.connector.opus.gateway.processors;

import com.adobe.connector.ConnectorRequest;
import com.adobe.connector.ConnectorResponse;

import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
public interface Processor {
    String getName();

    <T> List<T> process(byte[] data, ConnectorRequest request, Class<T> modelClass);
}
