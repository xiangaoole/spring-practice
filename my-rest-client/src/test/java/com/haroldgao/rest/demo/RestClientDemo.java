package com.haroldgao.rest.demo;

import com.haroldgao.log.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.net.URI;

public class RestClientDemo {
    public static void main(String[] args) {
        testPost();
    }

    private static void testGet() {
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

    private static void testPost() {
        URI uri = URI.create("http://localhost:8080/RegisterServlet");
        // META-INF/services/javax.ws.rs.client.ClientBuilder for ClientBuilder
        // META-INF/services/javax.ws.rs.ext.RuntimeDelegate for UriBuilder
        Client client = ClientBuilder.newClient();

        Response response = client.target(uri) // WebTarget
                .request() // Invocation Builder
                .post(buildUserInfoEntity());
        String entity = response.readEntity(String.class);
        Logger.info(entity);
    }

    private static Entity<Form> buildUserInfoEntity() {
        Form form = new Form();
        form.param("username", "Harold Gao");
        form.param("password", "123456");
        form.param("phone_number", "12345678901");
        form.param("email", "haroldgao@github.com");
        return Entity.form(form);
    }
}
