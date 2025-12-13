package me.colingrimes.tweaky.util.bukkit;

import me.colingrimes.tweaky.util.Util;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public final class Items {

	/**
	 * Renames the item with the given name. Supports color codes.
	 *
	 * @param item the item to rename
	 * @param name the name
	 * @return the renamed item
	 */
	@Nonnull
	public static ItemStack rename(@Nonnull ItemStack item, @Nonnull String name) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(Util.color(name));
			item.setItemMeta(meta);
		}
		return item;
	}

	/**
	 * Attempts to give the player the item.
	 * If there is no room in the player's inventory, the item will be dropped on the ground.
	 *
	 * @param player the player
	 * @param item the item to give to the player
	 */
	public static void give(@Nonnull Player player, @Nonnull ItemStack item) {
		Collection<ItemStack> remaining = player.getInventory().addItem(item).values();
		for (ItemStack itemDrop : remaining) {
			drop(itemDrop, player.getLocation());
		}
	}

	/**
	 * Removes a single item from the stack.
	 *
	 * @param item the item stack
	 */
	public static void remove(@Nonnull ItemStack item) {
		item.setAmount(item.getAmount() - 1);
	}

	/**
	 * Damages the specified item by 1 durability.
	 *
	 * @param item the item to damage
	 * @param player the player to play the damage sound if applicable
	 */
	public static void damage(@Nullable ItemStack item, @Nonnull Player player) {
		if (item == null || !(item.getItemMeta() instanceof Damageable damageable)) {
			return;
		}

		int damage = damageable.getDamage() + 1;
		if (damage < item.getType().getMaxDurability()) {
			damageable.setDamage(damage);
			item.setItemMeta(damageable);
		} else {
			remove(item);
			Sounds.play(player, Sound.ENTITY_ITEM_BREAK);
		}
	}

	/**
	 * Drops the item at the given location.
	 *
	 * @param item the item to drop
	 * @param location the location
	 */
	public static void drop(@Nonnull ItemStack item, @Nonnull Location location) {
		if (location.getWorld() != null) {
			location.getWorld().dropItemNaturally(location, item);
		}
	}
}
