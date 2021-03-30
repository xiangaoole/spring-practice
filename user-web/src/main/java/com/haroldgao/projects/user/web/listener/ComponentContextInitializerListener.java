package com.haroldgao.projects.user.web.listener;

import com.haroldgao.context.ClassicComponentContext;
import com.haroldgao.log.Logger;
import com.haroldgao.projects.user.management.Author;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 * 监听全局上下文
 */
public class ComponentContextInitializerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logger.info("contextInitialized");
        ServletContext servletContext = sce.getServletContext();
        new ClassicComponentContext().init(servletContext);
        registerAuthorMXBean();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Logger.info("contextDestroyed");
        ClassicComponentContext.getInstance().destroy();
        unregisterAuthorMXBean();
    }

    // Test jolokia JMX agent
    // http://localhost:8080/jolokia/read/com.haroldgao.projects.user.management:type=Author
    private void registerAuthorMXBean() {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName objectName = new ObjectName("com.haroldgao.projects.user.management:type=Author");
            Author author = Author.getInstance();
            platformMBeanServer.registerMBean(author, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void unregisterAuthorMXBean() {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName objectName = new ObjectName("com.haroldgao.projects.user.management:type=Author");
            platformMBeanServer.unregisterMBean(objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
