package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BreakEnderChestTweak extends DefaultTweak {

	enum Mode {
		Drop,
		Protection
	}

	public BreakEnderChestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_ender_chest");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(i -> !i.containsEnchantment(Enchantment.SILK_TOUCH))
				.block(Material.ENDER_CHEST);
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		Mode mode = Util.parse(Mode.class, settings.TWEAK_BREAK_ENDER_CHEST_MODE.get());
		return switch (mode) {
			case Drop -> menus.TWEAK_BREAK_ENDER_CHEST_DROP.get().material(Material.ENDER_CHEST);
			case Protection -> menus.TWEAK_BREAK_ENDER_CHEST_PROTECTION.get().material(Material.ENDER_CHEST);
			case null -> super.getGuiItem();
		};
	}

	@TweakHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Mode mode = Util.parse(Mode.class, settings.TWEAK_BREAK_ENDER_CHEST_MODE.get());
		if (mode == null) {
			return;
		}

		switch (mode) {
			case Drop -> {
				Block block = event.getBlock();
				block.setType(Material.AIR);
				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.ENDER_CHEST));
			}
			case Protection -> msg.TWEAK_ENDER_CHEST_PROTECTED.send(event.getPlayer());
		}

		event.setCancelled(true);
	}
}
