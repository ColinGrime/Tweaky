package me.colingrimes.tweaky.tweak.implementation.crop;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

public class CropProtectionTweak extends DefaultTweak {

	public CropProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_protection");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.setPermissionRequired(false);
		properties.getGuard().block(Material.FARMLAND);
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) {
			event.setCancelled(true);
		}
	}

	@TweakHandler
	public void onEntityInteract(@Nonnull EntityInteractEvent event) {
		event.setCancelled(true);
	}
}
