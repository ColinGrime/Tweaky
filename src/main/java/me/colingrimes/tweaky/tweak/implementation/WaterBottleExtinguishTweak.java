package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;

public class WaterBottleExtinguishTweak extends Tweak {

	public WaterBottleExtinguishTweak(@Nonnull Tweaky plugin) {
		super(plugin, "water_bottle_extinguish");
	}

	@Override
	public boolean isEnabled() {
		// Me when I make a tweak that already exists in Minecraft... o-o
		return false;
	}

	@EventHandler
	public void onPotionSplash(@Nonnull PotionSplashEvent event) {
		ItemStack item = event.getEntity().getItem();
		if (!(item.getItemMeta() instanceof PotionMeta potion) || potion.getBasePotionType() != PotionType.WATER) {
			return;
		}

		if (extinguishEntities(event) || extinguishFire(event)) {
			event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1F, 1F);
		}
	}

	private boolean extinguishEntities(@Nonnull PotionSplashEvent event) {
//		if (!settings.TWEAK_WATER_BOTTLE_EXTINGUISH_ENTITIES.get()) {
//			return false;
//		}

		boolean extinguished = false;
		for (LivingEntity entity : event.getAffectedEntities()) {
			if (entity.getFireTicks() > 0) {
				entity.setFireTicks(0);
				extinguished = true;
			}
		}
		return extinguished;
	}

	private boolean extinguishFire(@Nonnull PotionSplashEvent event) {
//		if (!settings.TWEAK_WATER_BOTTLE_EXTINGUISH_FIRE.get()) {
//			return false;
//		}

		boolean extinguished = false;
		for (Location loc : Util.around(event.getEntity().getLocation(), 2)) {
			if (Tag.FIRE.isTagged(loc.getBlock().getType())) {
				loc.getBlock().setType(Material.AIR);
				extinguished = true;
			}
		}
		return extinguished;
	}
}
