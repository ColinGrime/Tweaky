package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropMagnetTweak extends DefaultTweak {

	private final Map<Item, Instant> drops = new HashMap<>();
	private Task task;

	public DropMagnetTweak(@Nonnull Tweaky plugin) {
		super(plugin, "drops_magnet");
	}

	@Override
	public void init() {
		task = Scheduler.sync().runRepeating(() -> {
			Instant now = Instant.now();
			drops.values().removeIf(instant -> instant.plusSeconds(2).isBefore(now));
		}, 20L, 20L);
	}

	@Override
	public void shutdown() {
		task.stop();
		drops.clear();
	}

	@TweakHandler
	public void onBlockDropItem(@Nonnull BlockDropItemEvent event) {
		List<Item> items = event.getItems();
		if (items.isEmpty()) {
			return;
		}

		for (Item item : items) {
			Vector velocity = Util.direction(item.getLocation(), event.getPlayer().getEyeLocation());
			item.setVelocity(velocity.multiply(new Vector(0.3, 0.4, 0.3)));
			drops.put(item, Instant.now());
		}
	}

	@EventHandler
	public void onItemMerge(@Nonnull ItemMergeEvent event) {
		// Stop merging for 2 seconds.
		if (drops.containsKey(event.getEntity())) {
			event.setCancelled(true);
		}
	}
}
