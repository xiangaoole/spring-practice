package com.haroldgao.projects.user.web.listener;

import com.haroldgao.projects.log.Logger;
import com.haroldgao.projects.user.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 监听全局上下文
 */
public class ComponentContextInitializerListener implements ServletContextListener {
    private ServletContext servletContext;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Logger.info("contextInitialized");
        this.servletContext = sce.getServletContext();
        new ComponentContext().init(this.servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Logger.info("contextDestroyed");
    }
}
