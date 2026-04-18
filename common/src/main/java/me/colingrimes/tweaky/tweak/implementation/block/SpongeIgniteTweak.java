package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

import javax.annotation.Nonnull;

public class SpongeIgniteTweak extends DefaultTweak {

	public SpongeIgniteTweak(@Nonnull Tweaky plugin) {
		super(plugin, "sponge_ignite");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.buildable()
				.rightClick()
				.anyItem(Material.FLINT_AND_STEEL, Material.FIRE_CHARGE)
				.block(Material.WET_SPONGE);
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Players.use(event.getPlayer(), event.getHand(), Sound.BLOCK_FIRE_EXTINGUISH, event.getBlock().getLocation());
		event.getBlock().setType(Material.SPONGE);
		event.getBlock().getWorld().spawnParticle(Particle.LARGE_SMOKE, event.getLocation().add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.01);
		event.setCancelled(true);
	}
}
