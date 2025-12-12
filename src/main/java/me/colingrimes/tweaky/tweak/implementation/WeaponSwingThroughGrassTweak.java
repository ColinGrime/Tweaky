package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class WeaponSwingThroughGrassTweak extends Tweak {

	public WeaponSwingThroughGrassTweak(@Nonnull Tweaky plugin) {
		super(plugin, "weapon_swing_through_grass");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_WEAPON_SWING_THROUGH_GRASS.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isLeftClick() || !event.getBlock().isPassable()) {
			return;
		}

		Material type = event.getItemType();
		if (!type.name().endsWith("_SWORD") && !type.name().endsWith("_AXE")) {
			return;
		}

		Player player = event.getPlayer();
		Location eye = player.getEyeLocation();
		RayTraceResult result = player.getWorld().rayTrace(eye, eye.getDirection(), 3.5, FluidCollisionMode.NEVER, true, 0, e -> !e.equals(player));
		if (result != null && result.getHitEntity() != null) {
			player.attack(result.getHitEntity());
		}

		event.setCancelled(true);
	}
}
