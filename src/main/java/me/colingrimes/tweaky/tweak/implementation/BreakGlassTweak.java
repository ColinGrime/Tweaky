package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

import javax.annotation.Nonnull;

public class BreakGlassTweak extends Tweak {

	public BreakGlassTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_glass");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BREAK_GLASS.get();
	}

	@EventHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		Material itemType = event.getItemInHand().getType();
		Material blockType = event.getBlock().getType();
		if (settings.TWEAK_BREAK_GLASS_MATERIALS.get().contains(itemType) && blockType.name().contains("GLASS")) {
			Blocks.breakSound(event.getBlock());
			event.setInstaBreak(true);
		}
	}
}
