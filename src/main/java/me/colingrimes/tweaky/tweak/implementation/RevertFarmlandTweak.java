package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
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
		return menus.TWEAK_REVERT_FARMLAND.get().material(Material.FARMLAND);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || !event.isShiftRightClick() || !Tag.ITEMS_HOES.isTagged(event.getItemType()) || !event.isBlock(Material.FARMLAND)) {
			return;
		}

		Items.damage(event.getItem(), player);
		player.swingMainHand();
		player.getWorld().playSound(player.getLocation(), Sound.ITEM_HOE_TILL, 1F, 1F);
		event.getBlock().setType(Material.DIRT);
		event.setCancelled(true);
	}
}
