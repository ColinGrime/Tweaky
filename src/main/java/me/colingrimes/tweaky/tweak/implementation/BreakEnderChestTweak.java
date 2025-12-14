package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BreakEnderChestTweak extends Tweak {

	enum Mode {
		Drop,
		Protection
	}

	public BreakEnderChestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_ender_chest");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BREAK_ENDER_CHEST.get();
	}

	@EventHandler
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() != Material.ENDER_CHEST) {
			return;
		}

		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if (item.getItemMeta() != null && item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
			return;
		}

		Mode mode = Util.parse(Mode.class, settings.TWEAK_BREAK_ENDER_CHEST_MODE.get());
		if (mode == null) {
			return;
		}

		switch (mode) {
			case Drop -> {
				block.setType(Material.AIR);
				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.ENDER_CHEST));
			}
			case Protection -> event.getPlayer().sendMessage(Util.color(settings.TWEAK_BREAK_ENDER_CHEST_PROTECTION.get()));
		}

		event.setCancelled(true);
	}
}
