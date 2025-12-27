package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.message.Message;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.NBT;
import me.colingrimes.tweaky.util.bukkit.Players;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.*;

public class CoordinatesTweak extends Tweak implements CommandExecutor {

	private static final String COORDINATES_HIDE_KEY = "coordinates_hide";
	private final Set<UUID> hide = new HashSet<>();
	private BukkitTask task;

	public CoordinatesTweak(@Nonnull Tweaky plugin) {
		super(plugin, "coordinates");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_COORDINATES.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_COORDINATES.get().material(Material.PLAYER_HEAD);
	}

	@Override
	public void init() {
		Bukkit.getPluginCommand("coordinates").setExecutor(this);
		Players.filter(p -> NBT.hasTag(p, COORDINATES_HIDE_KEY, Boolean.class)).forEach(p -> hide.add(p.getUniqueId()));
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> Players.filter(p -> !hide.contains(p.getUniqueId())).forEach(this::sendCoordinates), 0L, 30L);
	}

	@Override
	public void shutdown() {
		Bukkit.getPluginCommand("coordinates").setExecutor(null);
		hide.clear();
		task.cancel();
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player) || !hasPermission(player)) {
			return true;
		}
		if (hide.contains(player.getUniqueId())) {
			hide.remove(player.getUniqueId());
			NBT.removeTag(player, COORDINATES_HIDE_KEY);
			Players.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING);
			sendCoordinates(player);
		} else {
			hide.add(player.getUniqueId());
			NBT.setTag(player, COORDINATES_HIDE_KEY, true);
			Players.sound(player, Sound.BLOCK_NOTE_BLOCK_BASS);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent());
		}
		return true;
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		if (NBT.hasTag(event.getPlayer(), COORDINATES_HIDE_KEY, Boolean.class)) {
			hide.add(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		hide.remove(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		if (event.getTo() == null || hide.contains(event.getPlayer().getUniqueId())) {
			return;
		}

		Location from = event.getFrom();
		Location to = event.getTo();
		if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
			sendCoordinates(event.getPlayer());
		}
	}

	/**
	 * Displays the Coordinates HUD for the specified player.
	 *
	 * @param player the player
	 */
	private void sendCoordinates(@Nonnull Player player) {
		if (!hasPermission(player)) {
			return;
		}

		Location location = player.getLocation();
		Message<?> message = player.getWorld().getTime() <= 12541 ? settings.TWEAK_COORDINATES_MESSAGE_DAY : settings.TWEAK_COORDINATES_MESSAGE_NIGHT;
		message = message
				.replace("{x}", String.valueOf(location.getBlockX()))
				.replace("{y}", String.valueOf(location.getBlockY()))
				.replace("{z}", String.valueOf(location.getBlockZ()));
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message.toText()));
	}
}
