package me.colingrimes.tweaky.tweak.implementation.misc;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;

import javax.annotation.Nonnull;

public class ArmorStandArmsTweak extends DefaultTweak {

	public ArmorStandArmsTweak(@Nonnull Tweaky plugin) {
		super(plugin, "armor_stand_arms");
	}

	@EventHandler
	public void onEntitySpawn(@Nonnull EntitySpawnEvent event) {
		if (event.getEntity() instanceof ArmorStand stand && !stand.isMarker() && stand.isVisible()) {
			stand.setArms(true);
		}
	}
}
