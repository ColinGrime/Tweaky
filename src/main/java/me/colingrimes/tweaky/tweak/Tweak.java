package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;

public abstract class Tweak implements Listener {

	protected final Tweaky plugin;
	protected final Settings settings;
	protected final String id;

	public Tweak(@Nonnull Tweaky plugin, @Nonnull String id) {
		this.plugin = plugin;
		this.settings = plugin.getSettings();
		this.id = id;
		if (isEnabled()) {
			Bukkit.getPluginManager().registerEvents(this, plugin);
		}
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
	 * Runs when the tweak is initialized.
	 */
	public void init() {}

	/**
	 * Runs when the tweak is shutdown.
	 */
	public void shutdown() {}
}
