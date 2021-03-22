package com.haroldgao.configuration.microprofile.config.converter;

public class LongConverter extends AbstractConverter<Long> {

    @Override
    protected Long doConvert(String s) {
        return Long.valueOf(s);
    }

}
