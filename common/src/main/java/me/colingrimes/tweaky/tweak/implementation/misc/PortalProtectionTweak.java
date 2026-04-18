package me.colingrimes.tweaky.tweak.implementation.misc;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import javax.annotation.Nonnull;

public class PortalProtectionTweak extends DefaultTweak {

	public PortalProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "portal_protection");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.setPermissionRequired(false);
	}

	@EventHandler
	public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
		event.blockList().removeIf(b -> b.getType() == Material.NETHER_PORTAL);
	}
}
