package me.colingrimes.tweaky.tweak.implementation.crop;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;

import javax.annotation.Nonnull;

public class CropHarvestTweak extends DefaultTweak {

	public CropHarvestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_harvest");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.rightClick()
				.item(Tag.ITEMS_HOES)
				.block(b -> b.getBlockData() instanceof Ageable crop && crop.getAge() >= crop.getMaximumAge());
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Block block = event.getBlock();
		if (event.getPlayer().breakBlock(block)) {
			Players.use(event.getPlayer(), event.getHand(), block.getBlockData().getSoundGroup().getBreakSound(), event.getLocation());
			Blocks.edit(block, Ageable.class, c -> c.setAge(0));
		}
	}
}
