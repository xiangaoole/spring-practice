package com.haroldgao.configuration.microprofile.config.source;

import com.haroldgao.log.Logger;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * A {@link ConfigSource} for each property file `META-INF/microprofile-config.properties`
 * found on the classpath
 */
public class DefaultResourceConfigSource extends MapBasedConfigSource {

    public static final String CONFIG_FILE_LOCATION = "META-INF/microprofile-config.properties";

    public DefaultResourceConfigSource() {
        super("Default Config File", 100);
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        URL resource = getClass().getClassLoader().getResource(CONFIG_FILE_LOCATION);
        if (resource == null) {
            Logger.warning("The default config file cannot be found in the classpath: "
                    + CONFIG_FILE_LOCATION);
            return;
        }
        try (InputStream inputStream = resource.openStream()){
            Properties properties = new Properties();
            properties.load(inputStream);
            configData.putAll(properties);
        }
    }
}
