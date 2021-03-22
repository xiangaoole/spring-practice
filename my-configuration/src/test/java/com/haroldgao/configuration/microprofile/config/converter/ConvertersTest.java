package com.haroldgao.configuration.microprofile.config.converter;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConvertersTest {

    private Converters converters;

    @Before
    public void setUp() {
        converters = new Converters();
    }

    @Test
    public void testResolveConvertedType() {
        assertEquals(Boolean.class, converters.resolveConvertedType(new BooleanConverter()));
        assertEquals(Byte.class, converters.resolveConvertedType(new ByteConverter()));
        assertEquals(Double.class, converters.resolveConvertedType(new DoubleConverter()));
        assertEquals(Float.class, converters.resolveConvertedType(new FloatConverter()));
        assertEquals(Integer.class, converters.resolveConvertedType(new IntegerConverter()));
        assertEquals(Long.class, converters.resolveConvertedType(new LongConverter()));
        assertEquals(Short.class, converters.resolveConvertedType(new ShortConverter()));
        assertEquals(String.class, converters.resolveConvertedType(new StringConverter()));
    }
}