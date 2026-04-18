package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.event.block.BlockDamageEvent;

import javax.annotation.Nonnull;

public class BreakGlassTweak extends DefaultTweak {

	public BreakGlassTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_glass");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(i -> settings.TWEAK_BREAK_GLASS_MATERIALS.get().contains(i.getType()))
				.block(b -> b.getType().name().contains("GLASS"));
	}

	@TweakHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		Blocks.breakSound(event.getBlock());
		event.setInstaBreak(true);
	}
}
