package com.haroldgao.configuration.microprofile.config.converter;

public class IntegerConverter extends AbstractConverter<Integer> {
    @Override
    protected Integer doConvert(String s) {
        return Integer.valueOf(s);
    }
}
