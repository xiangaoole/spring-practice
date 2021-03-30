package com.haroldgao.rest.client;

import com.haroldgao.rest.core.DefaultResponse;
import com.haroldgao.rest.util.URLUtil;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.haroldgao.rest.util.URLUtil.AND;
import static com.haroldgao.rest.util.URLUtil.EQUAL;

public class HttpPostInvocation implements Invocation {

    private final URI uri;

    private final URL url;

    private final MultivaluedMap<String, Object> headers;

    private final Entity<?> entity;

    public HttpPostInvocation(URI uri, MultivaluedMap<String, Object> headers, Entity<?> entity) {
        this.uri = uri;
        this.headers = headers;
        this.entity = entity;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Invocation property(String name, Object value) {
        return null;
    }

    @Override
    public Response invoke() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            setRequestHeads(connection);
            setRequestEntity(connection);
            // TODO: Set the cookie
            int statusCode = connection.getResponseCode();

            DefaultResponse response = new DefaultResponse();
            response.setConnection(connection);
            response.setStatus(statusCode);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setRequestHeads(HttpURLConnection urlConnection) {
        for (String headerName : headers.keySet()) {
            List<Object> headerValues = headers.get(headerName);
            for (Object headValue : headerValues) {
                urlConnection.addRequestProperty(headerName, headValue.toString());
            }
        }
    }

    private void setRequestEntity(HttpURLConnection urlConnection) {
        urlConnection.setDoOutput(true);
        try (OutputStream outputStream = urlConnection.getOutputStream()) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            if (entity.getMediaType() == MediaType.APPLICATION_FORM_URLENCODED_TYPE) {
                Form form = (Form) entity.getEntity();
                String paramsString = paramsToString(form.asMap());
                writer.write(paramsString);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String paramsToString(MultivaluedMap<String, String> params) {
        if (params == null || params.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                sb.append(name).append(EQUAL).append(value).append(AND);
            }
        }
        return sb.substring(0, sb.length() - 1); // remove the last AND
    }

    @Override
    public <T> T invoke(Class<T> responseType) {
        return invoke().readEntity(responseType);
    }

    @Override
    public <T> T invoke(GenericType<T> responseType) {
        return invoke().readEntity(responseType);
    }

    @Override
    public Future<Response> submit() {
        return null;
    }

    @Override
    public <T> Future<T> submit(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(GenericType<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(InvocationCallback<T> callback) {
        return null;
    }
}
