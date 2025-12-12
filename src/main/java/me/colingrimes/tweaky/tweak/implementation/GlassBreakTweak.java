package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class GlassBreakTweak extends Tweak {

	public GlassBreakTweak(@Nonnull Tweaky plugin) {
		super(plugin, "glass_break");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_GLASS_BREAK.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isLeftClick() || !event.getBlockType().name().contains("GLASS")) {
			return;
		}

		if (settings.TWEAK_GLASS_BREAK_MATERIALS.get().contains(event.getItemType())) {
			Util.sound(event.getPlayer(), Sound.BLOCK_GLASS_BREAK);
			event.getPlayer().breakBlock(event.getBlock());
			event.setCancelled(true);
		}
	}
}
