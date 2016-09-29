package com.adeo.connector.opus.gateways.processors;

import com.adobe.connector.gateways.ConnectorRequest;
import com.adobe.connector.gateways.ConnectorResponse;
import com.adobe.connector.gateways.http.Processor;
import okhttp3.Response;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

/**
 * Created by stievena on 29/09/16.
 */
@Component(immediate = true)
@Service(value = Processor.class)
public class DefaultOpusProcessor implements Processor {
    @Override
    public String getName() {
        return "com.adeo.connector.opus.gateways.processors.DefaultOpusProcessor";
    }

    @Override
    public void process(Response httpResponse, ConnectorRequest connectorRequest, ConnectorResponse connectorResponse) {
        try {
            System.out.print(httpResponse.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
