package me.colingrimes.tweaky.config.manager;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.util.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Responsible for providing a simple interface for accessing configuration values.
 */
public class ConfigurationProvider {

	private final File configFile;
	private FileConfiguration config;

	public ConfigurationProvider(@Nonnull Tweaky plugin, @Nonnull String name) {
		this.configFile = new File(plugin.getDataFolder(), name);
		this.init(plugin, name.endsWith(".yml") ? name.toLowerCase() : name.toLowerCase() + ".yml");
		this.reload();
	}

	/**
	 * Initializes the configuration file.
	 * This gets the current configuration, re-generates a new one, and migrates over the values of the old one.
	 *
	 * @param plugin the plugin
	 * @param name the name of the config file
	 */
	private void init(@Nonnull Tweaky plugin, @Nonnull String name) {
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
		}

		// Gets the current config file.
		FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(configFile);

		// Re-generates a new one.
		plugin.saveResource(name, true);
		FileConfiguration newConfig = YamlConfiguration.loadConfiguration(configFile);

		// Migrates over the values of the old one.
		for (String key : newConfig.getKeys(true)) {
			if (!newConfig.isConfigurationSection(key) && oldConfig.contains(key) && !oldConfig.isConfigurationSection(key)) {
				newConfig.set(key, oldConfig.get(key));
			}
		}

		// Saves the new config.
		try {
			newConfig.save(configFile);
		} catch (IOException e) {
			Logger.severe(plugin, "Error saving Configuration File '" + name + "': " + e.getMessage());
		}

		Logger.log(plugin, "Loaded the " + name + " file.");
	}

	/**
	 * Reloads the configuration file with the latest values.
	 */
	public void reload() {
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	/**
	 * Gets a configuration section from the configuration.
	 *
	 * @param path the path to the configuration section
	 * @return the configuration section, if present
	 */
	@Nonnull
	public Optional<ConfigurationSection> getSection(@Nonnull String path) {
		return Optional.ofNullable(config.getConfigurationSection(path));
	}

	/**
	 * Gets a string from the configuration.
	 *
	 * @param path the path to the string
	 * @return the string, if present
	 */
	@Nonnull
	public Optional<String> getString(@Nonnull String path) {
		return Optional.ofNullable(config.getString(path));
	}

	/**
	 * Gets a list of strings from the configuration.
	 *
	 * @param path the path to the list
	 * @return the list, if present
	 */
	@Nonnull
	public Optional<List<String>> getStringList(@Nonnull String path) {
		List<String> list = config.getStringList(path);
		if (!list.isEmpty()) {
			return Optional.of(list);
		}

		Optional<String> value = getString(path);
		return value.map(List::of).or(Optional::empty);
	}

	/**
	 * Gets an integer from the configuration.
	 *
	 * @param path the path to the integer
	 * @return the integer, if present
	 */
	@Nonnull
	public Optional<Integer> getInteger(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Integer.class));
	}

	/**
	 * Gets a double from the configuration.
	 *
	 * @param path the path to the double
	 * @return the double, if present
	 */
	@Nonnull
	public Optional<Double> getDouble(@Nonnull String path) {
		Object decimal = config.get(path);
		if (decimal instanceof Number number) {
			return Optional.of(number.doubleValue());
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Gets a boolean from the configuration.
	 *
	 * @param path the path to the boolean
	 * @return the boolean, if present
	 */
	@Nonnull
	public Optional<Boolean> getBoolean(@Nonnull String path) {
		return Optional.ofNullable(config.getObject(path, Boolean.class));
	}
}
