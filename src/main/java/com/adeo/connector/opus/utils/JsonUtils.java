package com.adeo.connector.opus.utils;

import com.adeo.connector.opus.annotations.Field;
import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Multivalue;
import com.adeo.connector.opus.models.MultivalueBean;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            if (field != null) {
                if (mask != null) {
                    xpath.append("$..chapter");
                    xpath.append("[?(@.name == '").append(field.value()).append("')]");
                    if (multivalue != null) {
                        xpath.append(".attribute");
                    } else {
                        xpath.append("..value");
                    }
                } else {
                    xpath.append("$..attribute[?(@.href == '").append(field.value()).append("')].value");
                }

                List<Object> jsonObject = JsonPath.read(json, xpath.toString());
                if (jsonObject.size() > 0) {
                    if (multivalue != null) {
                        List<MultivalueBean> multivalueBeanList = new ArrayList();
                        ((JSONArray) jsonObject.get(0)).forEach(o -> {
                            MultivalueBean newBean = new MultivalueBean();
                            newBean.setKey((String) ((Map) o).get(multivalue.keyField()));
                            newBean.setValue(((JSONArray) ((Map) o).get(multivalue.valueField())).get(0));
                            multivalueBeanList.add(newBean);
                        });
                        BeanUtils.copyProperty(objectModel, classField.getName(), multivalueBeanList);
                    } else {
                        BeanUtils.copyProperty(objectModel, classField.getName(), jsonObject.get(0));
                    }
                } else {
                    logger.warn("Could not find xpath '" + xpath.toString() + "' in " + json.toString());
                }
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
