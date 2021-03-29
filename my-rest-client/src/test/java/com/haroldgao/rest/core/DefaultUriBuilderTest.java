package com.haroldgao.rest.core;

import com.haroldgao.rest.util.Maps;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.Map;

import static org.junit.Assert.*;

public class DefaultUriBuilderTest {

    @Test
    public void testBuildFromMap() {
        UriBuilder uriBuilder = new DefaultUriBuilder()
                .scheme("http")
                .host("127.0.0.1")
                .port(8080)
                .path("/{a}/{b}/{c}")
                .queryParam("x", "m")
                .queryParam("y", "n", "o")
                .fragment("{d}")
                .resolveTemplates(Maps.of("a", 1, "b", 2, "c", 3, "d", 4));
        //Map<String, Object> values = Maps.of("a", 1, "b", 2, "c", 3, "d", 4);
        URI uri = uriBuilder.build();

        assertEquals("http", uri.getScheme());
        assertEquals("127.0.0.1", uri.getHost());
        assertEquals(8080, uri.getPort());
        assertEquals("/1/2/3", uri.getPath());
        assertEquals("x=m&y=n&y=o", uri.getRawQuery());
        assertEquals("4", uri.getFragment());

        assertEquals("http://127.0.0.1:8080/1/2/3?x=m&y=n&y=o#4", uri.toString());
    }
}