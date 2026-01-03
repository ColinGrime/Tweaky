package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Bukkit;
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
import org.bukkit.inventory.view.AnvilView;

import javax.annotation.Nonnull;

public class InventoryEnderChestTweak extends Tweak {

	public InventoryEnderChestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "inventory_ender_chest");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_INVENTORY_ENDER_CHEST.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_INVENTORY_ENDER_CHEST.get().material(Material.ENDER_CHEST);
	}

	@EventHandler
	public void onPrepareAnvil(@Nonnull PrepareAnvilEvent event) {
		AnvilView view = event.getView();
		ItemStack first = event.getInventory().getItem(0);
		ItemStack second = event.getInventory().getItem(1);
		if (!hasPermission(view.getPlayer()) || first == null || second == null) {
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
		Bukkit.getScheduler().runTask(plugin, () -> view.setRepairCost(settings.TWEAK_INVENTORY_ENDER_CHEST_COST.get()));
	}

	@EventHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!hasPermission(player) || event.getView().getTopInventory().getType() != InventoryType.CRAFTING || !event.getClick().isRightClick()) {
			return;
		}

		ItemStack item = event.getCurrentItem();
		if (item != null && item.getType() == Material.ENDER_CHEST && item.containsEnchantment(Enchantment.SILK_TOUCH)) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(player.getEnderChest()));
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
