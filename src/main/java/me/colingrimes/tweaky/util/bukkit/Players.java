package me.colingrimes.tweaky.util.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Players {

	/**
	 * Returns a collection of all online players.
	 *
	 * @return all online players
	 */
	@Nonnull
	public static Collection<? extends Player> all() {
		return Bukkit.getServer().getOnlinePlayers();
	}

	/**
	 * Performs the given action for each online player.
	 *
	 * @param action the action to perform
	 */
	public static void forEach(@Nonnull Consumer<? super Player> action) {
		all().forEach(action);
	}

	/**
	 * Returns a stream of all online players, filtered by the given predicate.
	 *
	 * @param predicate the predicate
	 * @return the stream of players
	 */
	@Nonnull
	public static Stream<? extends Player> filter(@Nonnull Predicate<? super Player> predicate) {
		return all().stream().filter(predicate);
	}

	/**
	 * Plays the given sound to the given player.
	 *
	 * @param player the player
	 * @param sound  the sound
	 */
	public static void sound(@Nonnull Player player, @Nonnull Sound sound) {
		player.playSound(player.getLocation(), sound, 1F, 1F);
	}

	/**
	 * Performs a general ray trace from the player's eye.
	 *
	 * @param player the player
	 * @param distance the distance to check
	 * @return the ray trace result
	 */
	@Nullable
	public static RayTraceResult rayTrace(@Nonnull Player player, double distance) {
		Location eye = player.getEyeLocation();
		return player.getWorld().rayTrace(eye, eye.getDirection(), distance, FluidCollisionMode.NEVER, true, 0, e -> !e.equals(player));
	}

	/**
	 * Performs a ray trace from the player's eye that checks for entities.
	 *
	 * @param player the player
	 * @param distance the distance to check
	 * @return the ray trace result
	 */
	@Nullable
	public static RayTraceResult rayTraceEntities(@Nonnull Player player, double distance) {
		Location eye = player.getEyeLocation();
		return player.getWorld().rayTraceEntities(eye, eye.getDirection(), distance, 0, e -> !e.equals(player));
	}

	/**
	 * Performs a ray trace from the player's eye that checks for blocks.
	 *
	 * @param player the player
	 * @param distance the distance to check
	 * @return the ray trace result
	 */
	@Nullable
	public static RayTraceResult rayTraceBlocks(@Nonnull Player player, double distance) {
		Location eye = player.getEyeLocation();
		return player.getWorld().rayTraceBlocks(eye, eye.getDirection(), distance, FluidCollisionMode.NEVER, true);
	}

	private Players() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}