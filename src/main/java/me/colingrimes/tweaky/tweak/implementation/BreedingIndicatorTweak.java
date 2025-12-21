package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreedingIndicatorTweak extends Tweak {

	private final Map<UUID, Instant> breedingEntities = new HashMap<>();
	private BukkitTask task;

	public BreedingIndicatorTweak(@Nonnull Tweaky plugin) {
		super(plugin, "breeding_indicator");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BREEDING_INDICATOR.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.COW_SPAWN_EGG)
				.name("&aBreeding Indicator")
				.lore("&7Shows when Mobs can breed again.")
				.usage("&eUsage: &aAdds an indicator above Mobs to show when they can breed again.");
	}

	@Override
	public void init() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
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
	public void shutdown() {
		task.cancel();
		breedingEntities.keySet().forEach(uuid -> {
			Entity mob = Bukkit.getEntity(uuid);
			if (mob != null && !mob.isDead()) {
				mob.setCustomName(null);
				mob.setCustomNameVisible(false);
			}
		});
		breedingEntities.clear();
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
