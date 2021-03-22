package com.haroldgao.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.*;
import java.util.stream.Stream;

public class ConfigSources implements Iterable<ConfigSource> {

    private boolean addedDefaultConfigSources;

    private boolean addedDiscoveredConfigSources;

    private final List<ConfigSource> configSources = new LinkedList<>();

    private ClassLoader classLoader;

    public ConfigSources(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void addDefaultConfigSources() {
        if (addedDefaultConfigSources) {
            return;
        }
        addConfigSources(JavaSystemPropertiesConfigSource.class,
                OSEnvironmentVariablesConfigSource.class,
                DefaultResourceConfigSource.class);

        addedDefaultConfigSources = true;
    }

    public void addDiscoveredConfigSource() {
        if (addedDiscoveredConfigSources) {
            return;
        }
        addConfigSources(ServiceLoader.load(ConfigSource.class, classLoader));
        addedDiscoveredConfigSources = true;
    }

    private void addConfigSources(Class<? extends ConfigSource>... configSources) {
        addConfigSources(Stream.of(configSources).map(this::newInstance).toArray(ConfigSource[]::new));
    }

    public void addConfigSources(ConfigSource... configSources) {
        addConfigSources(Arrays.asList(configSources));
    }

    private void addConfigSources(Iterable<ConfigSource> configSources) {
        configSources.forEach(this.configSources::add);
        Collections.sort(this.configSources, ConfigSourceOrdinalComparator.getInstance());
    }

    private ConfigSource newInstance(Class<? extends ConfigSource> configSourceClass) {
        ConfigSource instance = null;
        try {
            instance = configSourceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        return instance;
    }

    @Override
    public Iterator<ConfigSource> iterator() {
        return configSources.iterator();
    }

    public boolean isAddedDefaultConfigSources() {
        return addedDefaultConfigSources;
    }

    public boolean isAddedDiscoveredConfigSources() {
        return addedDiscoveredConfigSources;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
