package com.adeo.connector.opus;

import com.adeo.connector.opus.gateways.OpusResponse;

import java.util.List;

/**
 * Created by stievena on 29/09/16.
 */
public class ProductDetailsResponse extends OpusResponse {

    public ProductDetailsResponse(List results) {
        super(results);
    }
}
