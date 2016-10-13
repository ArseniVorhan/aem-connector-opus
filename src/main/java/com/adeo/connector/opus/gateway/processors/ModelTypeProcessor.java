package com.adeo.connector.opus.gateway.processors;


import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.utils.JsonUtils;
import com.adobe.connector.ConnectorRequest;
import com.jayway.jsonpath.Configuration;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Collections;

/**
 * Created by stievena on 29/09/16.
 */
@Component(immediate = true)
@Service(value = Processor.class)
public class ModelTypeProcessor implements Processor {

    private final static Logger logger = LoggerFactory.getLogger(ModelTypeProcessor.class);

    @Override
    public String getName() {
        return "ModelTypeProcessor";
    }

    @Override
    public OpusResponse process(byte[] data, ConnectorRequest connectorRequest, Class modelClass) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(new ByteArrayInputStream(data), "UTF-8");
        try {
            return new OpusResponse(Collections.singletonList(JsonUtils.parseModelType(document, modelClass)));
        } catch (Exception e) {
            logger.error("Error while parsing JSON for request '" + connectorRequest.getClass().getName() + "' " + connectorRequest.toString(), e);
        }
        return null;
    }

}
