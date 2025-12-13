package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class FireDriesSpongesTweak extends Tweak {

	public FireDriesSpongesTweak(@Nonnull Tweaky plugin) {
		super(plugin, "fire_dries_sponges");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_FIRE_DRIES_SPONGES.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isRightClick() || !event.isItem(Material.FLINT_AND_STEEL, Material.FIRE_CHARGE) || !event.isBlock(Material.WET_SPONGE)) {
			return;
		}

		Sounds.play(event.getBlock(), Sound.BLOCK_FIRE_EXTINGUISH);
		event.getBlock().setType(Material.SPONGE);
		event.getBlock().getWorld().spawnParticle(Particle.LARGE_SMOKE, event.getLocation().add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.01);
		event.setCancelled(true);

		switch (event.getItemType()) {
			case Material.FLINT_AND_STEEL -> Items.damage(event.getItem(), event.getPlayer());
			case Material.FIRE_CHARGE -> Items.remove(event.getItem());
		}
	}
}
