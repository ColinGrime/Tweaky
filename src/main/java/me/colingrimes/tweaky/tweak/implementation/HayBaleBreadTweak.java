package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class HayBaleBreadTweak extends Tweak {

	public HayBaleBreadTweak(@Nonnull Tweaky plugin) {
		super(plugin, "hay_bale_bread");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_HAY_BALE_BREAD.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isRightClick() || !event.getItemType().name().endsWith("HOE") || !event.isBlock(Material.HAY_BLOCK)) {
			return;
		}

		event.getBlock().setType(Material.AIR);
		event.getPlayer().getWorld().dropItemNaturally(event.getLocation(), new ItemStack(Material.BREAD, 3));
		event.setCancelled(true);

		// Damage hoe.
		if (Util.damage(event.getItem())) {
			Util.sound(event.getPlayer(), Sound.ENTITY_ITEM_BREAK);
		}
	}
}
