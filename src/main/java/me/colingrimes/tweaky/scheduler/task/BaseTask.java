package me.colingrimes.tweaky.scheduler.task;

import me.colingrimes.tweaky.scheduler.Scheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

abstract class BaseTask implements Task {

    private final AtomicInteger timesRan = new AtomicInteger(0);
    private final Runnable runnable;
    private final Consumer<Task> consumer;
    private final BukkitTask task;

    BaseTask(@Nonnull Runnable runnable, long delayTicks, long periodTicks, long cancelTicks) {
        this.runnable = runnable;
        this.consumer = null;
        this.task = schedule(delayTicks, periodTicks);
        if (cancelTicks != -1) {
            Scheduler.async().runLater(this::stop, cancelTicks);
        }
    }

    BaseTask(@Nonnull Consumer<Task> consumer, long delayTicks, long periodTicks, long cancelTicks) {
        this.runnable = null;
        this.consumer = consumer;
        this.task = schedule(delayTicks, periodTicks);
        if (cancelTicks != -1) {
            Scheduler.async().runLater(this::stop, cancelTicks);
        }
    }

    /**
     * Schedules the task.
     */
    @Nonnull
    public abstract BukkitTask schedule(long delayTicks, long periodTicks);

    @Override
    public void run() {
        if (isCancelled()) {
            return;
        } else if (runnable != null) {
            runnable.run();
        } else if (consumer != null) {
            consumer.accept(this);
        }
        timesRan.incrementAndGet();
    }

    @Override
    public void stop() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public int getTimesRan() {
        return timesRan.get();
    }

    @Override
    public int getTaskId() {
        return task.getTaskId();
    }

    @Nonnull
    @Override
    public BukkitTask getBukkitTask() {
        return task;
    }
}