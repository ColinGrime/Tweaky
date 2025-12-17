package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class InventoryCraftingTweak extends Tweak {

	public InventoryCraftingTweak(@Nonnull Tweaky plugin) {
		super(plugin, "inventory_crafting");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_INVENTORY_CRAFTING.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.CRAFTING_TABLE)
				.name("&aInventory Crafting &8(Right Click)")
				.lore("&7Open up a Crafting Table in your inventory.")
				.usage("&eUsage: &aRight Click a Crafting Table in your inventory to open it.");
	}

	@EventHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		if (event.getView().getTopInventory().getType() != InventoryType.CRAFTING) {
			return;
		}

		ItemStack item = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		if (event.getClick().isRightClick() && item != null && item.getType() == Material.CRAFTING_TABLE) {
			event.setCancelled(true);
			Bukkit.getScheduler().runTask(plugin, () -> player.openWorkbench(null, true));
		}
	}
}
