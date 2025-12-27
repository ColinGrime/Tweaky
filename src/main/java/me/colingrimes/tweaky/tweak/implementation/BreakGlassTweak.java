package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

import javax.annotation.Nonnull;

public class BreakGlassTweak extends Tweak {

	public BreakGlassTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_glass");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BREAK_GLASS.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_BREAK_GLASS.get().material(Material.TINTED_GLASS);
	}

	@EventHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		if (!hasPermission(event.getPlayer())) {
			return;
		}

		Material itemType = event.getItemInHand().getType();
		Material blockType = event.getBlock().getType();
		if (settings.TWEAK_BREAK_GLASS_MATERIALS.get().contains(itemType) && blockType.name().contains("GLASS")) {
			Blocks.breakSound(event.getBlock());
			event.setInstaBreak(true);
		}
	}
}
