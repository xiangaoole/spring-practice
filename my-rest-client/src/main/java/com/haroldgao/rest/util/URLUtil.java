package com.haroldgao.rest.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public interface URLUtil {
    String DEFAULT_ENCODING = System.getProperty("com.haroldgao.url.encoding");

    String AND = "&";

    String EQUAL = "=";

    String TEMPLATE_VARIABLE_START = "{";

    String TEMPLATE_VARIABLE_END = "}";

    static String encode(String content) {
        return encode(content, DEFAULT_ENCODING);
    }

    static String encode(String content, String encoding) {
        String encodedContent = null;
        try {
            encodedContent = URLEncoder.encode(content, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
        return encodedContent;
    }

    static Map<String, ?> encodeSlash(Map<String, ?> templateValues, boolean encodeSlashInPath) {
        if (!encodeSlashInPath) {
            return templateValues;
        }

        Map<String, Object> encodedSlashTemplateValues = new HashMap<>();
        for (Map.Entry<String, ?> entry : templateValues.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                String str = (String) value;
                value = StringUtils.replace(str, PathUtil.SLASH, PathUtil.ENCODED_SLASH);
            }
            encodedSlashTemplateValues.put(name, value);
        }
        return encodedSlashTemplateValues;
    }

}
