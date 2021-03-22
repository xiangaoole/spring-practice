package com.haroldgao.configuration.microprofile.config;

import com.haroldgao.configuration.microprofile.config.converter.Converters;
import com.haroldgao.configuration.microprofile.config.source.ConfigSources;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DefaultConfig implements Config {

    private final ConfigSources configSources;

    private final Converters converters;

    public DefaultConfig(ConfigSources configSources, Converters converters) {
        this.configSources = configSources;
        this.converters = converters;
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        String propertyValue = getPropertyValue(propertyName);
        Converter<T> converter = getTypedConverter(propertyType);
        return converter == null ? null : converter.convert(propertyValue);
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return null;
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return StreamSupport.stream(configSources.spliterator(), false)
                .map(ConfigSource::getPropertyNames)
                .collect(LinkedHashSet::new, Set::addAll, Set::addAll);
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return configSources;
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        Converter<T> converter = getTypedConverter(forType);
        return converter == null ? Optional.empty() : Optional.of(converter);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }

    private <T> Converter<T> getTypedConverter(Class<T> forType) {
        List<Converter> converters = this.converters.getConverters(forType);
        return converters.isEmpty() ? null : converters.get(0);
    }

    private String getPropertyValue(String propertyName) {
        String propertyValue = null;
        for (ConfigSource source : configSources) {
            propertyValue = source.getValue(propertyName);
            if (propertyValue != null) {
                break;
            }
        }
        return propertyValue;
    }
}
