package com.haroldgao.projects.user.context;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;

/**
 * Web 应用全局上下文
 */
public class ComponentContext {
    public static final String CONTEXT_NAME = ComponentContext.class.getName();
    private static ServletContext servletContext;

    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    private Context context;

    public void init(ServletContext servletContext) throws RuntimeException {
        try {
            this.context = (Context) new InitialContext().lookup("java:comp/env");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        servletContext.setAttribute(CONTEXT_NAME, this);
        this.servletContext = servletContext;
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <T>
     * @return
     */
    public <T> T getComponent(String name) {
        T component = null;
        try {
            component = (T) this.context.lookup(name);
        } catch (NamingException e) {
            throw new NoSuchElementException(name);
        }
        return component;
    }

    public void destroy() throws RuntimeException {
        if (this.context != null) {
            try {
                this.context.close();
            } catch (NamingException e) {
                new RuntimeException(e);
            }
        }
    }
}
