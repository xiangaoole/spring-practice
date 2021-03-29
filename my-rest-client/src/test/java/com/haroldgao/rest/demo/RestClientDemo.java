package com.haroldgao.rest.demo;

import com.haroldgao.log.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;

public class RestClientDemo {
    public static void main(String[] args) {
        URI uri = URI.create("http://localhost:8080/");
        // META-INF/services/javax.ws.rs.client.ClientBuilder for ClientBuilder
        // META-INF/services/javax.ws.rs.ext.RuntimeDelegate for UriBuilder
        Client client = ClientBuilder.newClient();
        Response response = client.target(uri) // WebTarget
                .request() // Invocation Builder
                .get();
        String entity = response.readEntity(String.class);
        Logger.info(entity);
    }
}
