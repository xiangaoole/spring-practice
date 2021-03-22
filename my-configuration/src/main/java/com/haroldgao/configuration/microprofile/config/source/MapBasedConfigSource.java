package com.haroldgao.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class MapBasedConfigSource implements ConfigSource {

    private final int ordinal;

    private final String name;

    private final Map<String, String> source;

    protected MapBasedConfigSource(String name, int ordinal) {
        this.ordinal = ordinal;
        this.name = name;
        this.source = getProperties();
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> configData = new HashMap<>();
        try {
            prepareConfigData(configData);
        } catch (Throwable throwable) {
            throw new IllegalStateException("准备配置数据发生错误", throwable);
        }
        return configData;
    }

    protected abstract void prepareConfigData(Map<String, String> configData) throws Throwable;

    @Override
    public Set<String> getPropertyNames() {
        return source.keySet();
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String getValue(String s) {
        return source.get(s);
    }

    @Override
    public String getName() {
        return name;
    }
}
