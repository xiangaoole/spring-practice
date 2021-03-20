package com.haroldgao.configuration.microprofile.config.converter;

public class DoubleConverter extends AbstractConverter<Double> {

    @Override
    protected Double doConvert(String s) {
        return Double.valueOf(s);
    }
}
