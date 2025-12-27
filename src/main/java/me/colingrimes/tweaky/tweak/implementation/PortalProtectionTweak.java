package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
		return menus.TWEAK_PORTAL_PROTECTION.get().material(Material.OBSIDIAN);
	}

	@Override
	public boolean hasPermission(@Nullable Entity entity) {
		return true;
	}

	@EventHandler
	public void onEntityExplode(@Nonnull EntityExplodeEvent event) {
		event.blockList().removeIf(b -> b.getType() == Material.NETHER_PORTAL);
	}
}
