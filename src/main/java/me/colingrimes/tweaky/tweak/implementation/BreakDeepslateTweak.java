package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

public class BreakDeepslateTweak extends Tweak {

	public BreakDeepslateTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_deepslate");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BREAK_DEEPSLATE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.DEEPSLATE)
				.name("&aDeepslate Break")
				.lore("&7Instantly break Deepslate.")
				.lore()
				.lore("&8Requires:")
				.lore(" &7Netherite Pick, Eff 5, Haste II.")
				.usage("&eUsage: &aBreak Deepslate instantly with a Netherite Pickaxe, Efficiency V, and Haste II.");
	}

	@EventHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		ItemStack item = event.getItemInHand();
		Material blockType = event.getBlock().getType();
		if (item.getType() != Material.NETHERITE_PICKAXE || (blockType != Material.DEEPSLATE && blockType != Material.COBBLED_DEEPSLATE)) {
			return;
		}

		PotionEffect potion = event.getPlayer().getPotionEffect(PotionEffectType.HASTE);
		if (item.getEnchantmentLevel(Enchantment.EFFICIENCY) < 5 || potion == null || potion.getAmplifier() < 1) {
			return;
		}

		Blocks.breakSound(event.getBlock());
		event.setInstaBreak(true);
	}
}
