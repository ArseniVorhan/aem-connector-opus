package com.adeo.connector.opus.gateway;

import com.adeo.connector.opus.FamilyRequest;
import com.adeo.connector.opus.ProductDetailsRequest;
import com.adeo.connector.opus.gateway.com.adeo.connector.opus.models.ProductModel;
import com.adeo.connector.opus.gateway.processors.ContentSetProcessor;
import com.adeo.connector.opus.gateway.processors.ModelTypeProcessor;
import com.adobe.connector.gateway.connection.http.HttpEndpointConnector;
import com.adobe.connector.gateway.connection.http.OkHttpEndpointClient;
import com.adobe.connector.services.OrchestratorService;
import com.adobe.connector.services.impl.DefaultExecutionPlanBuilder;
import com.adobe.connector.services.impl.DefaultOrchestratorService;
import com.adobe.connector.services.impl.ExecutionPlanFactoryImpl;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OpusGatewayTest {

    @Rule
    public final OsgiContext context = new OsgiContext();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private OrchestratorService orchestratorService = new DefaultOrchestratorService();

    private OpusGateway opusGateway = new OpusGateway();

    @Before
    public void setUp() throws Exception {

        context.registerInjectActivateService(new OkHttpEndpointClient());
        context.registerInjectActivateService(new HttpEndpointConnector());
        context.registerInjectActivateService(new DefaultExecutionPlanBuilder());

        context.registerInjectActivateService(opusGateway, ImmutableMap.<String, Object>builder()
                .put(OpusGateway.GATEWAY_NAME, "opusGateway")
                .put(OpusGateway.OPUS_DOMAIN, "opus-core.adobe.demo.web.opus.webadeo.net")
                .put(OpusGateway.OPUS_CONTEXT, "")
                .put(OpusGateway.OPUS_SCHEME, "http")
                .put(OpusGateway.OPUS_USERNAME, "wikeo")
                .put(OpusGateway.OPUS_PASSWORD, "oekiw")
                .put(OpusGateway.MAPPINGS, new String[]{"com.adeo.connector.opus.ProductDetailsRequest:/business/v2/products/{0}:ModelTypeProcessor:com.adeo.connector.opus.gateway.com.adeo.connector.opus.models.ProductModel", "com.adeo.connector.opus.FamilyRequest:/business/v2/families/{0}/contentSet/contents:ContentSetProcessor:com.adeo.connector.opus.gateway.com.adeo.connector.opus.models.ProductModel"})
                .build());

        context.registerInjectActivateService(new ModelTypeProcessor());
        context.registerInjectActivateService(new ContentSetProcessor());

        context.registerInjectActivateService(new ExecutionPlanFactoryImpl(), ImmutableMap.<String, Object>builder()
                .put("gateway.name", "opusGateway")
                .put("request", "com.adeo.connector.opus.gateway.OpusRequest")
                .build());

        context.registerInjectActivateService(orchestratorService);


    }

    @Test()
    public void testProduct() {
        OpusRequest opusRequest = new ProductDetailsRequest("12868691_refproduct_Product");
        OrchestratorService orchestratorService = context.getService(OrchestratorService.class);
        OpusResponse<ProductModel> response = (OpusResponse) orchestratorService.execute(opusRequest);
        Assert.assertEquals("0622-TABLEAU ELECTRIQUE : DISJONCTEUR POUR ...", response.getResults().get(0).getDesignation());
        Assert.assertEquals("дифавтоматы", response.getResults().get(0).getResistanceFeu());
    }

    @Test()
    public void testFamily() {
        OpusRequest opusRequest = new FamilyRequest("d9446ea6-86fe-421e-ad4f-102fcb0365c3_Opus_Family");
        OrchestratorService orchestratorService = context.getService(OrchestratorService.class);
        OpusResponse<ProductModel> response = (OpusResponse) orchestratorService.execute(opusRequest);
        List<ProductModel> models = response.getResults();
        Assert.assertEquals(10, models.size());
    }


}
