package com.haroldgao.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

    private final ConcurrentMap<ClassLoader, Config> configsRepository = new ConcurrentHashMap<>();

    @Override
    public Config getConfig() {
        return getConfig(null);
    }

    @Override
    public Config getConfig(ClassLoader classLoader) {
        return configsRepository.computeIfAbsent(resolveClassLoader(classLoader), this::newConfig);
    }

    private Config newConfig(ClassLoader classLoader) {
        return newConfigBuilder(classLoader).build();
    }

    @Override
    public ConfigBuilder getBuilder() {
        return newConfigBuilder(null);
    }

    private ConfigBuilder newConfigBuilder(ClassLoader classLoader) {
        return new DefaultConfigBuilder(resolveClassLoader(classLoader));
    }

    private ClassLoader resolveClassLoader(ClassLoader classLoader) {
        return classLoader == null ? getClass().getClassLoader() : classLoader;
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        configsRepository.put(classLoader, config);
    }

    @Override
    public void releaseConfig(Config config) {
        List<ClassLoader> targetKeys = new LinkedList<>();
        for (Map.Entry<ClassLoader, Config> entry : configsRepository.entrySet()) {
            if (Objects.equals(config, entry.getValue())) {
                targetKeys.add(entry.getKey());
            }
        }

        targetKeys.forEach(configsRepository::remove);
    }
}
