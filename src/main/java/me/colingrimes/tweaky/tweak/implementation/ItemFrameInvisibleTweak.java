package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ItemFrameInvisibleTweak extends Tweak {

	public ItemFrameInvisibleTweak(@Nonnull Tweaky plugin) {
		super(plugin, "item_frame_invisible");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ITEM_FRAME_INVISIBLE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.ITEM_FRAME)
				.name("&aToggle Item Frames &8(Sneak Right Click)")
				.lore("&7Toggle the visibility of Item Frames.")
				.lore()
				.lore("&8Requires:")
				.lore(" &7Shears &8(Default)")
				.usage("&eUsage: &aSneak Right Click an Item Frame with Shears to toggle its visibility.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (event.getRightClicked() instanceof ItemFrame frame && player.isSneaking() && item.getType() == Material.SHEARS) {
			Sounds.play(frame, Sound.ITEM_SHEARS_SNIP);
			Items.damage(item, event.getPlayer());
			frame.setVisible(!frame.isVisible());
			event.setCancelled(true);
		}
	}
}
