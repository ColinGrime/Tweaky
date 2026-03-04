package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;

import javax.annotation.Nonnull;

public class RevertPathTweak extends DefaultTweak {

	public RevertPathTweak(@Nonnull Tweaky plugin) {
		super(plugin, "revert_path");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.sneaking()
				.rightClick()
				.item(Tag.ITEMS_SHOVELS)
				.block(Material.DIRT_PATH);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		Players.use(event.getPlayer(), event.getHand(), Sound.ITEM_SHOVEL_FLATTEN, event.getBlock().getLocation());
		event.getBlock().setType(Material.DIRT);
		event.setCancelled(true);
	}
}
