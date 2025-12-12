package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class EnchantingLapisTweak extends Tweak {

	public EnchantingLapisTweak(@Nonnull Tweaky plugin) {
		super(plugin, "enchanting_lapis");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ENCHANTING_LAPIS.get();
	}

	@EventHandler
	public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
		if (!(event.getInventory() instanceof EnchantingInventory inv) || inv.getLocation() == null) {
			return;
		}

		Block block = inv.getLocation().getBlock();
		if (!(block.getState() instanceof EnchantingTable ench)) {
			return;
		}

		int amount = inv.getSecondary() != null ? inv.getSecondary().getAmount() : 0;
		inv.setSecondary(null);

		NamespacedKey key = new NamespacedKey(plugin, "enchanting_lapis");
		if (amount == 0) {
			ench.getPersistentDataContainer().remove(key);
		} else {
			ench.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, amount);
		}
		ench.update();
	}

	@EventHandler
	public void onInventoryOpen(@Nonnull InventoryOpenEvent event) {
		if (!(event.getInventory() instanceof EnchantingInventory inv) || inv.getLocation() == null) {
			return;
		}

		Block block = inv.getLocation().getBlock();
		if (!(block.getState() instanceof EnchantingTable ench)) {
			return;
		}

		NamespacedKey key = new NamespacedKey(plugin, "enchanting_lapis");
		Integer lapisCount = ench.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
		if (lapisCount != null) {
			inv.setSecondary(new ItemStack(Material.LAPIS_LAZULI, lapisCount));
		}
	}

	@EventHandler
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Block block = event.getBlock();
		if (!(block.getState() instanceof EnchantingTable ench)) {
			return;
		}

		NamespacedKey key = new NamespacedKey(plugin, "enchanting_lapis");
		Integer lapisCount = ench.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
		if (lapisCount != null) {
			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.LAPIS_LAZULI, lapisCount));
		}
	}
}
