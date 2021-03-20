package com.haroldgao.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class PrioritizedConverter<T> implements Converter<T>, Comparable<PrioritizedConverter<T>> {

    private final Converter<T> converter;

    private final int priority;

    public PrioritizedConverter(Converter<T> converter, int priority) {
        this.converter = converter;
        this.priority = priority;
    }

    @Override
    public int compareTo(PrioritizedConverter<T> o) {
        return Integer.compare(getPriority(), o.getPriority());
    }

    @Override
    public T convert(String s) throws IllegalArgumentException, NullPointerException {
        return converter.convert(s);
    }

    public Converter<T> getConverter() {
        return converter;
    }

    public int getPriority() {
        return priority;
    }

}
