package com.adeo.connector.opus.utils;

import com.adeo.connector.opus.annotations.*;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by stievena on 02/10/16.
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static <T> T parseModelType(Object json, Class<T> model) throws Exception {
        T objectModel = model.newInstance();
        for (java.lang.reflect.Field classField : getAllFields(model)) {
            StringBuilder xpath = new StringBuilder();
            Mask mask = classField.getAnnotation(Mask.class);
            Field field = classField.getAnnotation(Field.class);
            Multivalue multivalue = classField.getAnnotation(Multivalue.class);
            ModelType modelType = classField.getAnnotation(ModelType.class);
            Identifier identifier = classField.getAnnotation(Identifier.class);
            if (field != null) {
                if (mask != null) {
                    xpath.append("$.chapter");
                    xpath.append("[?(@.name == '").append(field.value()).append("')]");
                    if (multivalue != null) {
                        xpath.append(".attribute");
                    } else {
                        xpath.append("..value");
                    }
                } else {
                    xpath.append("$.attribute[?(@.href == '").append(field.value()).append("')].value");
                }

                List<Object> jsonObject = JsonPath.read(json, xpath.toString());
                if (jsonObject.size() > 0) {
                    if (multivalue != null) {
                        List<AbstractMap.SimpleImmutableEntry> multivalueBeanList = new ArrayList();
                        ((JSONArray) jsonObject.get(0)).forEach(o -> {
                            multivalueBeanList.add(new AbstractMap.SimpleImmutableEntry((String) ((Map) o).get(multivalue.keyField()), ((JSONArray) ((Map) o).get(multivalue.valueField())).get(0)));
                        });
                        BeanUtils.copyProperty(objectModel, classField.getName(), multivalueBeanList);
                    } else {
                        BeanUtils.copyProperty(objectModel, classField.getName(), jsonObject.get(0));
                    }
                } else {
                    logger.warn("Could not find xpath '" + xpath.toString() + "' in " + json.toString());
                }
            } else if (modelType != null) {
                Class modelClass = modelType.modelClass();
                if (Collection.class.isAssignableFrom(classField.getType())) {
                    List modelList = new ArrayList<>();
                    xpath.append("$.").append(modelType.path());
                    List<Object> jsonObject = JsonPath.read(json, xpath.toString());
                    if (jsonObject.size() > 0) {
                        jsonObject.forEach(o -> {
                            try {
                                modelList.add(parseModelType(o, modelClass));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    BeanUtils.copyProperty(objectModel, classField.getName(), modelList);
                }
            } else if (identifier != null) {
                xpath.append("$.id");
                String id = JsonPath.read(json, xpath.toString());
                BeanUtils.copyProperty(objectModel, classField.getName(), id);
            }
        }
        return objectModel;
    }

    private static List<java.lang.reflect.Field> getAllFields(Class<?> type) {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}
