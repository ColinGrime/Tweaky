package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import javax.annotation.Nonnull;

public class PortalExplosionProofTweak extends Tweak {

	public PortalExplosionProofTweak(@Nonnull Tweaky plugin) {
		super(plugin, "portal_explosion_proof");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_PORTAL_EXPLOSION_PROOF.get();
	}

	@EventHandler
	public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
		event.blockList().removeIf(b -> b.getType() == Material.NETHER_PORTAL);
	}
}
