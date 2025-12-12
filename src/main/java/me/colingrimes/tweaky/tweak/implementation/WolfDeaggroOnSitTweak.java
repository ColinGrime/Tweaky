package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;

public class WolfDeaggroOnSitTweak extends Tweak {

	public WolfDeaggroOnSitTweak(@Nonnull Tweaky plugin) {
		super(plugin, "wolf_deaggro_on_sit");
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@EventHandler
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND || !(event.getRightClicked() instanceof Wolf wolf)) {
			return;
		}

		// Needs to be reworked + consideration towards other wolves who join in the fight.
		// Perhaps shift right click to sit all? Not sure yet.
		if (wolf.isTamed() && event.getPlayer().equals(wolf.getOwner())) {
			boolean wasSitting = wolf.isSitting();
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				if (wolf.isSitting() && !wasSitting) {
					wolf.setAngry(false);
					wolf.setTarget(null);
				}
			}, 2L);
		}
	}
}
