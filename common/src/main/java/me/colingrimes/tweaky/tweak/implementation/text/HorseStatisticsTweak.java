package me.colingrimes.tweaky.tweak.implementation.text;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

public class HorseStatisticsTweak extends DefaultTweak {

	public HorseStatisticsTweak(@Nonnull Tweaky plugin) {
		super(plugin, "horse_statistics");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.sneaking()
				.mainHand()
				.entity(e -> e instanceof AbstractHorse);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		AbstractHorse horse = (AbstractHorse) event.getRightClicked();
		AttributeInstance maxHealth = horse.getAttribute(Attribute.MAX_HEALTH);
		AttributeInstance maxSpeed = horse.getAttribute(Attribute.MOVEMENT_SPEED);
		if (maxHealth != null && maxSpeed != null) {
			sendHorseStatistics(event.getPlayer(), maxHealth.getBaseValue(),  maxSpeed.getBaseValue(), horse.getJumpStrength());
		}
	}

	/**
	 * Sends the horse statistics to the player.
	 *
	 * @param player the player
	 * @param health the health of the horse
	 * @param speed the speed of the horse
	 * @param jump the jump strength of the horse
	 */
	private void sendHorseStatistics(@Nonnull Player player, double health, double speed, double jump) {
		player.sendMessage("");
		msg.TWEAK_HORSE_STATISTICS
				.replace("{health}", String.format("%.2f", health) + " HP")
				.replace("{speed}", String.format("%.2f", convertSpeed(speed)) + " blocks/sec")
				.replace("{jump}", String.format("%.2f", convertJump(jump)) + " blocks")
				.send(player);
		player.sendMessage("");
	}

	/**
	 * Converts the speed attribute to blocks/sec.
	 *
	 * @param speed the speed attribute value
	 * @return the speed in blocks/sec
	 */
	private double convertSpeed(double speed) {
		// https://minecraft.fandom.com/wiki/Tutorials/Horses
		return speed * 42.163;
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
//		return 7.56889 * Math.exp(0.602676 * jump) - 8.59434;
//		return 5.42044 * Math.pow(jump, 1.61929) - 0.13636;

		// Another potential one below:
		return -0.1817584952d * jump * jump * jump +
				3.689713992d * jump * jump +
				2.128599134d * jump +
				-0.343930367;
	}
}
