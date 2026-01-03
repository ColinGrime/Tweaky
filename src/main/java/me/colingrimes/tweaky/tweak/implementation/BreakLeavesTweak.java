package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class BreakLeavesTweak extends Tweak {

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
	public boolean isEnabled() {
		return settings.TWEAK_BREAK_LEAVES.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_BREAK_LEAVES.get().material(Material.MANGROVE_LEAVES);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

		Block block = event.getBlock();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!Tag.ITEMS_HOES.isTagged(item.getType()) || !Tag.LEAVES.isTagged(block.getType())) {
			return;
		}

		if (item.getType() == Material.NETHERITE_HOE) {
			for (Location loc : Util.around(block.getLocation(), 1)) {
				if (Tag.LEAVES.isTagged(loc.getBlock().getType())) {
					Blocks.destroy(loc.getBlock(), item);
				}
			}
		} else {
			for (Vector vector : directions) {
				Location loc = block.getLocation().clone().add(vector);
				if (Tag.LEAVES.isTagged(loc.getBlock().getType())) {
					Blocks.destroy(loc.getBlock(), item);
				}
			}
		}

		Items.damage(item, player);
		event.setCancelled(true);
	}
}
