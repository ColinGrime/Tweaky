package me.colingrimes.tweaky.util.bukkit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Convenience methods for global sounds.
 * Use {@link Players#sound(Player, Sound)} for a player-specific sound.
 */
public final class Sounds {

	/**
	 * Plays the sound at the given location.
	 *
	 * @param location the location to play the sound
	 * @param sound the sound to play
	 */
	public static void play(@Nonnull Location location, @Nullable Sound sound) {
		if (location.getWorld() != null && sound != null) {
			location.getWorld().playSound(location, sound, 1F, 1F);
		}
	}

	/**
	 * Plays the sound at the entity's location.
	 *
	 * @param entity the entity
	 * @param sound the sound to play
	 */
	public static void play(@Nonnull Entity entity, @Nullable Sound sound) {
		play(entity.getLocation(), sound);
	}

	/**
	 * Plays the sound at the block's location.
	 *
	 * @param block the block
	 * @param sound the sound to play
	 */
	public static void play(@Nonnull Block block, @Nullable Sound sound) {
		play(block.getLocation(), sound);
	}
}
