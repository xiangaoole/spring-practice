package com.haroldgao.rest.util;

import javax.ws.rs.Path;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

public interface PathUtil {
    String SLASH = "/";

    String ENCODED_SLASH = URLUtil.encode(SLASH);


    static String resolvePath(AnnotatedElement annotatedElement) {
        Path annotation = annotatedElement.getAnnotation(Path.class);
        if (annotation == null) {
            return null;
        }
        String value = annotation.value();
        if (!value.startsWith(SLASH)) {
            value = SLASH + value;
        }
        return value;
    }

    static String resolvePath(Class resource, String methodName) {
        return Stream.of(resource.getDeclaredMethods())
                .filter(m -> m.getName().equals(methodName))
                .map(PathUtil::resolvePath)
                .filter(Objects::nonNull)
                .findFirst()
                .get();
    }

    static String resolvePath(Class resource, Method handleMethod) {
        String pathFromResourceClass = resolvePath(resource);
        String pathFromHandleMethod = resolvePath(handleMethod);
        return pathFromResourceClass != null ? pathFromResourceClass + pathFromHandleMethod : pathFromHandleMethod;
    }
}
