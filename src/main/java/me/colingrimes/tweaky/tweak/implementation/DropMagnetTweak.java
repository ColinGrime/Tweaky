package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropMagnetTweak extends Tweak {

	private final Map<Item, Instant> drops = new HashMap<>();
	private BukkitTask task;

	public DropMagnetTweak(@Nonnull Tweaky plugin) {
		super(plugin, "drops_magnet");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_DROPS_MAGNET.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_DROPS_MAGNET.get().material(Material.FLINT);
	}

	@Override
	public void init() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			Instant now = Instant.now();
			drops.values().removeIf(instant -> instant.plusSeconds(2).isBefore(now));
		}, 20L, 20L);
	}

	@Override
	public void shutdown() {
		task.cancel();
		drops.clear();
	}

	@EventHandler
	public void onBlockDropItem(@Nonnull BlockDropItemEvent event) {
		Player player = event.getPlayer();
		List<Item> items = event.getItems();
		if (!hasPermission(player) || items.isEmpty()) {
			return;
		}

		for (Item item : items) {
			Vector velocity = Util.direction(item.getLocation(), player.getEyeLocation());
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
