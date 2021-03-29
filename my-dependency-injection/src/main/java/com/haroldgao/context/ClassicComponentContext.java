package com.haroldgao.context;

import com.haroldgao.di.DependencyInjection;
import com.haroldgao.function.ThrowableAction;
import com.haroldgao.function.ThrowableFunction;
import com.haroldgao.log.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Web 应用全局上下文
 */
public class ClassicComponentContext implements ComponentContext, ServletContainerInitializer {
    public static final String CONTEXT_NAME = ClassicComponentContext.class.getName();
    private static ServletContext servletContext;

    private ClassLoader classLoader;
    private Context envContext;
    private final Map<String, Object> componentsMap = new HashMap<>();

    /**
     * Cache for {@link PreDestroy}
     */
    private final Map<Method, Object> preDestroyMethodCache = new LinkedHashMap<>();

    public static ClassicComponentContext getInstance() {
        return (ClassicComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    private void init(ServletContext servletContext) throws RuntimeException {
        ClassicComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        init();
    }

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        init(ctx);
    }

    @Override
    public void init() {
        initClassLoader();
        initEnvContext();
        instantiateComponents();
        initComponents();
        registerShutdownHook();
    }

    private void initClassLoader() {
        this.classLoader = servletContext.getClassLoader();
    }

    private void initEnvContext() {
        if (this.envContext != null) {
            return;
        }

        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup("java:comp/env");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }

    }

    private void initComponents() {
        componentsMap.values().forEach(this::initComponent);
    }

    private void initComponent(Object component) {
        Class<?> componentClass = component.getClass();
        injectComponents(component, componentClass);
        List<Method> candidateMethods = findCandidateMethods(componentClass);
        processPostConstruct(component, candidateMethods);
        processPreDestroyMetaData(component, candidateMethods);
    }

    private void instantiateComponents() {
        List<String> componentNames = listAllComponentNames();
        Logger.info(componentNames.toString());

        componentNames.forEach(name -> componentsMap.put(name, lookupComponent(name)));
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::processPreDestroy));
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
            component = (C) envContext.lookup(name);
        } catch (NamingException e) {
            throw new NoSuchElementException(name);
        }
        return component;
    }

    @Override
    public List<String> getComponentNames() {
        return null;
    }

    @Override
    public void destroy() throws RuntimeException {
        processPreDestroy();
        clearCache();
        close(envContext);
    }

    private void clearCache() {
        componentsMap.clear();
        preDestroyMethodCache.clear();
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
        return executeInContext(this.envContext, function, ignoreException);
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

    private void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }

    /**
     * Inject component class's fields annotated by {@link Resource}
     *
     * @param component      the object
     * @param componentClass the Class of the object
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
     * @param component                     the Class instance
     * @param candidateMethods              list of candidate methods
     * @see #injectComponents(Object, Class)
     */
    private void processPostConstruct(final Object component, List<Method> candidateMethods) {
        candidateMethods.stream()
                .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> ThrowableAction.execute(() -> method.invoke(component)));
    }

    /**
     * Find candidate methods for {@link PreDestroy} and {@link PostConstruct}
     *
     * @param componentClass            the Class instance
     * @return                          list of candidate methods
     */
    private List<Method> findCandidateMethods(Class<?> componentClass) {
        return Stream.of(componentClass.getDeclaredMethods())
                .filter(method ->
                        !Modifier.isStatic(method.getModifiers()) && // not static
                                method.getParameterCount() == 0 // no argument
                ).collect(Collectors.toList());
    }

    /**
     * Cache methods annotated by {@link PostConstruct} of component instance
     *
     * @param component
     * @param candidateMethods
     */
    private void processPreDestroyMetaData(final Object component, List<Method> candidateMethods) {
        candidateMethods.stream()
                .filter(m -> m.isAnnotationPresent(PreDestroy.class))
                .forEach(method -> preDestroyMethodCache.put(method, component));
    }

    /**
     * Invoke methods annotated by {@link PreDestroy } before JVM destroy
     */
    private void processPreDestroy() {
        Method[] methods = preDestroyMethodCache.keySet().toArray(new Method[0]);
        for (Method method : methods) {
            Object component = preDestroyMethodCache.remove(methods);
            ThrowableAction.execute(() -> method.invoke(component));
        }
    }
}
