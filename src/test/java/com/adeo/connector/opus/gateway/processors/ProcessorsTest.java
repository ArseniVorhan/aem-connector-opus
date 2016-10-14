package com.adeo.connector.opus.gateway.processors;

import com.adeo.connector.opus.ProductDetailsRequest;
import com.adeo.connector.opus.gateway.OpusResponse;
import com.adeo.connector.opus.gateway.com.adeo.connector.opus.models.Criterion;
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
public class ProcessorsTest {

    @Rule
    public final OsgiContext context = new OsgiContext();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private ModelTypeProcessor modelTypeProcessor = new ModelTypeProcessor();
    private SegmentationProcessor segmentationProcessor = new SegmentationProcessor();

    @Before
    public void setUp() throws Exception {

        context.registerInjectActivateService(modelTypeProcessor);

    }

    @Test()
    public void testModelType() throws Exception {
        ProductDetailsRequest opusRequest = new ProductDetailsRequest(null);
        OpusResponse<ProductMask> response = modelTypeProcessor.process(Files.readAllBytes(Paths.get("src/test/resources/mask.json")), opusRequest, ProductMask.class);
        Assert.assertEquals("the best driller ever", response.getResults().get(0).getHead());
    }

    @Test()
    public void testSegmentation() throws Exception {
        ProductDetailsRequest opusRequest = new ProductDetailsRequest(null);
        OpusResponse<Criterion> response = segmentationProcessor.process(Files.readAllBytes(Paths.get("src/test/resources/segments.json")), opusRequest, Criterion.class);
        Assert.assertEquals("8825eea3-2f15-4ade-9070-1160600794b9_Opus_Criterion", response.getResults().get(0).getId());
        Assert.assertEquals("Localisation", response.getResults().get(0).getName());
        Assert.assertEquals("Local", response.getResults().get(0).getSegments().get(0).getName());
    }


}
