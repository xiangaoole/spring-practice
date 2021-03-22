package com.haroldgao.di;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.stream.Stream;

public class DependencyInjection {

    private final Map<String, Object> componentsMap;

    /**
     *
     * @param componentsMap     All component objects
     */
    public DependencyInjection(Map<String, Object> componentsMap) {
        this.componentsMap = componentsMap;
    }

    public void inject() {
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();

            injectComponents(component, componentClass);
            processPostConstruct(component, componentClass);
            processPreDestroy();
        });
    }

    /**
     * Inject component class's fields annotated by {@link Resource}
     *
     * @param component             the object
     * @param componentClass        the Class of the object
     */
    private void injectComponents(final Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) &&
                            field.isAnnotationPresent(Resource.class);
                }).forEach(field -> {
                    String resourceName = field.getAnnotation(Resource.class).name();
                    Object injectedComponent = componentsMap.get(resourceName);
                    field.setAccessible(true);
                    try {
                        field.set(component, injectedComponent);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

        );
    }

    /**
     * Invoke methods annotated by {@link PostConstruct } of component
     * , after fields annotated by {@link Resource} are injected.
     *
     * @param component         the Class instance
     * @param componentClass    the Class
     * @see #injectComponents(Object, Class)
     */
    private void processPostConstruct(final Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredMethods())
                .filter(method -> {
                    int modifier = method.getModifiers();
                    return !Modifier.isStatic(modifier) && // not static
                            method.getParameterCount() == 0 && // no argument
                            method.isAnnotationPresent(PostConstruct.class);
                })
                .forEach(method -> {
                    try {
                        method.invoke(component);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Invoke methods annotated by {@link PreDestroy } before JVM destroy
     */
    private void processPreDestroy() {
        // TODO: 通过 ShutdownHook 实现
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("JVM shuts down...");
            // 逐一调用这
            // componentsMap.values();
        }));
    }
}
