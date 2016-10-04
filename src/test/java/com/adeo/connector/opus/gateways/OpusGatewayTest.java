package com.adeo.connector.opus.gateways;

import com.adeo.connector.opus.ProductDetailsRequest;
import com.adeo.connector.opus.ProductDetailsResponse;
import com.adeo.connector.opus.gateways.com.adeo.connector.opus.models.ProductModel;
import com.adeo.connector.opus.gateways.processors.FamilyProcessor;
import com.adeo.connector.opus.gateways.processors.ModelTypeProcessor;
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

    private OpusGateway opusGateway = new OpusGateway();

    @Before
    public void setUp() throws Exception {

        context.registerInjectActivateService(new GatewayResolverImpl());

        context.registerInjectActivateService(opusGateway, ImmutableMap.<String, Object>builder()
                .put(OpusGateway.GATEWAY_NAME, "opusGateway")
                .put(OpusGateway.OPUS_DOMAIN, "opus-core.adobe.demo.web.opus.webadeo.net")
                .put(OpusGateway.OPUS_CONTEXT, "")
                .put(OpusGateway.OPUS_SCHEME, "http")
                .put(OpusGateway.OPUS_USERNAME, "")
                .put(OpusGateway.OPUS_PASSWORD, "")
                .put(OpusGateway.MAPPINGS, new String[]{"com.adeo.connector.opus.ProductDetailsRequest:/business/v2/products/{0}:ModelTypeProcessor", "com.adeo.connector.opus.FamilyRequest:/business/v2/families/{0}/contentSet/contents:FamilyProcessor"})
                .build());

        context.registerInjectActivateService(new ModelTypeProcessor());
        context.registerInjectActivateService(new FamilyProcessor());

        context.registerInjectActivateService(new GatewayFactoryServiceImpl(), ImmutableMap.<String, Object>builder()
                .put("name", "opusGateway")
                .put("request.allowed", "com.adeo.connector.opus.gateways.OpusRequest")
                .build());

        context.registerInjectActivateService(orchestratorService);


    }

    @Test()
    public void testProduct() {
        OpusRequest opusRequest = new ProductDetailsRequest("12868691_refproduct_Product");
        ProductDetailsResponse<ProductModel> opusResponse = new ProductDetailsResponse(ProductModel.class);
        OrchestratorService orchestratorService = context.getService(OrchestratorService.class);
        orchestratorService.execute(opusRequest, opusResponse);
        Assert.assertEquals("0622-TABLEAU ELECTRIQUE : DISJONCTEUR POUR ...", opusResponse.getModel().getDesignation());
        Assert.assertEquals("дифавтоматы", opusResponse.getModel().getResistanceFeu());
    }

    @Test()
    public void testFamily() {
//        OpusRequest opusRequest = new FamilyRequest("54664fbe-51a8-4282-a1ed-518993d28b27_Opus_Family");
//        FamilyResponse<ProductModel[]> opusResponse = new FamilyResponse(ProductModel.class);
//        OrchestratorService orchestratorService = context.getService(OrchestratorService.class);
//        orchestratorService.execute(opusRequest, opusResponse);
//        ProductModel[] models = opusResponse.getModel();
//        Assert.assertEquals(10, models.length);
    }


}
