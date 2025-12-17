package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import javax.annotation.Nonnull;

public class PortalProtectionTweak extends Tweak {

	public PortalProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "portal_protection");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_PORTAL_PROTECTION.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.OBSIDIAN)
				.name("&aPortal Protection")
				.lore("&7Protects Nether Portals from explosions.")
				.usage("&eUsage: &aNether Portals can no longer be destroyed by explosions.");
	}

	@EventHandler
	public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
		event.blockList().removeIf(b -> b.getType() == Material.NETHER_PORTAL);
	}
}
