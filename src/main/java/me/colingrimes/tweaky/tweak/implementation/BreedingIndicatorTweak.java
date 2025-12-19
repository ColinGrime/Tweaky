package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BreedingIndicatorTweak extends Tweak {

	private final Map<LivingEntity, Instant> mobs = new HashMap<>();
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
			Iterator<Map.Entry<LivingEntity, Instant>> iterator = mobs.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<LivingEntity, Instant> mob = iterator.next();
				Duration time = Duration.between(now, mob.getValue());
				if (time.isPositive()) {
					mob.getKey().setCustomName(Text.color("&a" + Text.format(time)));
				} else {
					mob.getKey().setCustomName(null);
					mob.getKey().setCustomNameVisible(false);
					iterator.remove();
				}
			}
		}, 20L, 20L);
	}

	@Override
	public void shutdown() {
		task.cancel();
		mobs.keySet().forEach(mob -> {
			mob.setCustomName(null);
			mob.setCustomNameVisible(false);
		});
		mobs.clear();
	}

	@EventHandler
	public void onEntityBreed(@Nonnull EntityBreedEvent event) {
		mobs.put(event.getMother(), Instant.now().plusSeconds(300));
		mobs.put(event.getFather(), Instant.now().plusSeconds(300));
		event.getMother().setCustomName(Text.color("&a5:00"));
		event.getFather().setCustomName(Text.color("&a5:00"));
		event.getMother().setCustomNameVisible(true);
		event.getFather().setCustomNameVisible(true);
	}
}
