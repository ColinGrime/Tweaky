package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.implementation.Menus;
import me.colingrimes.tweaky.config.implementation.Messages;
import me.colingrimes.tweaky.config.implementation.Settings;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Tweak implements Listener {

	protected final Tweaky plugin;
	protected final Settings settings;
	protected final Menus menus;
	protected final Messages msg;
	protected final String id;

	public Tweak(@Nonnull Tweaky plugin, @Nonnull String id) {
		this.plugin = plugin;
		this.settings = plugin.getSettings();
		this.menus = plugin.getMenus();
		this.msg = plugin.getMessages();
		this.id = id;
		if (isEnabled()) {
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
	}

	/**
	 * Gets the ID of the tweak.
	 *
	 * @return the tweak ID
	 */
	@Nonnull
	public String getId() {
		return id;
	}

	/**
	 * Gets whether the tweak is enabled.
	 *
	 * @return true if the tweak is enabled
	 */
	public abstract boolean isEnabled();

	/**
	 * Gets the number of tweaks that are enabled by the tweak class.
	 *
	 * @return the number of enabled tweaks
	 */
	public int getCount() {
		return isEnabled() ? 1 : 0;
	}

	/**
	 * Gets the GUI item representing this tweak.
	 *
	 * @return the gui item
	 */
	@Nonnull
	public TweakItem getGuiItem() {
		return TweakItem.create("No implementation: " + id);
	}

	/**
	 * Runs when the tweak is initialized.
	 */
	public void init() {}

	/**
	 * Runs when the tweak is shutdown.
	 */
	public void shutdown() {}

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
	public boolean hasPermission(@Nullable Entity entity) {
		if (!(entity instanceof Player player)) {
			return true;
		}

		String permission = "tweaky.tweaks." + id.replace("_", "-");
		String negation = "-" + permission;
		if (player.hasPermission(negation)) {
			return player.hasPermission(permission);
		}
		return true;
	}
}
