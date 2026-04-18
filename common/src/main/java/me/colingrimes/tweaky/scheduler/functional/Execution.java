package me.colingrimes.tweaky.scheduler.functional;

/**
 * Represents an operation that may throw an exception when executed.
 * Similar to {@link Runnable} but allows checked exceptions.
 */
@FunctionalInterface
public interface Execution {

    /**
     * Runs the operation.
     *
     * @throws Exception if execution fails
     */
    void execute() throws Exception;
}
