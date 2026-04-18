package me.colingrimes.tweaky.scheduler.task;

import me.colingrimes.tweaky.Tweaky;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AsyncTask extends BaseTask {

	public AsyncTask(@Nonnull Runnable runnable, long delayTicks, long periodTicks, long cancelTicks) {
		super(runnable, delayTicks, periodTicks, cancelTicks);
	}

	public AsyncTask(@Nonnull Consumer<Task> consumer, long delayTicks, long periodTicks, long cancelTicks) {
		super(consumer, delayTicks, periodTicks, cancelTicks);
	}

	@Nonnull
	@Override
	public BukkitTask schedule(long delayTicks, long periodTicks) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(Tweaky.getInstance(), this, delayTicks, periodTicks);
	}
}
