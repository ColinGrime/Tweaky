package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class ItemFrameClickThroughTweak extends DefaultTweak {

	public ItemFrameClickThroughTweak(@Nonnull Tweaky plugin) {
		super(plugin, "item_frame_click_through");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.standing()
				.entity(EntityType.ITEM_FRAME);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Material playerItem = player.getInventory().getItem(event.getHand()).getType();
		Material frameItem = ((ItemFrame) event.getRightClicked()).getItem().getType();

		// Check if the player is trying to place down the initial item in the frame (not rotating).
		if (!playerItem.isAir() && frameItem.isAir()) {
			return;
		}

		RayTraceResult result = Players.rayTraceBlocks(player, 3.5);
		if (result != null && result.getHitBlock() != null && result.getHitBlock().getState() instanceof Chest chest) {
			player.swingMainHand();
			player.openInventory(chest.getInventory());
			event.setCancelled(true);
		}
	}
}
