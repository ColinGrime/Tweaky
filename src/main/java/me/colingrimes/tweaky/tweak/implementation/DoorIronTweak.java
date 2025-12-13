package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class DoorIronTweak extends Tweak {

	public DoorIronTweak(@Nonnull Tweaky plugin) {
		super(plugin, "doors_iron");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_DOORS_IRON.get();
	}

	@EventHandler
	public void onPlayerInteractBlock(@Nonnull PlayerInteractBlockEvent event) {
		if (event.isRightClick() && event.isBlock(Material.IRON_DOOR)) {
			event.getPlayer().swingMainHand();
			Sounds.play(event.getBlock(), Sound.BLOCK_IRON_DOOR_OPEN);
			Blocks.edit(event.getBlock(), Door.class, d -> d.setOpen(!d.isOpen()));
		}
	}
}
