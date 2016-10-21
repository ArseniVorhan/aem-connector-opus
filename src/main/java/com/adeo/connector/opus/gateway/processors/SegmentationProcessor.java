package com.adeo.connector.opus.gateway.processors;


import com.adeo.connector.opus.utils.JsonUtils;
import com.adobe.connector.ConnectorRequest;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
@Component(immediate = true)
@Service(value = Processor.class)
public class SegmentationProcessor implements Processor {

    private final static Logger logger = LoggerFactory.getLogger(SegmentationProcessor.class);

    @Override
    public String getName() {
        return "SegmentationProcessor";
    }

    @Override
    public List process(byte[] data, ConnectorRequest connectorRequest, Class modelClass) {
        try {
            List objectList = new ArrayList<>();

            Object document = Configuration.defaultConfiguration().jsonProvider().parse(new ByteArrayInputStream(data), "UTF-8");

            JSONArray contentItem = JsonPath.read(document, "$.segmentationCriteria");
            contentItem.stream().forEach(o -> parseObject(connectorRequest, objectList, o, modelClass));

            return objectList;
        } catch (Exception e) {
            logger.error("Error while parsing JSON for request '" + connectorRequest.getClass().getName() + "' " + connectorRequest.toString(), e);
        }
        return null;
    }

    private void parseObject(ConnectorRequest connectorRequest, List products, Object newProduct, Class modelClass) {
        try {
            products.add(JsonUtils.parseModelType(newProduct, modelClass));
        } catch (Exception e) {
            logger.error("Error while parsing JSON for request '" + connectorRequest.getClass().getName() + "' " + connectorRequest.toString(), e);
        }
    }

}
