package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Events;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.display.Displays;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HappyGhastPlacementTweak extends DefaultTweak {

	private final Set<Player> inRange = new HashSet<>();
	private final Map<Player, Target> targets = new HashMap<>();
	private Task rangeCheck, targetCheck;

	public HappyGhastPlacementTweak(@Nonnull Tweaky plugin) {
		super(plugin, "happy_ghast_placement");
	}

	@Override
	public void init() {
		rangeCheck = Scheduler.sync().runRepeating(() -> Players.forEach(this::checkRange), 0L, 20L);
		targetCheck = Scheduler.sync().runRepeating(() -> new HashSet<>(inRange).forEach(this::checkTarget), 0L, 2L);
	}

	@Override
	public void shutdown() {
		rangeCheck.stop();
		targetCheck.stop();

		inRange.clear();
		targets.values().forEach(t -> t.blockDisplay.remove());
		targets.clear();
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.HAND && placeOnGhast(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.HAPPY_GHAST && placeOnGhast(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	/**
	 * Places the item in the player's hand on the happy ghast.
	 *
	 * @param player the player
	 * @return true if a block was placed on the happy ghast
	 */
	private boolean placeOnGhast(@Nonnull Player player) {
		if (!targets.containsKey(player)) {
			return false;
		}

		ItemStack item = player.getInventory().getItemInMainHand();
		if (item.getType().isAir() || !item.getType().isBlock()) {
			return false;
		}

		Target target = targets.get(player);

		// Check for permission.
		if (!Events.canPlace(player, target.block, target.block.getRelative(BlockFace.DOWN))) {
			return false;
		}

		// Set the block on the happy ghast.
		target.block.setType(item.getType());
		target.blockDisplay.remove();
		targets.remove(player);
		Items.remove(item);
		Blocks.placeSound(target.block);
		return true;
	}

	/**
	 * Checks if the player is in range of a Happy Ghast to start target checking.
	 *
	 * @param player the player
	 */
	private void checkRange(@Nonnull Player player) {
		if (!hasPermission(player)) {
			inRange.remove(player);
			removeTarget(player);
			return;
		}

		for (Entity entity : Util.nearby(player.getLocation(), 5)) {
			if (entity instanceof HappyGhast ghast && ghast.isAdult()) {
				inRange.add(player);
				return;
			}
		}

		inRange.remove(player);
	}

	/**
	 * Checks the new target location by raytracing where the player is looking at the ghast.
	 *
	 * @param player the player
	 */
	private void checkTarget(@Nonnull Player player) {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (player.isInsideVehicle() || item.getType().isAir() || !item.getType().isBlock()) {
			removeTarget(player);
			return;
		}

		RayTraceResult result = Players.rayTrace(player, 5);
		if (result == null || !(result.getHitEntity() instanceof HappyGhast ghast) || !ghast.isAdult()) {
			removeTarget(player);
			return;
		}

		// Block cannot be on player, needs to be on air, and be on top of the ghast.
		Block block = result.getHitPosition().toLocation(player.getWorld()).getBlock();
		if (player.getLocation().getBlock().equals(block) || !block.getType().isAir() || block.getLocation().getBlockY() - ghast.getLocation().getBlockY() != 4) {
			removeTarget(player);
			return;
		}

		// Same target, ignore.
		Target existing = targets.get(player);
		if (existing != null && existing.block.equals(block)) {
			return;
		}

		// Remove old target.
		if (existing != null) {
			removeTarget(player);
		}

		BlockDisplay blockDisplay = Displays.createBlock(block.getLocation(), item.getType().createBlockData());
		blockDisplay.setGlowing(true);
		blockDisplay.setGlowColorOverride(Color.LIME);
		targets.put(player, new Target(block, blockDisplay));

		// Re-position the happy ghast down if necessary! :)
		Location location = ghast.getLocation();
		location.setY(location.getBlockY());
		ghast.teleport(location);
	}

	/**
	 * Removes the target from the player.
	 *
	 * @param player the player
	 */
	private void removeTarget(@Nonnull Player player) {
		Target target = targets.get(player);
		if (target != null) {
			target.blockDisplay.remove();
			targets.remove(player);
		}
	}

	private static class Target {
		private final Block block;
		private final BlockDisplay blockDisplay;

		public Target(@Nonnull Block block, @Nonnull BlockDisplay blockDisplay) {
			this.block = block;
			this.blockDisplay = blockDisplay;
		}
	}
}
