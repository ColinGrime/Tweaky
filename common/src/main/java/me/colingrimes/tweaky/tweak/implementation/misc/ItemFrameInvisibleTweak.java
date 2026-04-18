package me.colingrimes.tweaky.tweak.implementation.misc;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

public class ItemFrameInvisibleTweak extends DefaultTweak {

	public ItemFrameInvisibleTweak(@Nonnull Tweaky plugin) {
		super(plugin, "item_frame_invisible");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.sneaking()
				.item(Material.SHEARS)
				.entity(EntityType.ITEM_FRAME);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		ItemFrame frame = (ItemFrame) event.getRightClicked();
		Players.use(event.getPlayer(), event.getHand(), Sound.ITEM_SHEARS_SNIP, frame.getLocation());
		frame.setVisible(!frame.isVisible());
		event.setCancelled(true);
	}
}
