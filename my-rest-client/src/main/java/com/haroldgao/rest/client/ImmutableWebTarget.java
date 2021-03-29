package com.haroldgao.rest.client;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

public class ImmutableWebTarget implements WebTarget {

    private final UriBuilder uriBuilder;

    public ImmutableWebTarget(UriBuilder uriBuilder) {
        this.uriBuilder = uriBuilder.clone();
    }

    protected ImmutableWebTarget newWebTarget() {
        return new ImmutableWebTarget(uriBuilder);
    }

    @Override
    public URI getUri() {
        return uriBuilder.build();
    }

    @Override
    public UriBuilder getUriBuilder() {
        return uriBuilder;
    }

    @Override
    public WebTarget path(String path) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().path(path);
        return target;
    }

    @Override
    public WebTarget resolveTemplate(String name, Object value) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().resolveTemplate(name, value);
        return target;
    }

    @Override
    public WebTarget resolveTemplate(String name, Object value, boolean encodeSlashInPath) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().resolveTemplate(name, value, encodeSlashInPath);
        return target;
    }

    @Override
    public WebTarget resolveTemplateFromEncoded(String name, Object value) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().resolveTemplateFromEncoded(name, value);
        return target;
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> templateValues) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().resolveTemplates(templateValues);
        return target;
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().resolveTemplates(templateValues, encodeSlashInPath);
        return target;
    }

    @Override
    public WebTarget resolveTemplatesFromEncoded(Map<String, Object> templateValues) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().resolveTemplatesFromEncoded(templateValues);
        return target;
    }

    @Override
    public WebTarget matrixParam(String name, Object... values) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().matrixParam(name, values);
        return target;
    }

    @Override
    public WebTarget queryParam(String name, Object... values) {
        WebTarget target = newWebTarget();
        target.getUriBuilder().queryParam(name, values);
        return target;
    }

    @Override
    public Invocation.Builder request() {
        return new DefaultInvocationBuilder(uriBuilder);
    }

    @Override
    public Invocation.Builder request(String... acceptedResponseTypes) {
        return null;
    }

    @Override
    public Invocation.Builder request(MediaType... acceptedResponseTypes) {
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public WebTarget property(String name, Object value) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass, int priority) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass, Class<?>... contracts) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
        return null;
    }

    @Override
    public WebTarget register(Object component) {
        return null;
    }

    @Override
    public WebTarget register(Object component, int priority) {
        return null;
    }

    @Override
    public WebTarget register(Object component, Class<?>... contracts) {
        return null;
    }

    @Override
    public WebTarget register(Object component, Map<Class<?>, Integer> contracts) {
        return null;
    }
}
