package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class LeafCutterTweak extends Tweak {

	private static final Vector[] directions = {
			new Vector(0, 0, 0),
			new Vector(1, 0, 0),
			new Vector(-1, 0, 0),
			new Vector(0, 1, 0),
			new Vector(0, -1, 0),
			new Vector(0, 0, 1),
			new Vector(0, 0, -1),
	};

	public LeafCutterTweak(@Nonnull Tweaky plugin) {
		super(plugin, "leaf_cutter");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_LEAF_CUTTER.get();
	}

	@EventHandler
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Block block = event.getBlock();
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (!Tag.ITEMS_HOES.isTagged(item.getType()) || !Tag.LEAVES.isTagged(block.getType())) {
			return;
		}

		if (item.getType() == Material.NETHERITE_HOE) {
			for (Location loc : Util.around(block.getLocation(), 1)) {
				if (Tag.LEAVES.isTagged(loc.getBlock().getType())) {
					loc.getBlock().breakNaturally(item);
				}
			}
		} else {
			for (Vector vector : directions) {
				Location loc = block.getLocation().clone().add(vector);
				if (Tag.LEAVES.isTagged(loc.getBlock().getType())) {
					loc.getBlock().breakNaturally(item);
				}
			}
		}

		event.setCancelled(true);
	}
}
