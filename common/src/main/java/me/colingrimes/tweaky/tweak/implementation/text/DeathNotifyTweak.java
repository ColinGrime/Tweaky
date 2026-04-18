package me.colingrimes.tweaky.tweak.implementation.text;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class DeathNotifyTweak extends DefaultTweak {

	public DeathNotifyTweak(@Nonnull Tweaky plugin) {
		super(plugin, "death_notify");
	}

	@TweakHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location location = player.getLocation();
		msg.TWEAK_DEATH_MESSAGE
				.replace("{x}", String.valueOf(location.getBlockX()))
				.replace("{y}", String.valueOf(location.getBlockY()))
				.replace("{z}", String.valueOf(location.getBlockZ()))
				.replace("{world}", getWorld(player))
				.replace("{levels}", String.valueOf(player.getLevel()))
				.send(player);
	}

	/**
	 * Gets the formatted world of the player.
	 *
	 * @param player the player
	 * @return the world
	 */
	@Nonnull
	private String getWorld(@Nonnull Player player) {
		return switch (player.getWorld().getEnvironment()) {
			case NETHER -> "&cNether";
			case THE_END -> "&5End";
			default -> "&2Overworld";
		};
	}
}
