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
	private Settings settings;
	private Menus menus;
	private Messages messages;

	public ConfigurationManager(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	/**
	 * Initializes all configuration files for the plugin.
	 */
	public void init() {
		settings = new Settings(plugin);
		menus = new Menus(plugin);
		messages = new Messages(plugin);
		configs.add(settings);
		configs.add(menus);
		configs.add(messages);
		reload();
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