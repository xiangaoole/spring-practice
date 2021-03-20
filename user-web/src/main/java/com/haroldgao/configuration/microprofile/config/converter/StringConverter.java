package com.haroldgao.configuration.microprofile.config.converter;

public class StringConverter extends AbstractConverter<String> {

    @Override
    protected String doConvert(String s) {
        return s;
    }
}
