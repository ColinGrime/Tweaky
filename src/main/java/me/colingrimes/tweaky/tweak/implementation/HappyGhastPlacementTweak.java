package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.display.Displays;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HappyGhast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class HappyGhastPlacementTweak extends Tweak {

	private final Map<Player, Target> targets = new HashMap<>();
	private BukkitTask task;

	public HappyGhastPlacementTweak(@Nonnull Tweaky plugin) {
		super(plugin, "happy_ghast_placement");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_HAPPY_GHAST_PLACEMENT.get();
	}

	@Override
	public void init() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> Players.forEach(this::getTarget), 10L, 2L);
	}

	@Override
	public void shutdown() {
		task.cancel();
		targets.values().forEach(t -> t.blockDisplay.remove());
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getHand() == EquipmentSlot.HAND && placeOnGhast(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
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

		// Set the block on the happy ghast.
		Target target = targets.get(player);
		target.block.setType(item.getType());
		target.blockDisplay.remove();
		targets.remove(player);
		Items.remove(item);
		Blocks.placeSound(target.block);
		return true;
	}

	/**
	 * Gets the new target location by raytracing where the player is looking at the ghast.
	 *
	 * @param player the player
	 */
	private void getTarget(@Nonnull Player player) {
		Target existing = targets.get(player);
		ItemStack item = player.getInventory().getItemInMainHand();
		if (player.isInsideVehicle() || item.getType().isAir() || !item.getType().isBlock()) {
			removeTarget(player, existing);
			return;
		}

		RayTraceResult result = Players.rayTrace(player, 5);
		if (result == null || !(result.getHitEntity() instanceof HappyGhast ghast) || !ghast.isAdult()) {
			removeTarget(player, existing);
			return;
		}

		// Block cannot be on player, needs to be on air, and be on top of the ghast.
		Block block = result.getHitPosition().toLocation(player.getWorld()).getBlock();
		if (player.getLocation().getBlock().equals(block) || !block.getType().isAir() || block.getLocation().getBlockY() - ghast.getLocation().getBlockY() != 4) {
			removeTarget(player, existing);
			return;
		}

		// Same target, ignore.
		if (existing != null && existing.block.equals(block)) {
			return;
		}

		// Remove old target.
		if (existing != null) {
			removeTarget(player, existing);
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
	 * @param target the target
	 */
	private void removeTarget(@Nonnull Player player, @Nullable Target target) {
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
