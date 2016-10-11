package com.adeo.connector.opus.gateways.processors;

import com.adobe.connector.ConnectorRequest;
import com.adobe.connector.ConnectorResponse;

/**
 * Created by stievena on 29/09/16.
 */
public interface Processor {
    String getName();

    ConnectorResponse process(byte[] data, ConnectorRequest request, Class modelClass);
}
