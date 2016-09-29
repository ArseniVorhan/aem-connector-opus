package com.adeo.connector.opus.gateways;

import com.adeo.connector.opus.gateways.processors.DefaultOpusProcessor;
import com.adobe.connector.services.OrchestratorService;
import com.adobe.connector.services.impl.GatewayFactoryServiceImpl;
import com.adobe.connector.services.impl.GatewayResolverImpl;
import com.adobe.connector.services.impl.OrchestratorServiceImpl;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
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

    private OpusRequest opusRequest = new OpusRequest();

    private OpusResponse opusResponse = new OpusResponse();

    @Before
    public void setUp() throws Exception {

        context.registerInjectActivateService(new GatewayResolverImpl());

        context.registerInjectActivateService(new OpusGateway(), ImmutableMap.<String, Object>builder()
                .put(OpusGateway.GATEWAY_NAME, "opusGateway")
                .put(OpusGateway.OPUS_DOMAIN, "webtop.dogfood2.ovh.wikeo.webadeo.net")
                .put(OpusGateway.OPUS_CONTEXT, "/wikeo-core")
                .put(OpusGateway.OPUS_SCHEME, "http")
                .put(OpusGateway.OPUS_USERNAME, "wikeo")
                .put(OpusGateway.OPUS_PASSWORD, "oekiw")
                .put(OpusGateway.MAPPINGS, new String[]{"com.adeo.connector.opus.gateways.OpusRequest:/business/v2/products/{0}:com.adeo.connector.opus.gateways.processors.DefaultOpusProcessor"})
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
        OrchestratorService orchestratorService = context.getService(OrchestratorService.class);
        opusRequest.setParameters("USER.1acfc985-ab01-470a-8ad6-4973f8be6ecc_Product_CNT");
        orchestratorService.execute(opusRequest, opusResponse);
    }

}
