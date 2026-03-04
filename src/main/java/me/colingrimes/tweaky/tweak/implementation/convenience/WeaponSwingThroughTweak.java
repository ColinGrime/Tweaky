package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class WeaponSwingThroughTweak extends DefaultTweak {

	public WeaponSwingThroughTweak(@Nonnull Tweaky plugin) {
		super(plugin, "weapon_swing_through");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.leftClick()
				.anyItem(Tag.ITEMS_SWORDS, Tag.ITEMS_AXES)
				.block(Block::isPassable);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		RayTraceResult result = Players.rayTrace(event.getPlayer(), 3);
		if (result != null && result.getHitEntity() != null) {
			event.getPlayer().attack(result.getHitEntity());
		}
	}
}
