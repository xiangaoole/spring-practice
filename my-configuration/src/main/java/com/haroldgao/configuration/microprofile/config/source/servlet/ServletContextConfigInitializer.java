package com.haroldgao.configuration.microprofile.config.source.servlet;

import com.haroldgao.configuration.microprofile.config.DefaultConfigProviderResolver;
import com.haroldgao.log.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextConfigInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ClassLoader classLoader = servletContext.getClassLoader();
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();

        ConfigBuilder builder = configProviderResolver.getBuilder();
        builder.forClassLoader(classLoader);
        builder.addDefaultSources();
        builder.addDiscoveredSources();
        builder.addDiscoveredConverters();
        builder.withSources(new ServletContextConfigSource(servletContext));
        Config config = builder.build();

        // register config to the ServletContext's classloader
        configProviderResolver.registerConfig(config, classLoader);
        test(servletContext);
    }

    private void test(ServletContext servletContext) {
        Config config = ConfigProvider.getConfig(servletContext.getClassLoader());

        config.getPropertyNames().forEach(name -> {
            String configItem = name + "=" + config.getValue(name, String.class);
            Logger.info(configItem);
        });

    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
