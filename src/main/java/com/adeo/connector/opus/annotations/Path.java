package com.adeo.connector.opus.annotations;

import java.lang.annotation.*;

/**
 * Created by stievena on 29/09/16.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    String value();
}
