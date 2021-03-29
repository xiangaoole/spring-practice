package com.haroldgao.rest.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class Maps {
    public static Map of(Object... values) {
        Map map = new LinkedHashMap();
        int length = values.length;
        for (int i = 0; i < length; ) {
            map.put(values[i++], values[i++]);
        }
        return map;
    }
}
