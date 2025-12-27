package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class SpongeIgniteTweak extends Tweak {

	public SpongeIgniteTweak(@Nonnull Tweaky plugin) {
		super(plugin, "sponge_ignite");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_SPONGE_IGNITE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_SPONGE_IGNITE.get().material(Material.WET_SPONGE);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || !event.isRightClick() || !event.isItem(Material.FLINT_AND_STEEL, Material.FIRE_CHARGE) || !event.isBlock(Material.WET_SPONGE) || !event.canModify()) {
			return;
		}

		Sounds.play(event.getBlock(), Sound.BLOCK_FIRE_EXTINGUISH);
		event.getBlock().setType(Material.SPONGE);
		event.getBlock().getWorld().spawnParticle(Particle.LARGE_SMOKE, event.getLocation().add(0.5, 0.5, 0.5), 10, 0.3, 0.3, 0.3, 0.01);
		event.setCancelled(true);

		switch (event.getItemType()) {
			case Material.FLINT_AND_STEEL -> Items.damage(event.getItem(), player);
			case Material.FIRE_CHARGE -> Items.remove(event.getItem());
		}
	}
}
