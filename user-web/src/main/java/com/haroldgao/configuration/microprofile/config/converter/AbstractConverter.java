package com.haroldgao.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public abstract class AbstractConverter<T> implements Converter<T> {

    @Override
    public T convert(String s) throws IllegalArgumentException, NullPointerException {
        if (s == null) {
            throw new NullPointerException("The value must not be null");
        }
        return doConvert(s);
    }

    protected abstract T doConvert(String s);
}
