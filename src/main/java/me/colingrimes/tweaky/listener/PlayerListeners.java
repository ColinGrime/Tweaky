package me.colingrimes.tweaky.listener;

import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

public class PlayerListeners implements Listener {

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getClickedBlock() != null) {
			Bukkit.getPluginManager().callEvent(new PlayerInteractBlockEvent(event));
		}
	}
}
