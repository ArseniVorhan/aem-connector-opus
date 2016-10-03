package com.adeo.connector.opus.gateways.processors;

import com.adeo.connector.opus.gateways.OpusResponse;
import com.adeo.connector.opus.utils.JsonUtils;
import com.adobe.connector.gateways.ConnectorRequest;
import com.adobe.connector.gateways.ConnectorResponse;
import com.adobe.connector.gateways.http.Processor;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
@Component(immediate = true)
@Service(value = Processor.class)
public class FamilyProcessor implements Processor {

    private final static Logger logger = LoggerFactory.getLogger(FamilyProcessor.class);

    @Override
    public String getName() {
        return "FamilyProcessor";
    }

    @Override
    public void process(InputStream inputStream, ConnectorRequest connectorRequest, ConnectorResponse connectorResponse) {
        OpusResponse response = (OpusResponse) connectorResponse;

        List<Serializable> products = new ArrayList<>();

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(inputStream, "UTF-8");

        JSONArray contentItem = JsonPath.read(document, "$.content");
        contentItem.stream().forEach(o -> parseProduct(connectorRequest, products, o, response.getModelClass()));

        response.setModel(products.toArray());
    }

    private void parseProduct(ConnectorRequest connectorRequest, List<Serializable> products, Object newProduct, Class modelClass) {
        try {
            products.add((Serializable) JsonUtils.parseModelType(newProduct, modelClass));
        } catch (Exception e) {
            logger.error("Error while parsing JSON for request '" + connectorRequest.getClass().getName() + "' " + connectorRequest.toString(), e);
        }
    }

}