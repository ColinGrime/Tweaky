package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Experience;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class XpFillTweak extends Tweak {

	public XpFillTweak(@Nonnull Tweaky plugin) {
		super(plugin, "xp_fill");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_XP_FILL.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isRightClick() || !event.isItem(Material.GLASS_BOTTLE) || !event.isBlock(Material.ENCHANTING_TABLE)) {
			return;
		}

		Player player = event.getPlayer();
		if (Experience.fromPlayer(player) >= settings.TWEAK_XP_FILL_COST.get()) {
			Sounds.play(event.getBlock(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
			Experience.remove(player, settings.TWEAK_XP_FILL_COST.get());
			Items.remove(event.getItem());
			Items.give(event.getPlayer(), new ItemStack(Material.EXPERIENCE_BOTTLE));
			event.setCancelled(true);
		}
	}
}
