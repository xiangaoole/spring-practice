package com.haroldgao.configuration.microprofile.config.converter;

public class BooleanConverter extends AbstractConverter<Boolean> {

    @Override
    protected Boolean doConvert(String s) {
        return Boolean.valueOf(s);
    }
}
