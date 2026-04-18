package me.colingrimes.tweaky.command.manager;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.command.TweaksCommand;
import me.colingrimes.tweaky.command.TweakyCommand;
import me.colingrimes.tweaky.util.io.Logger;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;

import javax.annotation.Nonnull;

public class CommandManager {

	private final Tweaky plugin;

	public CommandManager(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	/**
	 * Initializes all commands for the plugin.
	 */
	public void init() {
		register("tweaky", new TweakyCommand(plugin));
		register("tweaks", new TweaksCommand(plugin));
	}

	/**
	 * Cleans up all registered commands for the plugin.
	 */
	public void shutdown() {
		unregister("tweaky");
		unregister("tweaks");
	}

	/**
	 * Registers the given command.
	 *
	 * @param name the command string
	 * @param executor the command executor
	 */
	public void register(@Nonnull String name, @Nonnull CommandExecutor executor) {
		PluginCommand command = plugin.getCommand(name);
		if (command == null) {
			throw new IllegalStateException("Command '" + name + "' is not defined in the plugin.yml.");
		}

		command.setExecutor(executor);
		Logger.log("Registered the '/" + name + "' command.");
	}

	/**
	 * Unregisters the given command. If the command is not found, does nothing.
	 *
	 * @param name the command string
	 */
	public void unregister(@Nonnull String name) {
		PluginCommand command = plugin.getCommand(name);
		if (command != null) {
			command.setExecutor(null);
			Logger.log("Unregistered the '/" + name + "' command.");
		}
	}
}
