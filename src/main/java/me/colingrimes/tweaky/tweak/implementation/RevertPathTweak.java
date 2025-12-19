package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class RevertPathTweak extends Tweak {

	public RevertPathTweak(@Nonnull Tweaky plugin) {
		super(plugin, "revert_path");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_REVERT_PATH.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.DIRT_PATH)
				.name("&aRevert Path &8(Sneak Right Click)")
				.lore("&7Goes back to Dirt.")
				.lore()
				.lore("&8Requires:")
				.lore(" &7Shovel &8(Any)")
				.usage("&eUsage: &aSneak Right Click on Dirt Paths with a Shovel to revert it back to Dirt.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isShiftRightClick() || !Tag.ITEMS_SHOVELS.isTagged(event.getItemType()) || !event.isBlock(Material.DIRT_PATH)) {
			return;
		}

		Items.damage(event.getItem(), event.getPlayer());
		event.getPlayer().swingMainHand();
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_SHOVEL_FLATTEN, 1F, 1F);
		event.getBlock().setType(Material.DIRT);
		event.setCancelled(true);
	}
}
