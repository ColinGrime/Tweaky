package me.colingrimes.tweaky.scheduler.task;

import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;

/**
 * Represents a task that can be run repeatedly.
 */
public interface Task extends Runnable {

    /**
     * Cancels the task.
     */
    void stop();

    /**
     * Gets whether the task has been cancelled.
     *
     * @return true if the task has been cancelled
     */
    boolean isCancelled();

    /**
     * Gets the number of times the task has been run.
     *
     * @return the number of times the task has been run
     */
    int getTimesRan();

    /**
     * Gets the task ID.
     *
     * @return the task ID
     */
    int getTaskId();

    /**
     * Gets the Bukkit task that is running this task.
     *
     * @return the Bukkit task
     */
    @Nonnull
    BukkitTask getBukkitTask();
}