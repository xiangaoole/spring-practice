package com.haroldgao.configuration.microprofile.config.converter;

public class ShortConverter extends AbstractConverter<Short> {

    @Override
    protected Short doConvert(String s) {
        return Short.valueOf(s);
    }

}
