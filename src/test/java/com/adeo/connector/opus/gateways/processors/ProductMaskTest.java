package com.adeo.connector.opus.gateways.processors;

import com.adeo.connector.opus.ProductDetailsRequest;
import com.adeo.connector.opus.ProductDetailsResponse;
import com.adeo.connector.opus.gateways.com.adeo.connector.opus.models.ProductMask;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;

@RunWith(MockitoJUnitRunner.class)
public class ProductMaskTest {

    @Rule
    public final OsgiContext context = new OsgiContext();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ModelTypeProcessor processor = new ModelTypeProcessor();

    @Before
    public void setUp() throws Exception {

        context.registerInjectActivateService(processor);

    }

    @Test()
    public void testProductMask() throws Exception {
        ProductDetailsRequest opusRequest = new ProductDetailsRequest();
        ProductDetailsResponse<ProductMask> opusResponse = new ProductDetailsResponse(ProductMask.class);
        processor.process(new FileInputStream("src/test/resources/mask.json"), opusRequest, opusResponse);
        Assert.assertEquals("the best driller ever", opusResponse.getModel().getHead());
    }


}
