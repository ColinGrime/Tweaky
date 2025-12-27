package me.colingrimes.tweaky.update;

import me.colingrimes.tweaky.Tweaky;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateCheckerSpigot implements UpdateChecker, Listener {

	private final Tweaky plugin;
	private final String updateUrl;
	private final String resourceUrl;
	private final String current;
	private String latest;

	public UpdateCheckerSpigot(@Nonnull Tweaky plugin, int resourceId) {
		this.plugin = plugin;
		this.current = plugin.getDescription().getVersion();
		this.updateUrl = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;
		this.resourceUrl = "https://www.spigotmc.org/resources/" + resourceId;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		if (plugin.getSettings().UPDATE_CHECKER_LOG.get()) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
				if (check()) {
					log(Bukkit.getConsoleSender());
				}
			}, 10L);
		}
	}

	@Override
	public boolean check() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(updateUrl).openStream()))) {
			latest = reader.readLine();
			if (!current.equals(latest)) {
				return true;
			}
		} catch (IOException ignored) {
			// Silent failure is fine; logging isn't critical here.
		}
		return false;
	}

	@Override
	public void log(@Nonnull CommandSender sender) {
		plugin.getSettings().UPDATE_CHECKER_MESSAGE
				.replace("{latest}", latest)
				.replace("{current}", current)
				.replace("{download}", resourceUrl)
				.send(sender);
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("tweaky.admin")) {
			return;
		}

		Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
			if (!check()) {
				return;
			}
			if (plugin.getSettings().UPDATE_CHECKER_NOTIFY.get()) {
				log(player);
			}
			if (plugin.getSettings().UPDATE_CHECKER_LOG.get()) {
				log(Bukkit.getConsoleSender());
			}
		}, 10L);
	}
}