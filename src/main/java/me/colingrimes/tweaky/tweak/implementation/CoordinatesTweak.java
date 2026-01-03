package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.message.Message;
import me.colingrimes.tweaky.tweak.type.ToggleTweak;
import me.colingrimes.tweaky.util.bukkit.Players;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;

public class CoordinatesTweak extends ToggleTweak {

	private BukkitTask task;

	public CoordinatesTweak(@Nonnull Tweaky plugin) {
		super(plugin, "coordinates", "coordinates", true);
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
		super.init();
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> Players.forEach(this::activate), 0L, 30L);
	}

	@Override
	public void shutdown() {
		super.shutdown();
		task.cancel();
	}

	@Override
	protected void activateTweak(@Nonnull Player player) {
		Location location = player.getLocation();
		Message<?> message = player.getWorld().getTime() <= 12541 ? msg.TWEAK_COORDINATES_DAY : msg.TWEAK_COORDINATES_NIGHT;
		message = message
				.replace("{x}", String.valueOf(location.getBlockX()))
				.replace("{y}", String.valueOf(location.getBlockY()))
				.replace("{z}", String.valueOf(location.getBlockZ()));
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message.toText()));
	}

	@Override
	protected void deactivateTweak(@Nonnull Player player) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent());
	}

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();
		if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
			activate(event.getPlayer());
		}
	}
}
