package com.adeo.connector.opus.gateways;

import com.adeo.connector.opus.ProductDetailsRequest;
import com.adeo.connector.opus.ProductDetailsResponse;
import com.adeo.connector.opus.gateways.processors.DefaultOpusProcessor;
import com.adeo.connector.opus.models.ProductModel;
import com.adobe.connector.services.OrchestratorService;
import com.adobe.connector.services.impl.GatewayFactoryServiceImpl;
import com.adobe.connector.services.impl.GatewayResolverImpl;
import com.adobe.connector.services.impl.OrchestratorServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpusGatewayTest {

    @Rule
    public final OsgiContext context = new OsgiContext();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private OrchestratorService orchestratorService = new OrchestratorServiceImpl();

    @Before
    public void setUp() throws Exception {

        context.registerInjectActivateService(new GatewayResolverImpl());

        context.registerInjectActivateService(new OpusGateway(), ImmutableMap.<String, Object>builder()
                .put(OpusGateway.GATEWAY_NAME, "opusGateway")
                .put(OpusGateway.OPUS_DOMAIN, "webtop.dogfood2.ovh.wikeo.webadeo.net")
                .put(OpusGateway.OPUS_CONTEXT, "/wikeo-core")
                .put(OpusGateway.OPUS_SCHEME, "http")
                .put(OpusGateway.OPUS_USERNAME, "")
                .put(OpusGateway.OPUS_PASSWORD, "")
                .put(OpusGateway.MAPPINGS, new String[]{"com.adeo.connector.opus.gateways.OpusRequest:/business/v2/products/{0}:DefaultOpusProcessor"})
                .build());

        context.registerInjectActivateService(new DefaultOpusProcessor());

        context.registerInjectActivateService(new GatewayFactoryServiceImpl(), ImmutableMap.<String, Object>builder()
                .put("name", "opusGateway")
                .put("request.allowed", "com.adeo.connector.opus.gateways.OpusRequest")
                .build());

        context.registerInjectActivateService(orchestratorService);


    }

    @Test()
    public void testGateway() {
        OpusRequest opusRequest = new ProductDetailsRequest("USER.1acfc985-ab01-470a-8ad6-4973f8be6ecc_Product_CNT");
        ProductDetailsResponse<ProductModel> opusResponse = new ProductDetailsResponse(ProductModel.class);
        OrchestratorService orchestratorService = context.getService(OrchestratorService.class);
        orchestratorService.execute(opusRequest, opusResponse);
//        Assert.assertEquals("Coffre fort electronique a emmurer TECHNOMAX GT4P", opusResponse.getModel().getDesignation());
//        Assert.assertEquals("Non", opusResponse.getModel().getResistanceFeu());
    }

}
