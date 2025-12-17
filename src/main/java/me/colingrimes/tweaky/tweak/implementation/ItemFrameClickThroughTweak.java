package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class ItemFrameClickThroughTweak extends Tweak {

	public ItemFrameClickThroughTweak(@Nonnull Tweaky plugin) {
		super(plugin, "item_frame_click_through");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ITEM_FRAME_CLICK_THROUGH.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.ITEM_FRAME)
				.name("&aClick Through Item Frames &8(Right Click)")
				.lore("&7Open Chests with Item Frames on them.")
				.usage("&eUsage: &aRight Click an Item Frame on a Chest to open it.")
				.usage("         &aShift for normal behavior.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (!(event.getRightClicked() instanceof ItemFrame frame) || player.isSneaking()) {
			return;
		}

		Material playerItem = player.getInventory().getItemInMainHand().getType();
		Material frameItem = frame.getItem().getType();

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
