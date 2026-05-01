package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.ConfigurableTweak;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Experience;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class XpFillTweak extends DefaultTweak implements ConfigurableTweak {

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
		Player player = event.getPlayer();
		if (!player.isSneaking()) {
			Players.use(event.getPlayer(), event.getHand(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, event.getLocation());
			Items.give(event.getPlayer(), new ItemStack(Material.EXPERIENCE_BOTTLE));
			Experience.remove(event.getPlayer(), settings.TWEAK_XP_FILL_COST.get());
			event.setCancelled(true);
			return;
		}

		int bottles = player.getInventory().getItem(event.getHand()).getAmount();
		double maxConversions = (double) Experience.fromPlayer(player) / settings.TWEAK_XP_FILL_COST.get();
		int conversions = Math.min(bottles, (int) maxConversions);

		Players.swingHand(player, event.getHand());
		player.getInventory().getItem(event.getHand()).setAmount(bottles - conversions);
		Scheduler.sync().run(() -> Items.give(event.getPlayer(), new ItemStack(Material.EXPERIENCE_BOTTLE, conversions)));
		Experience.remove(event.getPlayer(), settings.TWEAK_XP_FILL_COST.get() * conversions);
		Sounds.play(event.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
		event.setCancelled(true);
	}
}
