package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;

public class DeepslateBreakTweak extends Tweak {

	public DeepslateBreakTweak(@Nonnull Tweaky plugin) {
		super(plugin, "deepslate_break");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_DEEPSLATE_BREAK.get();
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

		Util.sound(event.getPlayer(), Sound.BLOCK_DEEPSLATE_BREAK);
		event.setInstaBreak(true);
	}
}
