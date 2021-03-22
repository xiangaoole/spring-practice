package com.haroldgao.configuration.microprofile.config;

import com.haroldgao.configuration.microprofile.config.converter.Converters;
import com.haroldgao.configuration.microprofile.config.source.ConfigSources;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

public class DefaultConfigBuilder implements ConfigBuilder {

    private final ConfigSources configSources;

    private final Converters converters;

    public DefaultConfigBuilder(ConfigSources configSources, Converters converters) {
        this.configSources = configSources;
        this.converters = converters;
    }

    public DefaultConfigBuilder(ClassLoader classLoader) {
        configSources = new ConfigSources(classLoader);
        converters = new Converters(classLoader);
    }

    @Override
    public ConfigBuilder addDefaultSources() {
        configSources.addDefaultConfigSources();
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredSources() {
        configSources.addDiscoveredConfigSource();
        return this;
    }

    @Override
    public ConfigBuilder addDiscoveredConverters() {
        converters.addDiscoveredConverters();
        return null;
    }

    @Override
    public ConfigBuilder forClassLoader(ClassLoader classLoader) {
        configSources.setClassLoader(classLoader);
        converters.setClassLoader(classLoader);
        return this;
    }

    @Override
    public ConfigBuilder withSources(ConfigSource... configSources) {
        this.configSources.addConfigSources(configSources);
        return this;
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        this.converters.addConverters(converters);
        return this;
    }

    @Override
    public <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter) {
        converters.addConverter(converter, priority, type);
        return null;
    }

    @Override
    public Config build() {
        return new DefaultConfig(configSources, converters);
    }
}
