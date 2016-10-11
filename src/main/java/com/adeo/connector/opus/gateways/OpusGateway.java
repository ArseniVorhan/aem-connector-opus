package com.adeo.connector.opus.gateways;

import com.adeo.connector.opus.gateways.processors.Processor;
import com.adeo.connector.opus.gateways.processors.Worker;
import com.adobe.connector.ConnectorRequest;
import com.adobe.connector.ConnectorResponse;
import com.adobe.connector.gateways.Gateway;
import com.adobe.connector.gateways.GatewayRequest;
import com.adobe.connector.gateways.connection.EndpointConnector;
import com.adobe.connector.gateways.connection.EndpointResponse;
import com.adobe.connector.gateways.connection.http.HttpEndpointResponse;
import com.adobe.connector.gateways.connection.http.HttpResponse;
import com.adobe.connector.gateways.message.Message;
import com.adobe.connector.gateways.message.RestMessage;
import com.adobe.connector.utils.ConnectorUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component(immediate = true, label = "Adeo OPUS Connector Gateway", description = "Gateway for communicating with OPUS back end", metatype = true)
@Service(value = Gateway.class)
@References({
        @Reference(name = "processor", referenceInterface = Processor.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)})
public class OpusGateway extends Gateway {

    private final static Logger logger = LoggerFactory.getLogger(OpusGateway.class);

    @Reference
    private EndpointConnector endpointConnector;

    private final Map<String, Processor> processors = new HashMap<>();

    protected void bindProcessor(final Processor processor, final Map<String, Object> properties) {
        if (processor != null) {
            synchronized (this.processors) {
                this.processors.put(processor.getName(), processor);
            }
        }
    }

    protected void unbindProcessor(final Processor processor, final Map<String, Object> properties) {
        if (processor != null) {
            synchronized (this.processors) {
                this.processors.remove(processor.getName());
            }
        }
    }

    @Property(label = "Name", description = "The unique name of the gateway")
    protected static final String GATEWAY_NAME = "gateway.name";

    @Property(label = "URL Scheme", description = "URL scheme used for OPUS requests. Values are 'http' or 'https'")
    protected static final String OPUS_SCHEME = "opus.url.scheme";

    @Property(label = "URL Domain", description = "URL domain used for OPUS requests. Form is 'example.com[:port]'")
    protected static final String OPUS_DOMAIN = "opus.url.domain";

    @Property(label = "URL Context", description = "URL context used for OPUS requests. Form is '/context'")
    protected static final String OPUS_CONTEXT = "opus.url.context";

    @Property(label = "Username", description = "Username for authentication to OPUS")
    protected static final String OPUS_USERNAME = "opus.auth.username";

    @Property(label = "Password", description = "Password for authentication to OPUS")
    protected static final String OPUS_PASSWORD = "opus.auth.password";

    @Property(label = "Request Mappings", description = "Map request type to OPUS url path. Each entry is of the form 'request\":\"path\":\"processor'", unbounded = PropertyUnbounded.ARRAY)
    protected static final String MAPPINGS = "request.mappings";

    private String opusScheme;
    private String opusDomain;
    private String opusContext;
    private String opusUsername;
    private String opusPassword;
    private String name;
    private String[] mappings;
    private Map<String, Worker> workers = new HashMap<>();

    @Activate
    protected void activate(final Map<String, Object> config) {
        this.opusScheme = PropertiesUtil.toString(config.get(OPUS_SCHEME), "");
        this.opusDomain = PropertiesUtil.toString(config.get(OPUS_DOMAIN), "");
        this.opusContext = PropertiesUtil.toString(config.get(OPUS_CONTEXT), "");
        this.name = PropertiesUtil.toString(config.get(GATEWAY_NAME), "");
        this.opusUsername = PropertiesUtil.toString(config.get(OPUS_USERNAME), "");
        this.opusPassword = PropertiesUtil.toString(config.get(OPUS_PASSWORD), "");
        this.mappings = PropertiesUtil.toStringArray(config.get(MAPPINGS));
    }

    protected String buildUrl(String path) {
        return new StringBuilder().append(this.opusScheme).append("://").append(this.opusDomain).append(this.opusContext).append(path).toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected EndpointConnector getEndpointConnector() {
        return endpointConnector;
    }

    @Override
    protected Message getMessage(GatewayRequest gatewayRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + Base64.getEncoder().encodeToString((this.opusUsername + ":" + this.opusPassword).getBytes()));

        Optional<Worker> worker = getWorker(gatewayRequest.getConnectorRequest());
        if (worker.isPresent()) {
            return new RestMessage(resolveUrl(worker.get(), gatewayRequest.getConnectorRequest()), headers);
        }

        return null;
    }

    @Override
    protected ConnectorResponse makeConnectorResponse(EndpointResponse endpointResponse, GatewayRequest gatewayRequest) {
        HttpEndpointResponse httpEndpointResponse = (HttpEndpointResponse) endpointResponse;
        OpusResponse response = null;
        if (endpointResponse.isSuccessful()) {
            Optional<Worker> worker = getWorker(gatewayRequest.getConnectorRequest());
            if (worker.isPresent()) {
                response = (OpusResponse) worker.get().getProcessor().process(((HttpResponse) endpointResponse.getResponse()).getData(), gatewayRequest.getConnectorRequest(), worker.get().getModelClass());
            } else {
                logger.error("No config found for request " + gatewayRequest.getConnectorRequest().getClass().getName());
                response = OpusResponse.makeNoResponse();
            }
        } else {
            logger.error("Error when requesting OPUS " + httpEndpointResponse.getResponse().toString());
            response = OpusResponse.makeNoResponse();
        }
        response.setMessage(httpEndpointResponse.getResponse().getMessage());
        response.setStatus(httpEndpointResponse.getResponse().getStatus());
        return response;
    }

    private Optional<Worker> getWorker(ConnectorRequest req) {
        return Stream.of(mappings).map(elem -> elem.split(":")).filter(s -> s.length == 4 && ConnectorUtils.getClassHierarchy(req).contains(s[0])).map(s -> buildWorker(s)).findFirst();
    }

    private Worker buildWorker(String[] configuration) {
        return new Worker(buildUrl(configuration[1]), processors.get(configuration[2]), configuration[3]);
    }


    private String resolveUrl(Worker worker, ConnectorRequest req) {
        MessageFormat messageFormat = new MessageFormat(worker.getUrl());
        return messageFormat.format(((OpusRequest) req).getParameters());
    }

}
