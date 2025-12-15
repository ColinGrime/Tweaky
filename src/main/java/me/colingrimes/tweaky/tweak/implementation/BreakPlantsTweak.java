package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BreakPlantsTweak extends Tweak {

	public BreakPlantsTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_plants");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BREAK_PLANTS.get();
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Block block = event.getBlock();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (!Blocks.isPlant(block.getType()) || !Tag.ITEMS_HOES.isTagged(item.getType())) {
			return;
		}

		int radius = item.getType() == Material.NETHERITE_HOE ? 2 : 1;
		for (Location loc : Util.around(block.getLocation(), radius)) {
			if (Blocks.isPlant(loc.getBlock().getType())) {
				Blocks.destroy(loc.getBlock());
			}
		}

		event.setCancelled(true);
	}
}
