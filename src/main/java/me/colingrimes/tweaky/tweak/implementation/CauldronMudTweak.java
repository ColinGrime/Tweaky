package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Events;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_CAULDRON_MUD.get().material(Material.MUD);
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

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

			task.cancel();

			// Check for permission
			if (!Events.canInteract(player, block)) {
				return;
			}

			Sounds.play(block, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON);
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
