package me.colingrimes.tweaky.listener;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.type.ToggleTweak;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.annotation.Nonnull;

public class TweakListeners implements Listener {

	private final Tweaky plugin;

	public TweakListeners(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		for (Tweak tweak : plugin.getTweakManager().getTweaks()) {
			if (tweak instanceof ToggleTweak toggle) {
				toggle.checkToggle(event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		for (Tweak tweak : plugin.getTweakManager().getTweaks()) {
			if (tweak instanceof ToggleTweak toggle) {
				toggle.removeToggle(event.getPlayer());
			}
		}
	}
}
