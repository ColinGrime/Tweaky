package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_BREAK_PLANTS.get().material(Material.TALL_GRASS);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

		Block block = event.getBlock();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!Blocks.isPlant(block.getType()) || !Tag.ITEMS_HOES.isTagged(item.getType())) {
			return;
		}

		int radius = item.getType() == Material.NETHERITE_HOE ? 2 : 1;
		for (Location loc : Util.around(block.getLocation(), radius)) {
			if (Blocks.isPlant(loc.getBlock().getType())) {
				Blocks.destroy(loc.getBlock(), item);
			}
		}

		event.setCancelled(true);
	}
}
