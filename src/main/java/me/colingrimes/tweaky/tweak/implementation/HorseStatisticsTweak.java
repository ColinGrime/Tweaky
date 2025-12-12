package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;

public class HorseStatisticsTweak extends Tweak {

	public HorseStatisticsTweak(@Nonnull Tweaky plugin) {
		super(plugin, "horse_statistics");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_HORSE_STATISTICS.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getHand() != EquipmentSlot.HAND || !player.isSneaking() || !(event.getRightClicked() instanceof AbstractHorse horse)) {
			return;
		}

		AttributeInstance maxHealth = horse.getAttribute(Attribute.MAX_HEALTH);
		AttributeInstance maxSpeed = horse.getAttribute(Attribute.MOVEMENT_SPEED);
		if (maxHealth != null && maxSpeed != null) {
			double health = maxHealth.getBaseValue();
			double speed = maxSpeed.getBaseValue();
			double jump = horse.getJumpStrength();
			for (String msg : settings.TWEAK_HORSE_STATISTICS_MESSAGE.get()) {
				player.sendMessage(Util.color(msg
						.replace("{health}", String.format("%.2f", health) + " HP")
						.replace("{speed}", String.format("%.2f", convertSpeed(speed)) + " blocks/sec")
						.replace("{jump}", String.format("%.2f", convertJump(jump)) + " blocks")));
			}
		}
	}

	/**
	 * Converts the speed attribute to blocks/sec.
	 *
	 * @param speed the speed attribute value
	 * @return the speed in blocks/sec
	 */
	private double convertSpeed(double speed) {
		// https://minecraft.fandom.com/wiki/Tutorials/Horses
		return speed * 42.16;
	}

	/**
	 * Converts the jump strength attribute to an approximation of blocks.
	 *
	 * @param jump the jump strength attribute value
	 * @return the approximate amount of blocks
	 */
	private double convertJump(double jump) {
		// Two equations exist to approximate jump blocks:
		// 1. y = 7.56889e^(0.602676 * x) - 8.59434 (this is the one we are going to use, it seems a little more accurate).
		// 2. y = 5.42044x1.61929 - 0.13636
		return 7.56889 * Math.exp(0.602676 * jump) - 8.59434;
//		return 5.42044 * Math.pow(jump, 1.61929) - 0.13636;
	}
}
