package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.annotation.Nonnull;

public class DeathNotifyTweak extends Tweak {

	public DeathNotifyTweak(@Nonnull Tweaky plugin) {
		super(plugin, "death_notify");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_DEATH_NOTIFY.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.SKELETON_SKULL)
				.name("&aDeath Notify &8(Die)")
				.lore("&7Notifies where you died.")
				.usage("&eUsage: &aNotifies you of your death location.");
	}

	@EventHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location location = player.getLocation();
		player.sendMessage(Text.color(settings.TWEAK_DEATH_NOTIFY_MESSAGE.get()
				.replace("{x}", String.valueOf(location.getBlockX()))
				.replace("{y}", String.valueOf(location.getBlockY()))
				.replace("{z}", String.valueOf(location.getBlockZ()))
				.replace("{world}", getWorld(player))
				.replace("{levels}", String.valueOf(player.getLevel()))));
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
