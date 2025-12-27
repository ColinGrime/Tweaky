package me.colingrimes.tweaky.update;

import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * Responsible for looking up if there are any updates available for the plugin.
 */
public interface UpdateChecker {

	/**
	 * Checks for any updates available for the plugin.
	 *
	 * @return true if there are updates
	 */
	boolean check();

	/**
	 * Logs the update to the given {@link CommandSender}.
	 *
	 * @param sender the sender
	 */
	void log(@Nonnull CommandSender sender);
}