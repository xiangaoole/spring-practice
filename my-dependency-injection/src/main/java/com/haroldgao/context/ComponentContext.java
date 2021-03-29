package com.haroldgao.context;

import java.util.List;

/**
 * Context of Components
 */
public interface ComponentContext {

    void init();

    void destroy();

    /**
     * Get component from name
     *
     * @param name      Component name
     * @param <C>
     * @return          return <code>null</code> if not found
     */
    <C> C getComponent(String name);

    /**
     * Get all components' names
     *
     * @return
     */
    List<String> getComponentNames();

}
