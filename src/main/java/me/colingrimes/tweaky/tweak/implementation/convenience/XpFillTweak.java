package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Experience;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class XpFillTweak extends DefaultTweak {

	public XpFillTweak(@Nonnull Tweaky plugin) {
		super(plugin, "xp_fill");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.rightClick()
				.player(p -> Experience.fromPlayer(p) >= settings.TWEAK_XP_FILL_COST.get())
				.item(Material.GLASS_BOTTLE)
				.block(Material.ENCHANTING_TABLE);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Players.use(event.getPlayer(), event.getHand(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, event.getLocation());
		Items.give(event.getPlayer(), new ItemStack(Material.EXPERIENCE_BOTTLE));
		Experience.remove(event.getPlayer(), settings.TWEAK_XP_FILL_COST.get());
		event.setCancelled(true);
	}
}
