package com.adeo.connector.opus.gateways.processors;

import com.adeo.connector.opus.gateways.OpusResponse;
import com.adeo.connector.opus.utils.JsonUtils;
import com.adobe.connector.gateways.ConnectorRequest;
import com.adobe.connector.gateways.ConnectorResponse;
import com.adobe.connector.gateways.http.Processor;
import com.jayway.jsonpath.Configuration;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Serializable;

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
    public void process(InputStream inputStream, ConnectorRequest connectorRequest, ConnectorResponse connectorResponse) {
        OpusResponse response = (OpusResponse) connectorResponse;
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(inputStream, "UTF-8");
        try {
            response.setModel((Serializable) JsonUtils.parseModelType(document, response.getModelClass()));
        } catch (Exception e) {
            logger.error("Error while parsing JSON for request '" + connectorRequest.getClass().getName() + "' " + connectorRequest.toString(), e);
        }
    }

}
