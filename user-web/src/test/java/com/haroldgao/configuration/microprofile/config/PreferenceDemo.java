package com.haroldgao.configuration.microprofile.config;

import com.haroldgao.projects.log.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferenceDemo {

    public static void main(String[] args) throws Throwable {
        testConfig();
        // testPreferences();
        // testSystemProperties();
        // testOSEnvironmentVariables();
    }

    private static void testSystemProperties() {
        System.getProperties().forEach((k,v) -> Logger.info(k + " = " + v));
    }

    private static void testOSEnvironmentVariables() {
        Logger.highlight("OSEnvironmentVariables");
        System.getenv().forEach((k, v) -> Logger.info(k + " = " + v));
    }

    private static void testPreferences() throws BackingStoreException {
        Preferences preferences = Preferences.userRoot();
        for (String key : preferences.keys()) {
            Logger.info(key);
            Logger.info(preferences.get(key, "null"));
        }
    }

    /**
     * {@link DefaultConfigProviderResolver} use {@link DefaultConfigBuilder#build()} to build
     * the {@link DefaultConfig}. Provides all property in one config.
     */
    private static void testConfig() {
        Config config = ConfigProvider.getConfig();
        Logger.info(config);
        config.getPropertyNames().forEach(name -> {
            Logger.info(name + "=" + config.getValue(name, String.class));
        });
    }
}
