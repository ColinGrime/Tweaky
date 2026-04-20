package me.colingrimes.tweaky.tweak.implementation.text;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreedingIndicatorTweak extends DefaultTweak {

	private final Map<UUID, Instant> breedingEntities = new HashMap<>();
	private Task task;

	public BreedingIndicatorTweak(@Nonnull Tweaky plugin) {
		super(plugin, "breeding_indicator");
	}

	@Override
	protected void onEnable() {
		task = Scheduler.sync().runRepeating(() -> {
			Instant now = Instant.now();
			var iterator = breedingEntities.entrySet().iterator();
			while (iterator.hasNext()) {
				var entry = iterator.next();
				Entity entity = Bukkit.getEntity(entry.getKey());
				Duration time = Duration.between(now, entry.getValue());
				if (tick(entity, time)) {
					iterator.remove();
				}
			}
		}, 20L, 20L);
	}

	@Override
	protected void onDisable() {
		task.stop();
		breedingEntities.keySet().forEach(uuid -> {
			Entity mob = Bukkit.getEntity(uuid);
			if (mob != null && !mob.isDead()) {
				mob.setCustomName(null);
				mob.setCustomNameVisible(false);
			}
		});
		breedingEntities.clear();
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.setPermissionRequired(false);
	}

	@EventHandler
	public void onEntityBreed(@Nonnull EntityBreedEvent event) {
		breedingEntities.put(event.getMother().getUniqueId(), Instant.now().plusSeconds(300));
		breedingEntities.put(event.getFather().getUniqueId(), Instant.now().plusSeconds(300));
		event.getMother().setCustomName(Text.color("&a5:00"));
		event.getFather().setCustomName(Text.color("&a5:00"));
		event.getMother().setCustomNameVisible(true);
		event.getFather().setCustomNameVisible(true);
	}

	/**
	 * Sets the name to the breeding entity accordingly.
	 *
	 * @param entity the breeding entity
	 * @param time the current time it has left until it can breed again
	 * @return true if the breeding entity is done being updated and can be deleted from the map
	 */
	private boolean tick(@Nullable Entity entity, @Nonnull Duration time) {
		// Entity might have been unloaded, so we need to keep it in the map and wait.
		if (entity == null || entity.isDead()) {
			return false;
		}

		// If it is further than 20 blocks away from a player, we can reset name.
		if (Util.nearby(Player.class, entity.getLocation(), 20).isEmpty()) {
			entity.setCustomName(null);
			entity.setCustomNameVisible(false);
			return false;
		}

		// Default case -- count down the time.
		if (time.isPositive()) {
			entity.setCustomName(Text.color("&a" + Text.format(time)));
			entity.setCustomNameVisible(true);
			return false;
		}

		// Finished cooldown, we can reset and remove from map.
		entity.setCustomName(null);
		entity.setCustomNameVisible(false);
		return true;
	}
}
