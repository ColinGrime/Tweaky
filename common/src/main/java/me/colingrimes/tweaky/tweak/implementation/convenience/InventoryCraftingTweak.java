package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class InventoryCraftingTweak extends DefaultTweak {

	public InventoryCraftingTweak(@Nonnull Tweaky plugin) {
		super(plugin, "inventory_crafting");
	}

	@TweakHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		if (event.getView().getTopInventory().getType() != InventoryType.CRAFTING) {
			return;
		}

		ItemStack item = event.getCurrentItem();
		if (event.getClick().isRightClick() && item != null && item.getType() == Material.CRAFTING_TABLE) {
			event.setCancelled(true);
			Scheduler.sync().run(() -> event.getWhoClicked().openWorkbench(null, true));
		}
	}
}
