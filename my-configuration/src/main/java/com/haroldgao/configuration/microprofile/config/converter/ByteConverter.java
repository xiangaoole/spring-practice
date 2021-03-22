package com.haroldgao.configuration.microprofile.config.converter;

public class ByteConverter extends AbstractConverter<Byte> {

    @Override
    protected Byte doConvert(String s) {
        return Byte.valueOf(s);
    }
}
