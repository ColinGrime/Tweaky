package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;

public class VillagerProtectionTweak extends DefaultTweak {

	public VillagerProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "villager_protection");
	}

	@EventHandler
	public void onEntityDamageByEntity(@Nonnull EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Villager && event.getDamager() instanceof Player player && hasPermission(player)) {
			event.setCancelled(true);
		}
	}
}
