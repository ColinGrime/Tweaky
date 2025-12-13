package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

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
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		Material itemType = event.getItemInHand().getType();
		Material blockType = event.getBlock().getType();
		if (settings.TWEAK_GLASS_BREAK_MATERIALS.get().contains(itemType) && blockType.name().contains("GLASS")) {
			Blocks.breakSound(event.getBlock());
			event.setInstaBreak(true);
		}
	}
}
