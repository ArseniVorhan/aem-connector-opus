package com.adeo.connector.opus.utils;

import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Path;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by stievena on 02/10/16.
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static <T> T parseModelType(Object json, Class<T> model) throws Exception {
        T objectModel = model.newInstance();
        for (Field field : getAllFields(model)) {
            StringBuilder xpath = new StringBuilder();
            Mask mask = field.getAnnotation(Mask.class);
            Path path = field.getAnnotation(Path.class);
            if (path != null) {
                if (mask != null) {
                    xpath.append("$..chapter");
                    xpath.append("[?(@.name == '").append(path.value()).append("')]..value");
                } else {
                    xpath.append("$..attribute[?(@.href == '").append(path.value()).append("')].value");
                }

                List<Object> jsonObject = JsonPath.read(json, xpath.toString());
                if (jsonObject.size() > 0) {
                    BeanUtils.copyProperty(objectModel, field.getName(), jsonObject.get(0));
                } else {
                    logger.warn("Could not find xpath '" + xpath.toString() + "' in " + json.toString());
                }
            }
        }
        return objectModel;
    }

    private static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
