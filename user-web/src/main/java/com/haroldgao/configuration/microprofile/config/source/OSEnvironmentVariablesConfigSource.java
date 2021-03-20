package com.haroldgao.configuration.microprofile.config.source;

import java.util.Map;

public class OSEnvironmentVariablesConfigSource extends MapBasedConfigSource {
    public OSEnvironmentVariablesConfigSource() {
        super("OS Environment variables", 300);
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        configData.putAll(System.getenv());
    }
}
