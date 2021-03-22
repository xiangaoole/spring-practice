package com.haroldgao.configuration.microprofile.config.converter;

public class FloatConverter extends AbstractConverter<Float> {
    @Override
    protected Float doConvert(String s) {
        return Float.valueOf(s);
    }
}
