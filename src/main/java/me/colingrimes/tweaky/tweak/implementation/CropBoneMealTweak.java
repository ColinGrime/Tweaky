package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

/**
 * Allows you to bone meal a few additional crops:
 * <li>Nether Wart</li>
 * <li>Sugar Cane</li>
 * <li>Cactus</li>
 */
public class CropBoneMealTweak extends Tweak {

	public CropBoneMealTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_bone_meal");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CROPS_BONE_MEAL.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isRightClick() || !event.isItem(Material.BONE_MEAL) || !(event.getBlock().getBlockData() instanceof Ageable crop)) {
			return;
		}

		// Grows height crops.
		if (event.isBlock(Material.SUGAR_CANE, Material.CACTUS)) {
			Location location = event.getLocation();
			while (location.getBlock().getType() == event.getBlockType()) {
				location.add(0, 1, 0);
			}

			Block top = location.getBlock();
			if (top.getType().isAir()) {
				Util.removeSingle(event.getItem());
				top.setType(event.getBlockType());
				event.setCancelled(true);
			}
		}

		// Grows nether wart.
		if (event.isBlock(Material.NETHER_WART) && crop.getAge() < crop.getMaximumAge()) {
			Util.removeSingle(event.getItem());
			crop.setAge(crop.getAge() + 1);
			event.getBlock().setBlockData(crop);
			event.setCancelled(true);
		}
	}
}
