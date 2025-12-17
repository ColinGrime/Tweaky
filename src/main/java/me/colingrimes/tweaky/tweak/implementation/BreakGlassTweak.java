package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.text.Text;
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
		TweakItem item = TweakItem
				.of(Material.TINTED_GLASS)
				.name("&aGlass Break")
				.lore("&7Instantly break Glass.")
				.lore()
				.lore("&8Requires:")
				.usage("&eUsage: &aBreak Glass instantly with the listed tools.");
		settings.TWEAK_BREAK_GLASS_MATERIALS.get().forEach(type -> item.lore(" &7" + Text.format(type.name())));
		return item;
	}

	@EventHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		Material itemType = event.getItemInHand().getType();
		Material blockType = event.getBlock().getType();
		if (settings.TWEAK_BREAK_GLASS_MATERIALS.get().contains(itemType) && blockType.name().contains("GLASS")) {
			Blocks.breakSound(event.getBlock());
			event.setInstaBreak(true);
		}
	}
}
