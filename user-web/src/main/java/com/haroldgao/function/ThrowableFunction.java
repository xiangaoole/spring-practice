package com.haroldgao.function;

/**
 * Represents a function that accepts one argument and produces a result.
 * May throw a {@link RuntimeException} wrapper {@link Throwable}
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 *
 * @since 1.0
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t) throws Throwable;

    /**
     * Execute {@link ThrowableFunction}
     *
     * @param t the function argument
     * @return the function result
     * @throws RuntimeException wrappers {@link Throwable}
     */
    default R execute(T t) throws RuntimeException{
        R result = null;
        try {
            result = apply(t);
        } catch (Throwable e) {
            throw new RuntimeException(e.getCause());
        }
        return result;
    }

    static <T, R> R execute(T t, ThrowableFunction<T, R> function) {
        return function.execute(t);
    }

}
