package com.haroldgao.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class Converters {

    private static final int DEFAULT_PRIORITY = 100;

    private final Map<Class<?>, PriorityQueue<PrioritizedConverter<?>>> typedConverters = new HashMap<>();

    private ClassLoader classLoader;

    private boolean addedDiscoveredConverters;

    public Converters() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public Converters(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void addDiscoveredConverters() {
        if (addedDiscoveredConverters) {
            return;
        }
        addConverters(ServiceLoader.load(Converter.class, classLoader));
        addedDiscoveredConverters = true;
    }

    public void addConverters(Converter<?>... converters) {
        addConverters(Arrays.asList(converters));
    }

    public void addConverters(Iterable<Converter> converters) {
        converters.forEach(this::addConverter);
    }

    public <T> void addConverter(Converter<T> converter) {
        addConverter(converter, DEFAULT_PRIORITY);
    }

    public void addConverter(Converter<?> converter, int priority) {
        Class<?> convertedType = resolveConvertedType(converter);
        addConverter(converter, priority, convertedType);
    }

    public void addConverter(Converter<?> converter, int priority, Class<?> type) {
        PriorityQueue<PrioritizedConverter<?>> prioritizedConverters = typedConverters.computeIfAbsent(type, t -> new PriorityQueue<>());
        prioritizedConverters.offer(new PrioritizedConverter<>(converter, priority));
    }

    public List<Converter> getConverters(Class<?> forType) {
        PriorityQueue<PrioritizedConverter<?>> prioritizedConverters = typedConverters.get(forType);
        if (prioritizedConverters == null || prioritizedConverters.isEmpty()) {
            return Collections.emptyList();
        }
        List<Converter> converters = new LinkedList<>();
        for (PrioritizedConverter<?> c : prioritizedConverters) {
            converters.add(c.getConverter());
        }
        return converters;
    }

    protected Class<?> resolveConvertedType(Converter<?> converter) {
        Class<?> convertedType = null;
        Class<?> convertedClass = converter.getClass();

        while (convertedClass != null) {
            convertedType = resolveConvertedType(convertedClass);
            if (convertedType != null) {
                break;
            }

            Type genericSuperclass = convertedClass.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                convertedType = resolveConvertedType(genericSuperclass);
            }
            if (convertedType != null) {
                break;
            }

            convertedClass = convertedClass.getSuperclass();
        }

        return convertedType;
    }

    private Class<?> resolveConvertedType(Class<?> convertedClass) {
        Class<?> convertedType = null;
        for (Type superInterface : convertedClass.getGenericInterfaces()) {
            convertedType = resolveConvertedType(superInterface);

            if (convertedType != null) {
                break;
            }
        }
        return convertedType;
    }

    private Class<?> resolveConvertedType(Type superInterface) {
        Class<?> convertedType = null;
        if (superInterface instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) superInterface;
            if (type.getRawType() instanceof Class) {
                Class rawType = (Class) type.getRawType();
                if (Converter.class.isAssignableFrom(rawType)) {
                    Type[] actualTypeArguments = type.getActualTypeArguments();
                    if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof Class) {
                        convertedType = (Class<?>) actualTypeArguments[0];
                    }
                }
            }
        }
        return convertedType;
    }
}
