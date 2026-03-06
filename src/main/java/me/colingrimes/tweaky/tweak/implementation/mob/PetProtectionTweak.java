package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;

public class PetProtectionTweak extends DefaultTweak {

	public PetProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "pet_protection");
	}

	@EventHandler
	public void onEntityDamageByEntity(@Nonnull EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Tameable tameable) || !(tameable.getOwner() instanceof Player owner) || !hasPermission(owner)) {
			return;
		}

		// Direct player attack.
		if (event.getDamager() instanceof Player player && player.equals(owner)) {
			event.setCancelled(true);
		}

		// Projectile from player.
		if (event.getDamager() instanceof Projectile projectile && owner.equals(projectile.getShooter())) {
			event.setCancelled(true);
		}

		// TNT from player.
		if (event.getDamager() instanceof TNTPrimed tnt && owner.equals(tnt.getSource())) {
			event.setCancelled(true);
		}
	}
}
