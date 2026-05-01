package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.menu.tweak.util.TweakItem;
import me.colingrimes.tweaky.tweak.manager.TweakQuery;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.MultiTweak;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

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

	/**
	 * Gets the number of enabled tweaks from the list of tweaks.
	 *
	 * @param tweaks the tweaks to count
	 * @return the number of tweaks
	 */
	static int count(@Nonnull Collection<Tweak> tweaks) {
		return count(tweaks, TweakQuery.enabled());
	}

	/**
	 * Gets the number of tweaks from the list of tweaks.
	 *
	 * @param tweaks the tweaks to count
	 * @param query the tweak query
	 * @return the number of tweaks
	 */
	static int count(@Nonnull Collection<Tweak> tweaks, @Nonnull TweakQuery query) {
		if (query.includeAll()) {
			return tweaks.stream().mapToInt(tweak -> tweak instanceof MultiTweak multi ? multi.getTotalCount() : 1).sum();
		}

		return tweaks.stream()
				.filter(Tweak::isEnabled)
				.mapToInt(tweak -> tweak instanceof MultiTweak multi ? multi.getCount() : 1)
				.sum();
	}
}
