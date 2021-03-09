package com.haroldgao.projects.user.context;

import com.haroldgao.projects.function.ThrowableFunction;
import com.haroldgao.projects.log.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * Web 应用全局上下文
 */
public class ComponentContext {
    public static final String CONTEXT_NAME = ComponentContext.class.getName();
    private static ServletContext servletContext;
    private ClassLoader classLoader;
    private Map<String, Object> componentsMap = new HashMap<>();

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
        ComponentContext.servletContext = servletContext;
        this.classLoader = servletContext.getClassLoader();

        findComponents();
        initComponents();
    }

    /**
     * Invoke {@link PostConstruct } method after {@link Resource}
     * annotation filed injected.
     *
     * @param component
     * @param componentClass
     * @see #injectComponents(Object, Class)
     */
    private void postConstruct(Object component, Class<?> componentClass) {
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

    private void initComponents() {
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();

            injectComponents(component, componentClass);
            postConstruct(component, componentClass);
        });
    }

    /**
     * Inject component class's fields with annotation {@link Resource}
     *
     * @param component
     * @param componentClass
     */
    private void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) &&
                            field.isAnnotationPresent(Resource.class);
                }).forEach(field -> {
                    String resourceName = field.getAnnotation(Resource.class).name();
                    Object injectedComponent = lookupComponent(resourceName);
                    field.setAccessible(true);
                    try {
                        field.set(component, injectedComponent);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

        );
    }

    private void findComponents() {
        List<String> componentNames = listAllComponentNames();
        Logger.info(componentNames.toString());

        componentNames.forEach(name -> componentsMap.put(name, lookupComponent(name)));
    }

    private <C> C lookupComponent(String componentName) {
        return executeInContext(context -> (C) context.lookup(componentName));
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        C component = null;
        try {
            component = (C) this.context.lookup(name);
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

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    protected List<String> listComponentNames(String path) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(path), true);

            if (e == null) {
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair pair = e.nextElement();
                String className = pair.getClassName();
                Class<?> loadClass = this.classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(loadClass)) {
                    // "bean"这样的目录的属于 Context.class，递归查找
                    fullNames.addAll(listComponentNames(pair.getName()));
                } else {
                    String fullName = path.startsWith("/") ?
                            path : path + "/" + pair.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    /**
     * 在 Context 中执行 {@link ThrowableFunction} 并返回其结果
     *
     * @param function
     * @param <R>      返回结果的类型
     * @return
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    /**
     * 在 Context 中执行 {@link ThrowableFunction} 并返回其结果
     *
     * @param function
     * @param ignoreException 是否忽略异常，false 时可能抛出 {@link RuntimeException}
     * @param <R>             返回结果的类型
     * @return
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoreException) {
        return executeInContext(this.context, function, ignoreException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function,
                                   boolean ignoreException) {
        R result = null;
        try {
            result = function.apply(context);
        } catch (Throwable throwable) {
            if (ignoreException) {
                Logger.warning(throwable.getMessage());
            } else {
                throw new RuntimeException(throwable);
            }
        }
        return result;
    }
}
