package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import javax.annotation.Nonnull;

public class InventoryEnderChestTweak extends DefaultTweak {

	public InventoryEnderChestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "inventory_ender_chest");
	}

	@TweakHandler
	public void onPrepareAnvil(@Nonnull PrepareAnvilEvent event) {
		ItemStack first = event.getInventory().getItem(0);
		ItemStack second = event.getInventory().getItem(1);
		if (first == null || second == null) {
			return;
		}

		if (first.getType() != Material.ENDER_CHEST || first.getAmount() != 1 || second.getType() != Material.ENCHANTED_BOOK) {
			return;
		}

		if (!(second.getItemMeta() instanceof EnchantmentStorageMeta meta) || !meta.hasStoredEnchant(Enchantment.SILK_TOUCH)) {
			return;
		}

		ItemStack result = first.clone();
		result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);

		String renameText = event.getView().getRenameText();
		if (renameText != null) {
			Items.rename(result, renameText);
		}

		event.setResult(result);
		Scheduler.sync().run(() -> event.getView().setRepairCost(settings.TWEAK_INVENTORY_ENDER_CHEST_COST.get()));
	}

	@TweakHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		if (event.getView().getTopInventory().getType() != InventoryType.CRAFTING || !event.getClick().isRightClick()) {
			return;
		}

		ItemStack item = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		if (item != null && item.getType() == Material.ENDER_CHEST && item.containsEnchantment(Enchantment.SILK_TOUCH)) {
			event.setCancelled(true);
			Scheduler.sync().run(() -> player.openInventory(player.getEnderChest()));
		}
	}

	@EventHandler
	public void onBlockPlace(@Nonnull BlockPlaceEvent event) {
		ItemStack item = event.getItemInHand();
		if (item.getType() == Material.ENDER_CHEST && item.containsEnchantment(Enchantment.SILK_TOUCH)) {
			event.setCancelled(true);
		}
	}
}
