package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class VillagerFollowTweak extends DefaultTweak {

	private final Set<Villager> villagers = new HashSet<>();
	private Task playerTask, villagerTask;

	public VillagerFollowTweak(@Nonnull Tweaky plugin) {
		super(plugin, "villager_follow");
	}

	@Override
	public void init() {
		playerTask = Scheduler.sync().runRepeating(() -> Players.forEach(this::checkPlayer), 10L, 10L);
		villagerTask = Scheduler.sync().runRepeating(() -> new HashSet<>(villagers).forEach(this::checkVillager), 10L, 10L);
	}

	@Override
	public void shutdown() {
		villagers.clear();
		playerTask.stop();
		villagerTask.stop();
	}

	/**
	 * If the player is holding an emerald, this will add all nearby villagers
	 * within 20 blocks of the player to activate following.
	 * <p>
	 * This doesn't guarantee all villagers within 10 blocks will follow the specific player.
	 * Instead, all villagers within 10 blocks are later checked for the closest player that is holding
	 * an emerald. If another player holding an emerald is closer, they will follow that player instead.
	 *
	 * @param player the player
	 */
	private void checkPlayer(@Nonnull Player player) {
		if (hasPermission(player) && holdingEmerald(player)) {
			villagers.addAll(Util.nearby(Villager.class, player.getLocation(), 20));
		}
	}

	/**
	 * Checks the villager for the closest player that is holding an emerald.
	 * Once found, it will start pathfinding towards that player.
	 *
	 * @param villager the villager
	 */
	private void checkVillager(@Nonnull Villager villager) {
		// Prevent weird glitchy movement from being in bed.
		if (villager.isSleeping()) {
			villager.getPathfinder().stopPathfinding();
			villagers.remove(villager);
			return;
		}

		Player closest = null;
		double closestDistance = Double.MAX_VALUE;
		for (Player player : villager.getWorld().getNearbyPlayers(villager.getLocation(), 20)) {
			if (!hasPermission(player) || !holdingEmerald(player)) {
				continue;
			}
			double distance = player.getLocation().distanceSquared(villager.getLocation());
			if (distance < closestDistance) {
				closest = player;
				closestDistance = distance;
			}
		}

		if (closest != null) {
			villager.getPathfinder().moveTo(closest, 0.8);
		} else {
			villager.getPathfinder().stopPathfinding();
			villagers.remove(villager);
		}
	}

	/**
	 * Checks if the player is holding an emerald.
	 *
	 * @param player the player
	 * @return true if the player is holding an emerald
	 */
	private boolean holdingEmerald(@Nonnull Player player) {
		PlayerInventory inv = player.getInventory();
		return inv.getItemInMainHand().getType() == Material.EMERALD || inv.getItemInOffHand().getType() == Material.EMERALD;
	}
}
