package me.colingrimes.tweaky;

import me.colingrimes.tweaky.command.TweaksCommand;
import me.colingrimes.tweaky.command.TweakyCommand;
import me.colingrimes.tweaky.config.implementation.Menus;
import me.colingrimes.tweaky.config.implementation.Messages;
import me.colingrimes.tweaky.config.implementation.Settings;
import me.colingrimes.tweaky.config.manager.ConfigurationManager;
import me.colingrimes.tweaky.listener.MenuListeners;
import me.colingrimes.tweaky.listener.PlayerListeners;
import me.colingrimes.tweaky.listener.TweakListeners;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.tweak.TweakManager;
import me.colingrimes.tweaky.update.UpdateCheckerSpigot;
import me.colingrimes.tweaky.util.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashSet;

public class Tweaky extends JavaPlugin {

	private static Tweaky instance;
	private TweakManager tweakManager;
	private ConfigurationManager configManager;
	private boolean isPaper = false;

	@Override
	public void onEnable() {
		instance = this;

		// Initialize settings.
		configManager = new ConfigurationManager(this);
		configManager.reload();

		// Setup commands + listeners.
		Bukkit.getPluginCommand("tweaky").setExecutor(new TweakyCommand(this));
		Bukkit.getPluginCommand("tweaks").setExecutor(new TweaksCommand(this));
		Bukkit.getPluginManager().registerEvents(new MenuListeners(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
		Bukkit.getPluginManager().registerEvents(new TweakListeners(this), this);
		Logger.log(this, "Registered all commands and events.");

		// Register all the tweaks.
		tweakManager = new TweakManager(this);
		tweakManager.register();

		// Check for Metrics.
		if (getSettings().ENABLE_METRICS.get()) {
			new Metrics(this, 25315);
		}

		// Check for updates.
		new UpdateCheckerSpigot(this, 123654);

		// Finished starting plugin.
		Logger.log(this, "Tweaky v" + getDescription().getVersion() + " has been fully enabled.");
	}

	@Override
	public void onDisable() {
		new HashSet<>(Gui.players.values()).forEach(Gui::invalidate);
		tweakManager.shutdown();
	}

	@Nonnull
	public static Tweaky getInstance() {
		return instance;
	}

	/**
	 * Gets the manager responsible for tweaks.
	 *
	 * @return the tweak manager
	 */
	@Nonnull
	public TweakManager getTweakManager() {
		return tweakManager;
	}

	/**
	 * Gets the manager responsible for the configurations.
	 *
	 * @return the configuration manager
	 */
	@Nonnull
	public ConfigurationManager getConfigManager() {
		return configManager;
	}

	/**
	 * Gets all the settings for the plugin.
	 *
	 * @return the plugin's settings
	 */
	@Nonnull
	public Settings getSettings() {
		return configManager.getSettings();
	}

	/**
	 * Gets all the menus for the plugin.
	 *
	 * @return the plugin's menus
	 */
	@Nonnull
	public Menus getMenus() {
		return configManager.getMenus();
	}

	/**
	 * Gets all the messages for the plugin.
	 *
	 * @return the plugin's messages
	 */
	@Nonnull
	public Messages getMessages() {
		return configManager.getMessages();
	}

	/**
	 * Checks if the server is running Paper.
	 *
	 * @return true if the server is a Paper server
	 */
	public boolean isPaper() {
		if (isPaper) {
			return true;
		}
		try {
			Class.forName("com.destroystokyo.paper.ParticleBuilder");
			isPaper = true;
			return true;
		} catch (ClassNotFoundException ignored) {
			return false;
		}
	}
}
