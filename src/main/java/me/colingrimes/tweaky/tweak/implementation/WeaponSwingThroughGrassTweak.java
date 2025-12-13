package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Tag;
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
		if (!event.isLeftClick() || !event.getBlock().isPassable() || !event.isItem(Tag.ITEMS_SWORDS, Tag.ITEMS_AXES)) {
			return;
		}

		RayTraceResult result = Players.rayTrace(event.getPlayer(), 3);
		if (result != null && result.getHitEntity() != null) {
			event.getPlayer().attack(result.getHitEntity());
		}

		event.setCancelled(true);
	}
}
