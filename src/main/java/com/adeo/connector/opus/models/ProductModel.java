package com.adeo.connector.opus.models;

import com.adeo.connector.opus.annotations.Mask;
import com.adeo.connector.opus.annotations.Path;

import java.io.Serializable;

/**
 * Created by stievena on 29/09/16.
 */
public class ProductModel implements Serializable {
    @Mask("StaticMask")
    @Path("title")
    private String title;


}
