package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.misc.Random;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Horse;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;

public class HorseRandomizerTweak extends DefaultTweak {

	public HorseRandomizerTweak(@Nonnull Tweaky plugin) {
		super(plugin, "horse_randomizer");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(Material.BRUSH)
				.entity(e -> e instanceof Horse horse && !horse.isTamed());
	}

	@TweakHandler
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		Players.use(event.getPlayer(), event.getHand(), Sound.ITEM_BUNDLE_INSERT, event.getRightClicked().getLocation());
		Horse horse = (Horse) event.getRightClicked();
		horse.setColor(Random.item(Horse.Color.values()));
		horse.setStyle(Random.item(Horse.Style.values()));
		event.setCancelled(true);
	}
}
