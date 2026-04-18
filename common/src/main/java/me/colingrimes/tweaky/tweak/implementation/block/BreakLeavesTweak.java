package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class BreakLeavesTweak extends DefaultTweak {

	private static final Vector[] directions = {
			new Vector(0, 0, 0),
			new Vector(1, 0, 0),
			new Vector(-1, 0, 0),
			new Vector(0, 1, 0),
			new Vector(0, -1, 0),
			new Vector(0, 0, 1),
			new Vector(0, 0, -1),
	};

	public BreakLeavesTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_leaves");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(Tag.ITEMS_HOES)
				.block(Tag.LEAVES);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		Location location = event.getBlock().getLocation();

		if (item.getType() == Material.NETHERITE_HOE) {
			Util.around(location, 1).forEach(l -> destroyLeaf(l, item));
		} else {
			Arrays.stream(directions).forEach(v -> destroyLeaf(location.clone().add(v), item));
		}

		Items.damage(item, event.getPlayer());
		event.setCancelled(true);
	}

	/**
	 * Destroys the leaf (if present) with the given item.
	 *
	 * @param location the location of the leaf
	 * @param item the item to destroy the leaf with
	 */
	private void destroyLeaf(@Nonnull Location location, @Nonnull ItemStack item) {
		Block block = location.getBlock();
		if (Tag.LEAVES.isTagged(block.getType())) {
			Blocks.destroy(block, item);
		}
	}
}
