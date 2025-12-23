package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.EnchantingTable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

public class EnchantingLapisTweak extends Tweak {

	private static final String ENCHANTING_LAPIS_KEY = "enchanting_lapis";
	private final Map<Location, SharedEnchantingTable> shared = new HashMap<>();

	public EnchantingLapisTweak(@Nonnull Tweaky plugin) {
		super(plugin, "enchanting_lapis");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ENCHANTING_LAPIS.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.LAPIS_LAZULI)
				.name("&aLapis Store")
				.lore("&7Permanently store Lapis in Enchanting Tables.")
				.usage("&eUsage: &aLapis stays in the Lapis slot in Enchanting Tables when you close it.");
	}

	@Override
	public void shutdown() {
		shared.values().forEach(ench -> new HashSet<>(ench.viewers).forEach(viewer -> {
			ench.close(viewer);
			viewer.closeInventory();
		}));
		shared.clear();
	}

	@EventHandler
	public void onInventoryOpen(@Nonnull InventoryOpenEvent event) {
		if (!(event.getInventory() instanceof EnchantingInventory inv) || inv.getLocation() == null) {
			return;
		}

		Block block = inv.getLocation().getBlock();
		if (block.getState() instanceof EnchantingTable ench) {
			Player player = (Player) event.getPlayer();
			shared.computeIfAbsent(inv.getLocation(), __ -> new SharedEnchantingTable(ench)).open(player, inv);
		}
	}

	@EventHandler
	public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
		if (!(event.getInventory() instanceof EnchantingInventory inv) || inv.getLocation() == null) {
			return;
		}

		SharedEnchantingTable ench = shared.get(inv.getLocation());
		if (ench != null) {
			ench.close((Player) event.getPlayer(), inv);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getState() instanceof EnchantingTable ench) {
			shared.computeIfAbsent(block.getLocation(), __ -> new SharedEnchantingTable(ench)).destroy();
		}
	}

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		for (SharedEnchantingTable ench : shared.values()) {
			if (ench.viewers.contains(event.getPlayer())) {
				ench.close(event.getPlayer());
			}
		}
	}

	// EVERYTHING BELOW IS A SAVE EVENT.
	@EventHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		save((Player) event.getWhoClicked(), event.getInventory());
	}

	@EventHandler
	public void onInventoryDrag(@Nonnull InventoryDragEvent event) {
		save((Player) event.getWhoClicked(), event.getInventory());
	}

	@EventHandler
	public void onEnchantItem(@Nonnull EnchantItemEvent event) {
		save(event.getEnchanter(), event.getInventory());
	}

	/**
	 * Handles the saving of the enchanting table inventory.
	 *
	 * @param player the player
	 * @param inventory the inventory
	 */
	private void save(@Nonnull Player player, @Nonnull Inventory inventory) {
		if (!(inventory instanceof EnchantingInventory inv) || inv.getLocation() == null) {
			return;
		}

		SharedEnchantingTable ench = shared.get(inv.getLocation());
		if (ench != null) {
			ench.save(player, inv, true);
		}
	}

	private class SharedEnchantingTable {
		private final Set<Player> viewers = new HashSet<>();
		private final EnchantingTable ench;
		private int lapisCount;

		public SharedEnchantingTable(@Nonnull EnchantingTable ench) {
			this.ench = ench;
			this.lapisCount = NBT.getTag(ench, ENCHANTING_LAPIS_KEY, Integer.class).orElse(0);
		}

		/**
		 * Handles the opening of the enchanting table to ensure its shared.
		 *
		 * @param player the player
		 * @param inventory the current enchanting inventory they have opened
		 */
		public void open(@Nonnull Player player, @Nonnull EnchantingInventory inventory) {
			inventory.setSecondary(lapisCount > 0 ? new ItemStack(Material.LAPIS_LAZULI, lapisCount) : null);
			viewers.add(player);
		}

		/**
		 * Handles the closing of the enchanting table for the player.
		 * <p>
		 * Stops the default behavior of giving lapis back to the player on close.
		 *
		 * @param player the player
		 */
		public void close(@Nonnull Player player) {
			if (viewers.contains(player) && player.getOpenInventory().getTopInventory() instanceof EnchantingInventory inv) {
				close(player, inv);
			}
			viewers.remove(player);
		}

		/**
		 * Handles the closing of the enchanting table for the player.
		 * <p>
		 * Stops the default behavior of giving lapis back to the player on close.
		 *
		 * @param player the player
		 * @param inventory the current enchanting inventory they have opened
		 */
		public void close(@Nonnull Player player, @Nonnull EnchantingInventory inventory) {
			save(player, inventory);
			inventory.setSecondary(null);
			viewers.remove(player);
		}

		/**
		 * Handles the saving of the enchanting table when a player makes a change.
		 *
		 * @param player the player
		 * @param inventory the current enchanting inventory they have opened
		 * @param delay whether to delay it by 1 tick
		 */
		public void save(@Nonnull Player player, @Nonnull EnchantingInventory inventory, boolean delay) {
			if (delay) {
				Bukkit.getScheduler().runTask(plugin, () -> save(player, inventory));
			} else {
				save(player, inventory);
			}
		}

		/**
		 * Handles the saving of the enchanting table when a player makes a change.
		 *
		 * @param player the player
		 * @param inventory the current enchanting inventory they have opened
		 */
		private void save(@Nonnull Player player, @Nonnull EnchantingInventory inventory) {
			if (!viewers.contains(player)) {
				return;
			}

			lapisCount = inventory.getSecondary() != null ? inventory.getSecondary().getAmount() : 0;
			if (lapisCount == 0) {
				NBT.removeTag(ench, ENCHANTING_LAPIS_KEY);
			} else {
				NBT.setTag(ench, ENCHANTING_LAPIS_KEY, lapisCount);
			}

			ench.update();

			// Update all viewers.
			for (Player viewer : viewers) {
				if (!viewer.equals(player) && viewer.getOpenInventory().getTopInventory() instanceof EnchantingInventory inv) {
					open(viewer, inv);
				}
			}
		}

		/**
		 * Handles the destruction of the enchanting table.
		 * <p>
		 * Ensures any remaining lapis is dropped from the broken enchanting table.
		 */
		public void destroy() {
			for (Player viewer : viewers) {
				close(viewer);
			}

			Location location = ench.getLocation();
			if (location.getWorld() == null) {
				return;
			}

			shared.remove(location);
			if (lapisCount > 0) {
				location.getWorld().dropItem(location, new ItemStack(Material.LAPIS_LAZULI, lapisCount));
			}
		}
	}
}
