package me.colingrimes.tweaky.tweak.properties;

import me.colingrimes.tweaky.config.Option;
import me.colingrimes.tweaky.config.manager.ConfigurationProvider;
import me.colingrimes.tweaky.menu.tweak.TweakMenu;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.util.bukkit.Events;
import org.bukkit.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Contains the various properties of a tweak.
 * This includes:
 * <ul>
 *     <li>The {@link TweakCategory} to categorize the tweak within the {@link TweakMenu}.</li>
 *     <li>The permission requirement of the tweak.</li>
 *     <li>An optional {@link Events.Guard} used to pre-filter all {@link TweakHandler} events within the tweak.</li>
 * </ul>
 */
public class TweakProperties {

	private final Events.Guard guard = Events.guard();
	private TweakCategory category = TweakCategory.UNKNOWN;
	private boolean permissionRequired = true;
	private Option<Set<String>> worldBlacklist = new Option<>() {
		@Nonnull
		@Override
		public Set<String> get() {
			return Set.of();
		}

		@Override
		public void reload(@Nullable ConfigurationProvider provider) {}
	};

	/**
	 * Gets the {@link Events.Guard} used to pre-filter all listeners of the tweak.
	 *
	 * @return the event guard
	 */
	@Nonnull
	public Events.Guard getGuard() {
		return guard;
	}

	/**
	 * Sets the category of the tweak.
	 *
	 * @param category the tweak category
	 */
	public void setCategory(@Nonnull TweakCategory category) {
		this.category = category;
	}

	/**
	 * Gets the category of the tweak.
	 *
	 * @return the tweak category
	 */
	@Nonnull
	public TweakCategory getCategory() {
		return category;
	}

	/**
	 * Sets the world blacklist option for the tweak.
	 *
	 * @param worldBlacklist the option holding the blacklisted world names
	 */
	public void setWorldBlacklist(@Nonnull Option<Set<String>> worldBlacklist) {
		this.worldBlacklist = worldBlacklist;
	}

	/**
	 * Checks whether the tweak is blacklisted in the given world.
	 *
	 * @param world the world to check (may be null if unknown)
	 * @return true if the tweak should not run in the world
	 */
	public boolean isWorldBlacklisted(@Nullable World world) {
		if (world == null) {
			return false;
		}

		Set<String> blacklist = worldBlacklist.get();
		return !blacklist.isEmpty() && blacklist.contains(world.getName().toLowerCase());
	}

	/**
	 * Gets the permission requirement for the tweak.
	 *
	 * @return true if permission is required
	 */
	public boolean isPermissionRequired() {
		return permissionRequired;
	}

	/**
	 * Sets the permission requirement for the tweak.
	 *
	 * @param permissionRequired true if permission is required
	 */
	public void setPermissionRequired(boolean permissionRequired) {
		this.permissionRequired = permissionRequired;
	}
}
