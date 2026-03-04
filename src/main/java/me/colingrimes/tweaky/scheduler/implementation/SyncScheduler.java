package me.colingrimes.tweaky.scheduler.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.SyncTask;
import me.colingrimes.tweaky.scheduler.task.Task;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SyncScheduler implements Scheduler {

    @Nonnull
    @Override
    public <T> CompletableFuture<T> call(@Nonnull Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTask(Tweaky.getInstance(), () -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Nonnull
    @Override
    public <T> CompletableFuture<T> callLater(@Nonnull Callable<T> task, long delayTicks) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskLater(Tweaky.getInstance(), () -> {
            try {
                future.complete(task.call());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, delayTicks);
        return future;
    }

    @Nonnull
    @Override
    public Task runRepeating(@Nonnull Runnable task, long delay, long period, long cancelTicks) {
        return new SyncTask(task, delay, period, cancelTicks);
    }

    @Nonnull
    @Override
    public Task runRepeating(@Nonnull Consumer<Task> task, long delay, long period, long cancelTicks) {
        return new SyncTask(task, delay, period, cancelTicks);
    }
}
