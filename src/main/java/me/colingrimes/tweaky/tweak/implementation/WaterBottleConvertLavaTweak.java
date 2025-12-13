package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.SplashPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;

public class WaterBottleConvertLavaTweak extends Tweak {

	public WaterBottleConvertLavaTweak(@Nonnull Tweaky plugin) {
		super(plugin, "water_bottle_convert_lava");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_WATER_BOTTLE_CONVERT_LAVA.get();
	}

	@EventHandler
	public void onProjectileLaunch(@Nonnull ProjectileLaunchEvent event) {
		if (!(event.getEntity() instanceof SplashPotion potion)) {
			return;
		}

		ItemStack item = potion.getItem();
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		if (meta == null || meta.getBasePotionType() != PotionType.WATER) {
			return;
		}

		Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
			if (!potion.isDead() && potion.getLocation().getBlock().getType() != Material.LAVA) {
				return;
			}

			task.cancel();

			int remaining = Util.number(6, 10);
			if (remaining != convertLava(potion.getLocation(), 0.50, 0, remaining)) {
				Sounds.play(potion, Sound.BLOCK_LAVA_EXTINGUISH);
			}
		}, 1L, 1L);
	}

	/**
	 * Attempts to convert the lava in the given radius to obsidian.
	 *
	 * @param location the location
	 * @param radius the radius around the location to check for lava
	 * @param skipChance chance to skip a lava block
	 * @param remaining how much is left to convert
	 * @return how much is left to convert
	 */
	private int convertLava(@Nonnull Location location, double radius, int skipChance, int remaining) {
		if (remaining == 0 || skipChance >= 100) {
			return remaining;
		}

		for (Location check : Util.around(location, radius)) {
			if (check.getBlock().getType() == Material.LAVA && Util.chance(100 - skipChance)) {
				check.getBlock().setType(Material.OBSIDIAN);
				remaining -= 1;
			}
			if (remaining == 0) {
				return 0;
			}
		}

		return convertLava(location, radius + 0.5, skipChance + 25, remaining);
	}
}
