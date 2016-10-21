package com.adeo.connector.opus.gateway.processors;


/**
 * Created by stievena on 29/09/16.
 */
public class Worker {
    private String url;
    private Processor processor;
    private Class modelClass;

    public Worker(String url, Processor processor, Class modelClass) {
        this.url = url;
        this.processor = processor;
        this.modelClass = modelClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Processor getProcessor() {
        return processor;
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }
}
