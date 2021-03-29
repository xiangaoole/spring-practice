package com.haroldgao.rest.core;

import com.haroldgao.rest.util.PathUtil;
import com.haroldgao.rest.util.URLUtil;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Stream;

import static com.haroldgao.rest.util.PathUtil.SLASH;
import static com.haroldgao.rest.util.URLUtil.AND;
import static com.haroldgao.rest.util.URLUtil.EQUAL;

public class DefaultUriBuilder extends UriBuilder {

    private String scheme;
    private String host;
    private int port;
    private String path;
    private String fragment;
    private String schemeSpecificPart;
    private String userInfo;
    private String uriTemplate;
    private String resolvedTemplate;

    private MultivaluedMap<String, String> queryParams = new MultivaluedHashMap<>();
    private MultivaluedMap<String, String> matrixParams = new MultivaluedHashMap<>();

    public DefaultUriBuilder() {
    }

    protected DefaultUriBuilder(DefaultUriBuilder other) {
        this.scheme = other.scheme;
        this.schemeSpecificPart = other.schemeSpecificPart;
        this.userInfo = other.userInfo;
        this.host = other.host;
        this.port = other.port;
        this.path = other.path;
        this.fragment = other.fragment;
        this.uriTemplate = other.uriTemplate;
        this.queryParams.putAll(other.queryParams);
        this.matrixParams.putAll(other.matrixParams);
    }

    @Override
    public UriBuilder clone() {
        return new DefaultUriBuilder(this);
    }

    @Override
    public UriBuilder uri(URI uri) {
        this.scheme = uri.getScheme();
        this.schemeSpecificPart = uri.getSchemeSpecificPart();
        this.userInfo = uri.getUserInfo();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.fragment = uri.getFragment();
        String rawQuery = uri.getRawQuery();
        this.queryParams.clear();
        this.matrixParams.putAll(resolveParams(rawQuery));
        return this;
    }

    @Override
    public UriBuilder uri(String uriTemplate) {
        this.uriTemplate = uriTemplate;
        return this;
    }

    @Override
    public UriBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    @Override
    public UriBuilder schemeSpecificPart(String ssp) {
        this.schemeSpecificPart = ssp;
        return this;
    }

    @Override
    public UriBuilder userInfo(String ui) {
        this.userInfo = ui;
        return this;
    }

    @Override
    public UriBuilder host(String host) {
        this.host = host;
        return this;
    }

    @Override
    public UriBuilder port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public UriBuilder replacePath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public UriBuilder path(String path) {
        this.path = buildPath(this.path, path);
        return this;
    }

    @Override
    public UriBuilder path(Class resource) {
        return path(PathUtil.resolvePath(resource));
    }


    @Override
    public UriBuilder path(Class resource, String method) {
        return path(PathUtil.resolvePath(resource, method));
    }


    @Override
    public UriBuilder path(Method method) {
        return path(PathUtil.resolvePath(method.getDeclaringClass(), method));
    }

    @Override
    public UriBuilder segment(String... segments) {
        path = buildPath(path, segments);
        return this;
    }

    @Override
    public UriBuilder replaceMatrix(String matrix) {
        return null;
    }

    @Override
    public UriBuilder matrixParam(String name, Object... values) {
        matrixParams.put(name, asList(of(values)));
        return this;
    }

    @Override
    public UriBuilder replaceMatrixParam(String name, Object... values) {
        return null;
    }

    @Override
    public UriBuilder replaceQuery(String query) {
        return null;
    }

    @Override
    public UriBuilder queryParam(String name, Object... values) {
        queryParams.put(name, asList(of(values)));
        return this;
    }

    @Override
    public UriBuilder replaceQueryParam(String name, Object... values) {
        return null;
    }

    @Override
    public UriBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    @Override
    public UriBuilder resolveTemplate(String name, Object value) {
        return resolveTemplate(name, value, false);
    }

    @Override
    public UriBuilder resolveTemplate(String name, Object value, boolean encodeSlashInPath) {
        return resolveTemplates(Collections.singletonMap(name, value), encodeSlashInPath);
    }

    @Override
    public UriBuilder resolveTemplateFromEncoded(String name, Object value) {
        return resolveTemplate(name, value, false);
    }

    @Override
    public UriBuilder resolveTemplates(Map<String, Object> templateValues) {
        return resolveTemplates(templateValues, false);
    }

    @Override
    public UriBuilder resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath) throws IllegalArgumentException {
        return doResolveTemplates(URLUtil.encodeSlash(templateValues, encodeSlashInPath), false);
    }

    @Override
    public UriBuilder resolveTemplatesFromEncoded(Map<String, Object> templateValues) {
        return resolveTemplates(templateValues, false);
    }

    @Override
    public URI buildFromMap(Map<String, ?> values) {
        return buildFromMap(values, false);
    }

    @Override
    public URI buildFromMap(Map<String, ?> values, boolean encodeSlashInPath) throws IllegalArgumentException, UriBuilderException {
        return doBuild(values, false);
    }

    @Override
    public URI buildFromEncodedMap(Map<String, ?> values) throws IllegalArgumentException, UriBuilderException {
        return doBuild(values, true);
    }

    @Override
    public URI build(Object... values) throws IllegalArgumentException, UriBuilderException {
        return build(values, false);
    }

    @Override
    public URI build(Object[] values, boolean encodeSlashInPath) throws IllegalArgumentException, UriBuilderException {
        return buildFromMap(toTemplateVariables(uriTemplate, values));
    }

    @Override
    public URI buildFromEncoded(Object... values) throws IllegalArgumentException, UriBuilderException {
        return buildFromEncodedMap(toTemplateVariables(uriTemplate, values));
    }

    @Override
    public String toTemplate() {
        return uriTemplate;
    }


    private Map<String, ? extends List<String>> resolveParams(String rawQuery) {
        if (StringUtils.isNotBlank(rawQuery)) {
            Map<String, List<String>> paramsMap = new LinkedHashMap<>();
            String[] queryParams = StringUtils.split(rawQuery, AND);
            if (queryParams != null) {
                for (String queryParam : queryParams) {
                    String[] paramNameAndValue = StringUtils.split(queryParam, EQUAL);
                    if (paramNameAndValue != null) {
                        String paramName = paramNameAndValue[0];
                        String paramValue = paramNameAndValue[1];
                        List<String> paramValues = paramsMap.computeIfAbsent(paramName, k -> new LinkedList<>());
                        paramValues.add(paramValue);
                    }
                }
            }
            return Collections.unmodifiableMap(paramsMap);
        }
        return Collections.emptyMap();
    }

    protected URI doBuild(Map<String, ?> values, boolean encoded) {
        doResolveTemplates(values, encoded);
        final URI uri;
        if (resolvedTemplate != null) {
            uri = URI.create(resolvedTemplate);
        } else {
            uri = toUri();
        }
        return uri;
    }

    private URI toUri() {
        URI uri = null;
        try {
            if (schemeSpecificPart != null) {
                uri = new URI(scheme, schemeSpecificPart, fragment);
            } else {
                uri = new URI(scheme, userInfo, host, port, path, toQueryString(queryParams), fragment);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private String toQueryString(MultivaluedMap<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return null;
        }

        StringBuilder queryStringBuilder = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                queryStringBuilder.append(name).append(EQUAL).append(value).append(AND);
            }
        }

        // remove the last AND
        return queryStringBuilder.substring(0, queryStringBuilder.length() - 1);
    }

    protected UriBuilder doResolveTemplates(Map<String, ?> templateValues, boolean encoded) {
        this.scheme = resolveVariables(this.scheme, templateValues, encoded);
        this.userInfo = resolveVariables(this.userInfo, templateValues, encoded);
        this.host = resolveVariables(this.host, templateValues, encoded);
        this.path = resolveVariables(this.path, templateValues, encoded);
        this.fragment = resolveVariables(this.fragment, templateValues, encoded);
        this.queryParams = resolveParams(this.queryParams, templateValues, encoded);
        this.matrixParams = resolveParams(this.matrixParams, templateValues, encoded);
        this.resolvedTemplate = resolvedTemplate == null ?
                resolveVariables(uriTemplate, templateValues, encoded) :
                resolveVariables(resolvedTemplate, templateValues, encoded);
        return this;
    }

    private MultivaluedMap<String, String> resolveParams(MultivaluedMap<String, String> params,
                                                         Map<String, ?> templateValues, boolean encoded) {
        MultivaluedMap<String, String> resolvedParams = new MultivaluedHashMap<>();

        for (Map.Entry<String, List<String>> param : params.entrySet()) {
            String paramName = param.getKey();
            String resolvedName = resolveVariables(paramName, templateValues, encoded);

            for (String paramValue : param.getValue()) {
                resolvedParams.add(resolvedName, resolveVariables(paramValue, templateValues, encoded));
            }
        }

        return resolvedParams;
    }

    private Map<String, ?> toTemplateVariables(String scheme, Object... templateValues) {
        if (StringUtils.isBlank(scheme)) {
            return Collections.emptyMap();
        }

        Map<String, Object> templateVariables = new LinkedHashMap<>();
        int start = 0, end = 0;
        int index = 0;
        while (true) {
            start = scheme.indexOf(URLUtil.TEMPLATE_VARIABLE_START, end);
            end = scheme.indexOf(URLUtil.TEMPLATE_VARIABLE_END, start);

            if (start == -1 || end == -1) {
                break;
            }

            String variableName = scheme.substring(start + 1, end);
            if (variableName == null) {
                continue;
            }
            if (!templateVariables.containsKey(variableName)) {
                Object templateValue = index < templateValues.length ? templateValues[index++] : null;
                templateVariables.put(variableName, templateValue);
            }
        }

        return Collections.unmodifiableMap(templateVariables);
    }

    private String resolveVariables(String template, Object[] templateValues, boolean encoded) {
        return resolveVariables(template, toTemplateVariables(template, templateValues), encoded);
    }

    /**
     * for scheme "/{a}/{b}", templateValues "{a:1, b:2}", return "/1/2"
     *
     * @param template
     * @param templateValues
     * @param encoded
     * @return
     */
    private String resolveVariables(String template, Map<String, ?> templateValues, boolean encoded) {
        if (StringUtils.isBlank(template)) {
            return null;
        }

        if (templateValues == null || templateValues.isEmpty()) {
            return template;
        }

        int start = 0, end = 0;
        StringBuilder resolvedScheme = new StringBuilder(template);

        while (true) {
            start = resolvedScheme.indexOf(URLUtil.TEMPLATE_VARIABLE_START, end);
            end = resolvedScheme.indexOf(URLUtil.TEMPLATE_VARIABLE_END, start);

            if (start == -1 || end == -1) {
                break;
            }

            String variableName = resolvedScheme.substring(start + 1, end);
            if (variableName == null) {
                continue;
            }

            Object value = templateValues.get(variableName);
            if (value == null) {
                continue;
            }

            String variableValue = String.valueOf(value);
            if (encoded) {
                variableValue = URLUtil.encode(variableValue);
            }

            resolvedScheme.replace(start, end + 1, variableValue);
        }

        return resolvedScheme.toString();
    }

    private String[] of(Object... values) {
        return Stream.of(values).toArray(String[]::new);
    }

    private List<String> asList(String[] values) {
        return Arrays.asList(values);
    }

    private String buildPath(String path, String... segments) {
        StringBuilder pathBuilder = new StringBuilder();
        if (path != null) {
            pathBuilder.append(path);
        }
        for (String segment : segments) {
            if (!segment.startsWith(SLASH)) {
                pathBuilder.append(SLASH);
            }
            pathBuilder.append(segment);
        }
        return pathBuilder.toString();
    }

}
