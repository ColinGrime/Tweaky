package me.colingrimes.tweaky.tweak.manager;

import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TweakQuery {

	private final boolean includeAll;
	private Player player;
	private TweakCategory category;

	/**
	 * Creates a tweak query for all tweaks, including disabled ones.
	 *
	 * @return the tweak query
	 */
	@Nonnull
	public static TweakQuery all() {
		return new TweakQuery(true);
	}

	/**
	 * Creates a tweak query for enabled tweaks only.
	 *
	 * @return the tweak query
	 */
	@Nonnull
	public static TweakQuery enabled() {
		return new TweakQuery(false);
	}

	private TweakQuery(boolean includeAll) {
		this.includeAll = includeAll;
	}

	/**
	 * Gets whether the query should include all tweaks.
	 * If false, only enabled tweaks will be processed.
	 *
	 * @return true if it should process all tweaks
	 */
	public boolean includeAll() {
		return includeAll;
	}

	/**
	 * Gets the player that should be queried.
	 *
	 * @return the player
	 */
	@Nullable
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player that should be queried.
	 *
	 * @param player the player
	 * @return the tweak query
	 */
	@Nonnull
	public TweakQuery forPlayer(@Nonnull Player player) {
		this.player = player;
		return this;
	}

	/**
	 * Gets the category that should be queried.
	 *
	 * @return the tweak category
	 */
	@Nullable
	public TweakCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category that should be queried.
	 *
	 * @param category the tweak category
	 * @return the tweak query
	 */
	@Nonnull
	public TweakQuery inCategory(@Nonnull TweakCategory category) {
		this.category = category;
		return this;
	}
}
