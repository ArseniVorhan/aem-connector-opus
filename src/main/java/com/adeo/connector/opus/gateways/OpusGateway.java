package com.adeo.connector.opus.gateways;

import com.adobe.connector.gateways.ConnectorGateway;
import com.adobe.connector.gateways.ConnectorRequest;
import com.adobe.connector.gateways.http.Processor;
import com.adobe.connector.gateways.http.RestGateway;
import com.adobe.connector.gateways.http.Worker;
import okhttp3.Credentials;
import okhttp3.Headers;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Component(immediate = true, label = "Adeo OPUS Connector Gateway", description = "Gateway for communicating with OPUS back end", metatype = true)
@Service(value = ConnectorGateway.class)
@References({
        @Reference(name = "processor", referenceInterface = Processor.class, cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC)})
public class OpusGateway extends RestGateway {

    private final static Logger logger = LoggerFactory.getLogger(OpusGateway.class);

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

    @Property(label = "Request Mappings", description = "Map request type to OPUS url path. Each entry is of the form 'request\":\"urlPath:'", unbounded = PropertyUnbounded.ARRAY)
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

    private String buildUrl(String path) {
        return new StringBuilder().append(this.opusScheme).append("://").append(this.opusDomain).append(this.opusContext).append(path).toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    protected String resolveUrl(Worker worker, ConnectorRequest req) {
        MessageFormat messageFormat = new MessageFormat(worker.getUrl());
        return messageFormat.format(((OpusRequest) req).getParameters());
    }

    @Override
    protected Headers buildHttpHeaders() {
        String credential = Credentials.basic(this.opusUsername, this.opusPassword);
        return new Headers.Builder().add("Authorization", credential).build();
    }

    @Override
    protected Optional<Worker> getWorker(ConnectorRequest req) {
        return Stream.of(mappings).map(elem -> elem.split(":")).filter(s -> s.length == 3 && s[0].equalsIgnoreCase(req.getName())).map(s -> new Worker(buildUrl(s[1]), processors.get(s[2]))).findFirst();
    }

}
