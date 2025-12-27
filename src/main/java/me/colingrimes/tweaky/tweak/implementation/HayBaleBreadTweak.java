package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
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

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_HAY_BALE_BREAD.get().material(Material.BREAD);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

		if (event.isRightClick() && event.isItem(Tag.ITEMS_HOES) && event.isBlock(Material.HAY_BLOCK) && event.canBreak()) {
			Blocks.breakSound(event.getBlock());
			event.getBlock().setType(Material.AIR);
			Items.drop(new ItemStack(Material.BREAD, 3), event.getLocation());
			Items.damage(event.getItem(), player);
			event.setCancelled(true);
		}
	}
}
