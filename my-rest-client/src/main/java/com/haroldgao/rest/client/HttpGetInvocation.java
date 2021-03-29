package com.haroldgao.rest.client;

import com.haroldgao.rest.core.DefaultResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

public class HttpGetInvocation implements Invocation {

    private final URI uri;

    private final URL url;

    private final MultivaluedMap<String, Object> headers;

    public HttpGetInvocation(URI uri, MultivaluedMap<String, Object> headers) {
        this.uri = uri;
        this.headers = headers;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Invocation property(String name, Object value) {
        return this;
    }

    @Override
    public Response invoke() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.GET);
            setRequestHeads(connection);
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
