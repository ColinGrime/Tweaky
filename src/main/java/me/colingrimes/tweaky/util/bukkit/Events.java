package me.colingrimes.tweaky.util.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public final class Events {

	/**
	 * Checks if the player can modify the block (place or break).
	 *
	 * @param player the player
	 * @param block the block
	 * @return true if the block can be modified
	 */
	public static boolean canModify(@Nonnull Player player, @Nonnull Block block) {
		return canPlace(player, block, block.getRelative(BlockFace.DOWN)) || canBreak(player, block);
	}

	/**
	 * Checks if the player can place the block.
	 *
	 * @param player the player
	 * @param block the block to place
	 * @param blockAgainst the block that it is placed against
	 * @return true if the block can be placed
	 */
	public static boolean canPlace(@Nonnull Player player, @Nonnull Block block, @Nonnull Block blockAgainst) {
		BlockPlaceEvent event = new BlockPlaceEvent(
				block,
				block.getState(),
				blockAgainst,
				player.getInventory().getItemInMainHand(),
				player,
				true,
				EquipmentSlot.HAND
		);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Checks if the player can break the block.
	 *
	 * @param player the player
	 * @param block the block to break
	 * @return true if the block can be broken
	 */
	public static boolean canBreak(@Nonnull Player player, @Nonnull Block block) {
		BlockBreakEvent event = new BlockBreakEvent(block, player);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Checks if the block can be interacted with.
	 *
	 * @param player the player
	 * @param block the block to interact with
	 * @return true if the block can be interacted with
	 */
	public static boolean canInteract(@Nonnull Player player, @Nonnull Block block) {
		ItemStack item = player.getInventory().getItemInMainHand();
		PlayerInteractEvent event = new PlayerInteractEvent(player, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, item, block, BlockFace.UP);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Checks if the entity can be interacted with.
	 *
	 * @param player the player
	 * @param entity the entity to interact with
	 * @return true if the entity can be interacted with
	 */
	public static boolean canInteractEntity(@Nonnull Player player, @Nonnull Entity entity) {
		PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, entity);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}
}
