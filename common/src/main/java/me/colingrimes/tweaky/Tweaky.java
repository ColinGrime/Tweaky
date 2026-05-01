package me.colingrimes.tweaky;

import me.colingrimes.tweaky.command.manager.CommandManager;
import me.colingrimes.tweaky.config.implementation.Menus;
import me.colingrimes.tweaky.config.implementation.Messages;
import me.colingrimes.tweaky.config.implementation.Settings;
import me.colingrimes.tweaky.config.manager.ConfigurationManager;
import me.colingrimes.tweaky.listener.MenuListeners;
import me.colingrimes.tweaky.listener.PlayerListeners;
import me.colingrimes.tweaky.listener.TweakListeners;
import me.colingrimes.tweaky.menu.manager.MenuManager;
import me.colingrimes.tweaky.message.Message;
import me.colingrimes.tweaky.message.MessageService;
import me.colingrimes.tweaky.tweak.manager.TweakManager;
import me.colingrimes.tweaky.update.UpdateCheckerSpigot;
import me.colingrimes.tweaky.util.io.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public abstract class Tweaky extends JavaPlugin {

	private static Tweaky instance;
	private TweakManager tweakManager;
	private ConfigurationManager configManager;
	private CommandManager commandManager;
	private MenuManager menuManager;

	@Override
	public void onEnable() {
		instance = this;
		Message.init(getMessageService());

		// Initialize settings.
		configManager = new ConfigurationManager(this);
		configManager.init();

		// Initialize commands.
		commandManager = new CommandManager(this);
		commandManager.init();
		Logger.log("Registered main commands.");

		// Initialize menus.
		menuManager = new MenuManager();

		// Initialize listeners.
		Bukkit.getPluginManager().registerEvents(new MenuListeners(this), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
		Bukkit.getPluginManager().registerEvents(new TweakListeners(this), this);
		Logger.log("Registered all events.");

		// Register all the tweaks.
		tweakManager = new TweakManager(this);
		tweakManager.init();
		tweakManager.register();

		// Check for Metrics.
		if (getSettings().ENABLE_METRICS.get()) {
			new Metrics(this, 25315);
		}

		// Check for updates.
		new UpdateCheckerSpigot(this, 123654);

		// Finished starting plugin.
		Logger.log(getVersion() + " has been fully enabled.");
	}

	@Override
	public void onDisable() {
		if (menuManager != null) {
			menuManager.shutdown();
		}
		if (commandManager != null) {
			commandManager.shutdown();
		}
		if (tweakManager != null) {
			tweakManager.shutdown();
		}
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
	 * Gets the manager responsible for commands.
	 *
	 * @return the command manager
	 */
	@Nonnull
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * Gets the manager responsible for menus.
	 *
	 * @return the menu manager
	 */
	@Nonnull
	public MenuManager getMenuManager() {
		return menuManager;
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
	 * Gets the message service to initialize for the {@link Message} class.
	 *
	 * @return the message service
	 */
	@Nonnull
	public abstract MessageService getMessageService();

	/**
	 * Gets the version string of the plugin.
	 *
	 * @return the version string
	 */
	@Nonnull
	public abstract String getVersion();

	/**
	 * Checks if the server is running Paper.
	 *
	 * @return true if the server is a Paper server
	 */
	public abstract boolean isPaper();
}
