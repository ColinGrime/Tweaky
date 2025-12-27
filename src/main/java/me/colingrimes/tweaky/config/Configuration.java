package me.colingrimes.tweaky.config;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.manager.ConfigurationProvider;
import me.colingrimes.tweaky.config.option.MessageOption;
import me.colingrimes.tweaky.config.option.Option;
import me.colingrimes.tweaky.config.option.SimpleOption;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class Configuration {

	private final ConfigurationProvider provider;
	private final List<Option<?>> options;

	public Configuration(@Nonnull Tweaky plugin, @Nonnull String name) {
		this.provider = new ConfigurationProvider(plugin, name);
		this.options = new ArrayList<>();
	}

	/**
	 * Reloads the settings, updating the values of all options.
	 */
	public void reload() {
		provider.reload();
		options.forEach(option -> option.reload(provider));
	}

	/**
	 * Registers the given option.
	 *
	 * @param option the option
	 * @return the option that was registered
	 */
	@Nonnull
	protected <T> Option<T> register(@Nonnull Option<T> option) {
		options.add(option);
		return option;
	}

	/**
	 * Creates a {@link Boolean} option.
	 *
	 * @param path the path to the boolean
	 * @return the option for the boolean or false if it doesn't exist
	 */
	@Nonnull
	protected Option<Boolean> option(@Nonnull String path) {
		return register(new SimpleOption<>(provider -> provider.getBoolean(path).orElse(false)));
	}

	/**
	 * Creates a {@link String} option.
	 *
	 * @param path the path to the string
	 * @param def the default value
	 * @return the option for the string
	 */
	@Nonnull
	protected Option<String> option(@Nonnull String path, @Nonnull String def) {
		return register(new SimpleOption<>(provider -> provider.getString(path).orElse(def)));
	}

	/**
	 * Creates a {@link List} of {@link String}s option.
	 *
	 * @param path the path to the list
	 * @param def the default value
	 * @return the option for the list
	 */
	@Nonnull
	protected Option<List<String>> option(@Nonnull String path, @Nonnull List<String> def) {
		return register(new SimpleOption<>(provider -> provider.getStringList(path).orElse(def)));
	}

	/**
	 * Creates an {@link Integer} option.
	 *
	 * @param path the path to the integer
	 * @param def the default value
	 * @return the option for the integer
	 */
	@Nonnull
	protected Option<Integer> option(@Nonnull String path, int def) {
		return register(new SimpleOption<>(provider -> provider.getInteger(path).orElse(def)));
	}

	/**
	 * Creates a {@link Double} option.
	 *
	 * @param path the path to the double
	 * @param def the default value
	 * @return the option for the double
	 */
	@Nonnull
	protected Option<Double> option(@Nonnull String path, double def) {
		return register(new SimpleOption<>(provider -> provider.getDouble(path).orElse(def)));
	}

	/**
	 * Creates a {@link Boolean} option.
	 *
	 * @param path the path to the boolean
	 * @param def the default value
	 * @return the option for the boolean
	 */
	@Nonnull
	protected Option<Boolean> option(@Nonnull String path, boolean def) {
		return register(new SimpleOption<>(provider -> provider.getBoolean(path).orElse(def)));
	}

	/**
	 * Creates a custom option using the provided extractor function.
	 *
	 * @param path the configuration path
	 * @param extractor a function that extracts the custom option
	 * @param <T> the type of the option
	 * @return the custom option
	 */
	@Nonnull
	protected <T> Option<T> option(@Nonnull String path, @Nonnull Function<ConfigurationSection, T> extractor) {
		return register(new SimpleOption<>(provider -> extractor.apply(provider.getSection(path).orElse(null))));
	}

	/**
	 * Creates a {@link String} message.
	 *
	 * @param path the path to the message
	 * @param def the default value
	 * @return the message option
	 */
	@Nonnull
	protected MessageOption<String> message(@Nonnull String path, @Nonnull String def) {
		MessageOption<String> message = new MessageOption<>(provider -> provider.getString(path).orElse(def));
		register(message);
		return message;
	}

	/**
	 * Creates a {@link List} of {@link String}s message.
	 *
	 * @param path the path to the message
	 * @param def the default value
	 * @return the message option
	 */
	@Nonnull
	protected MessageOption<List<String>> message(@Nonnull String path, @Nonnull List<String> def) {
		MessageOption<List<String>> message = new MessageOption<>(provider -> provider.getStringList(path).orElse(def));
		register(message);
		return message;
	}

	/**
	 * Creates a {@link List} of {@link String}s message.
	 *
	 * @param path the path to the message
	 * @param def the default value
	 * @return the message option
	 */
	@Nonnull
	protected MessageOption<List<String>> message(@Nonnull String path, @Nonnull String...def) {
		MessageOption<List<String>> message = new MessageOption<>(provider -> provider.getStringList(path).orElse(List.of(def)));
		register(message);
		return message;
	}
}