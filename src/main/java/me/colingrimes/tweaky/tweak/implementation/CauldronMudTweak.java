package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class CauldronMudTweak extends Tweak {

	public CauldronMudTweak(@Nonnull Tweaky plugin) {
		super(plugin, "cauldron_mud");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CAULDRON_MUD.get();
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		Item dirt = event.getItemDrop();
		if (dirt.getItemStack().getType() != Material.DIRT) {
			return;
		}

		Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
			if (dirt.isDead()) {
				task.cancel();
				return;
			}

			Location location = dirt.getLocation();
			Block block = location.getBlock();
			if (block.getType() != Material.WATER_CAULDRON || !(block.getBlockData() instanceof Levelled cauldron)) {
				return;
			}

			dirt.getWorld().dropItem(location, new ItemStack(Material.MUD, dirt.getItemStack().getAmount()));
			dirt.remove();

			if (settings.TWEAK_CAULDRON_MUD_USE_WATER.get()) {
				if (cauldron.getLevel() > 1) {
					Blocks.edit(block, Levelled.class, l -> l.setLevel(l.getLevel() - 1));
				} else {
					block.setType(Material.CAULDRON);
				}
			}
		}, 1L, 1L);
	}
}
