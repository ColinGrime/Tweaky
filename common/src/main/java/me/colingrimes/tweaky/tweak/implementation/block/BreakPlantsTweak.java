package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BreakPlantsTweak extends DefaultTweak {

	public BreakPlantsTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_plants");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(Tag.ITEMS_HOES)
				.block(b -> Blocks.isPlant(b.getType()));
	}

	@TweakHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		int radius = item.getType() == Material.NETHERITE_HOE ? 2 : 1;
		Util.around(event.getBlock().getLocation(), radius).forEach(l -> destroyPlant(l, item));
		event.setCancelled(true);
	}

	/**
	 * Destroys the plant (if present) with the given item.
	 *
	 * @param location the location of the plant
	 * @param item the item to destroy the plant with
	 */
	private void destroyPlant(@Nonnull Location location, @Nonnull ItemStack item) {
		Block block = location.getBlock();
		if (Blocks.isPlant(block.getType())) {
			Blocks.destroy(block, item);
		}
	}
}
