package me.colingrimes.tweaky.config.manager;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.config.implementation.Menus;
import me.colingrimes.tweaky.config.implementation.Messages;
import me.colingrimes.tweaky.config.implementation.Settings;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the configurations for the plugin.
 */
public class ConfigurationManager {

	private final List<Configuration> configs = new ArrayList<>();
	private final Tweaky plugin;
	private final Settings settings;
	private final Menus menus;
	private final Messages messages;

	public ConfigurationManager(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
		this.settings = new Settings(plugin);
		this.menus = new Menus(plugin);
		this.messages = new Messages(plugin);
		addConfiguration(settings);
		addConfiguration(menus);
		addConfiguration(messages);
	}

	/**
	 * Adds a configuration to the manager.
	 *
	 * @param config the configuration
	 */
	private void addConfiguration(@Nonnull Configuration config) {
		configs.add(config);
	}

	/**
	 * Reloads all configuration files for the plugin.
	 */
	public void reload() {
		plugin.reloadConfig();
		configs.forEach(Configuration::reload);
	}

	/**
	 * Gets all the settings for the plugin.
	 *
	 * @return the plugin's settings
	 */
	@Nonnull
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Gets all the menu data for the plugin.
	 *
	 * @return the plugin's menus
	 */
	@Nonnull
	public Menus getMenus() {
		return menus;
	}

	/**
	 * Gets all the messages for the plugin.
	 *
	 * @return the plugin's messages
	 */
	@Nonnull
	public Messages getMessages() {
		return messages;
	}
}