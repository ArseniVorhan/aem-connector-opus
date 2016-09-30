package com.adeo.connector.opus.gateways.processors;

import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Path;
import com.adeo.connector.opus.gateways.OpusResponse;
import com.adobe.connector.gateways.ConnectorRequest;
import com.adobe.connector.gateways.ConnectorResponse;
import com.adobe.connector.gateways.http.Processor;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
@Component(immediate = true)
@Service(value = Processor.class)
public class DefaultOpusProcessor implements Processor {

    private final static Logger logger = LoggerFactory.getLogger(DefaultOpusProcessor.class);

    @Override
    public String getName() {
        return "DefaultOpusProcessor";
    }

    @Override
    public void process(InputStream inputStream, ConnectorRequest connectorRequest, ConnectorResponse connectorResponse) {
        OpusResponse response = (OpusResponse) connectorResponse;
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(inputStream, "UTF-8");

        try {
            Serializable objectModel = (Serializable) response.getModelClass().newInstance();
            response.setModel(objectModel);
            for (Field field : getAllFields(response.getModelClass())) {
                StringBuilder xpath = new StringBuilder();
                Mask mask = field.getAnnotation(Mask.class);
                Path path = field.getAnnotation(Path.class);
                if (path != null) {
                    if (mask != null) {
                        xpath.append("$..chapter");
                        xpath.append("[?(@.name == '").append(path.value()).append("')]");
                    } else {
                        xpath.append("$..attribute[?(@.href == '").append(path.value()).append("')].value");
                    }

                    List<Object> jsonObject = JsonPath.read(document, xpath.toString());
                    BeanUtils.copyProperty(objectModel, field.getName(), jsonObject.get(0));
                }

            }
        } catch (Exception e) {
            logger.error("Error");
        }
    }

    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
