package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Tweak extends Listener {

	/**
	 * Runs when the tweak is enabled.
	 */
	default void enable() {}

	/**
	 * Runs when the tweak is disabled.
	 */
	default void disable() {}

	/**
	 * Gets the ID of the tweak.
	 *
	 * @return the tweak ID
	 */
	@Nonnull
	String getId();

	/**
	 * Gets whether the tweak is enabled.
	 *
	 * @return true if the tweak is enabled
	 */
	boolean isEnabled();

	/**
	 * Gets the number of tweaks that are enabled by the tweak class.
	 *
	 * @return the number of enabled tweaks
	 */
	default int getCount() {
		return isEnabled() ? 1 : 0;
	}

	/**
	 * Gets the GUI item representing this tweak.
	 *
	 * @return the gui item
	 */
	@Nonnull
	TweakItem getGuiItem();

	/**
	 * Gets the various properties of the tweak.
	 *
	 * @return the tweak properties
	 */
	@Nonnull
	TweakProperties getProperties();

	/**
	 * Checks if the player has permission to use the tweak.
	 * By default, no permission is needed to use the tweak.
	 * <p>
	 * To require permission to use the tweak, players need to be
	 * given the negated permission node for the tweak (e.g. "tweaky.tweaks.villager-follow").
	 * <p>
	 * After negating permission, the player can no longer use the tweak until
	 * you remove the negation or add the "tweaky.tweaks.villager-follow" permission.
	 *
	 * @param entity the entity
	 * @return true if they have permission to use the tweak
	 */
	default boolean hasPermission(@Nullable Entity entity) {
		if (!getProperties().isPermissionRequired() || !(entity instanceof Player player)) {
			return true;
		}

		String permission = "tweaky.tweaks." + getId().replace("_", "-");
		String negation = "-" + permission;
		if (player.hasPermission(negation)) {
			return player.hasPermission(permission);
		}
		return true;
	}
}
