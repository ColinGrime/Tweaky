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

public class RevertFarmlandTweak extends DefaultTweak {

	public RevertFarmlandTweak(@Nonnull Tweaky plugin) {
		super(plugin, "revert_farmland");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.buildable()
				.sneaking()
				.rightClick()
				.item(Tag.ITEMS_HOES)
				.block(Material.FARMLAND);
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Players.use(event.getPlayer(), event.getHand(), Sound.ITEM_HOE_TILL, event.getBlock().getLocation());
		event.getBlock().setType(Material.DIRT);
		event.setCancelled(true);
	}
}
