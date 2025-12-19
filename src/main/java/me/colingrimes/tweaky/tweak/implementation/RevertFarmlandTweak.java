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

public class RevertFarmlandTweak extends Tweak {

	public RevertFarmlandTweak(@Nonnull Tweaky plugin) {
		super(plugin, "revert_farmland");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_REVERT_FARMLAND.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.FARMLAND)
				.name("&aRevert Farmland &8(Sneak Right Click)")
				.lore("&7Goes back to Dirt.")
				.lore()
				.lore("&8Requires:")
				.lore(" &7Hoe &8(Any)")
				.usage("&eUsage: &aSneak Right Click on Farmland with a Hoe to revert it back to Dirt.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isShiftRightClick() || !Tag.ITEMS_HOES.isTagged(event.getItemType()) || !event.isBlock(Material.FARMLAND)) {
			return;
		}

		Items.damage(event.getItem(), event.getPlayer());
		event.getPlayer().swingMainHand();
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_HOE_TILL, 1F, 1F);
		event.getBlock().setType(Material.DIRT);
		event.setCancelled(true);
	}
}
