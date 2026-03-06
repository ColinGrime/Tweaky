package me.colingrimes.tweaky.util.io;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.logging.Level;

/**
 * Provides an easy way to log messages to the server.
 */
public final class Logger {

	/**
	 * Logs a normal message.
	 *
	 * @param msg the info message to log
	 */
	public static void log(@Nonnull String msg) {
		Bukkit.getServer().getLogger().log(Level.INFO, "[Tweaky] " + msg);
	}

	/**
	 * Logs a warning message.
	 *
	 * @param msg the warning message to log
	 */
	public static void warn(@Nonnull String msg) {
		Bukkit.getServer().getLogger().log(Level.WARNING, "[Tweaky] " + msg);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param msg the severe message to log
	 */
	public static void severe(@Nonnull String msg) {
		severe(msg, null);
	}

	/**
	 * Logs a severe message.
	 *
	 * @param msg the severe message to log
	 * @param thrown throwable associated with log message
	 */
	public static void severe(@Nonnull String msg, @Nullable Throwable thrown) {
		if (thrown == null) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "[Tweaky] " + msg);
		} else {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "[Tweaky] " + msg, thrown);
		}
	}

	private Logger() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}