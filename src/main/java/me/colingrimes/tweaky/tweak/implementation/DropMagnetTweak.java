package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.List;

public class DropMagnetTweak extends Tweak {

	public DropMagnetTweak(@Nonnull Tweaky plugin) {
		super(plugin, "drops_magnet");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_DROPS_MAGNET.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_DROPS_MAGNET.get().material(Material.FLINT);
	}

	@EventHandler
	public void onBlockDropItem(@Nonnull BlockDropItemEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

		List<Item> items = event.getItems();
		if (!items.isEmpty() && items.getFirst().getNearbyEntities(2, 2, 2).contains(player)) {
			items.forEach(i -> i.setVelocity(Util.direction(i, player).multiply(new Vector(0.2, 0.4, 0.2))));
		}
	}
}
