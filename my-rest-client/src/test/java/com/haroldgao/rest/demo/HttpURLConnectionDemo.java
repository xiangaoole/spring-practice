package com.haroldgao.rest.demo;

import com.haroldgao.log.Logger;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpURLConnectionDemo {
    public static void main(String[] args) throws Throwable {
        URI uri = URI.create("http://localhost:8080/hello/world");
        URL url = uri.toURL();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        try (InputStream inputStream = urlConnection.getInputStream()) {
            String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Logger.info(body);
        }
        urlConnection.disconnect();
    }
}
