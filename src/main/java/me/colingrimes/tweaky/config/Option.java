package me.colingrimes.tweaky.config;

import me.colingrimes.tweaky.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class Option<T> {

	private final Function<FileConfiguration, T> function;
	private T value;

	public Option(@Nonnull Function<FileConfiguration, T> function) {
		this.function = function;
	}

	/**
	 * The value of the option.
	 *
	 * @return the value
	 */
	@Nonnull
	public T get() {
		return value;
	}

	/**
	 * Sends the value of the option to the sender.
	 *
	 * @param sender the sender
	 */
	public void send(@Nonnull CommandSender sender) {
		sender.sendMessage(Util.color(String.valueOf(value)));
	}

	/**
	 * Reloads the value of the option.
	 *
	 * @param config the configuration option
	 */
	public void reload(@Nonnull FileConfiguration config) {
		this.value = function.apply(config);
	}
}
