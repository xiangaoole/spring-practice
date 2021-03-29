package com.haroldgao.function;

/**
 * A function interface for action with {@link Throwable}
 */
@FunctionalInterface
public interface ThrowableAction {

    /**
     * Execute the action
     *
     * @throws Throwable    if met with error
     */
    void execute() throws Throwable;

    /**
     * Execute {@link ThrowableAction}
     *
     * @param action
     * @throws RuntimeException     wrap {@link Exception} to {@link RuntimeException}
     */
    static void execute(ThrowableAction action) throws RuntimeException {
        try {
            action.execute();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
