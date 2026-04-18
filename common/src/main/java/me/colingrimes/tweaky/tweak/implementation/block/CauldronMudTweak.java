package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.CauldronTweak;
import org.bukkit.Material;

import javax.annotation.Nonnull;

public class CauldronMudTweak extends CauldronTweak {

	public CauldronMudTweak(@Nonnull Tweaky plugin) {
		super(plugin, "cauldron_mud", (type) -> type == Material.DIRT ? Material.MUD : null, plugin.getSettings().TWEAK_CAULDRON_MUD_USE_WATER);
	}
}
