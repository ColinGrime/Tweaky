package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventPriority;

import javax.annotation.Nonnull;

public class DoorIronTweak extends DefaultTweak {

	public DoorIronTweak(@Nonnull Tweaky plugin) {
		super(plugin, "doors_iron");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.buildable()
				.mainHand()
				.rightClick()
				.block(Material.IRON_DOOR);
	}

	@TweakHandler(priority = EventPriority.HIGH)
	public void onPlayerInteractBlock(@Nonnull PlayerInteractBlockEvent event) {
		event.setCancelled(true);
		event.getPlayer().swingMainHand();
		Sounds.play(event.getBlock(), Sound.BLOCK_IRON_DOOR_OPEN);
		Blocks.edit(event.getBlock(), Door.class, d -> d.setOpen(!d.isOpen()));
	}
}
