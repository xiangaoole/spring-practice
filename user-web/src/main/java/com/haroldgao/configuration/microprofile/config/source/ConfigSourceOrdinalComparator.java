package com.haroldgao.configuration.microprofile.config.source;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Comparator;

/**
 * {@link ConfigSource} Ordinal Comparator
 */
public class ConfigSourceOrdinalComparator implements Comparator<ConfigSource> {

    private static final ConfigSourceOrdinalComparator INSTANCE = new ConfigSourceOrdinalComparator();

    public static ConfigSourceOrdinalComparator getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(ConfigSource o1, ConfigSource o2) {
        return Integer.compare(o1.getOrdinal(), o2.getOrdinal());
    }

}
