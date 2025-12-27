package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public class VillagerFollowTweak extends Tweak {

	private final Set<Villager> villagers = new HashSet<>();
	private BukkitTask playerTask, villagerTask;

	public VillagerFollowTweak(@Nonnull Tweaky plugin) {
		super(plugin, "villager_follow");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_VILLAGER_FOLLOW.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_VILLAGER_FOLLOW.get().material(Material.VILLAGER_SPAWN_EGG);
	}

	@Override
	public void init() {
		playerTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> Players.forEach(this::checkPlayer), 10L, 10L);
		villagerTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> new HashSet<>(villagers).forEach(this::checkVillager), 10L, 10L);
	}

	@Override
	public void shutdown() {
		villagers.clear();
		playerTask.cancel();
		villagerTask.cancel();
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
