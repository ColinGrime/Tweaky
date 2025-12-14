package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

public class CropProtectionTweak extends Tweak {

	public CropProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_protection");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CROPS_PROTECTION.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		Block block = event.getClickedBlock();
		if (event.getAction() == Action.PHYSICAL && block != null && block.getType() == Material.FARMLAND) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityInteract(@Nonnull EntityInteractEvent event) {
		if (event.getBlock().getType() == Material.FARMLAND) {
			event.setCancelled(true);
		}
	}
}
