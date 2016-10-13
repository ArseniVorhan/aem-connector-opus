package com.adeo.connector.opus.gateway.processors;

import com.adeo.connector.opus.ProductDetailsRequest;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.com.adeo.connector.opus.models.ProductMask;
import org.apache.sling.testing.mock.osgi.junit.OsgiContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

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
        ProductDetailsRequest opusRequest = new ProductDetailsRequest(null);
        OpusResponse<ProductMask> response = processor.process(Files.readAllBytes(Paths.get("src/test/resources/mask.json")), opusRequest, ProductMask.class);
        Assert.assertEquals("the best driller ever", response.getResults().get(0).getHead());
    }


}
