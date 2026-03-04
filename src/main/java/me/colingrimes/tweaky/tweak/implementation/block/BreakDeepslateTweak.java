package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.Objects;

public class BreakDeepslateTweak extends DefaultTweak {

	public BreakDeepslateTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_deepslate");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(Material.NETHERITE_PICKAXE)
				.item(i -> i.getEnchantmentLevel(Enchantment.EFFICIENCY) >= 5)
				.anyBlock(Material.DEEPSLATE, Material.COBBLED_DEEPSLATE);
	}

	@TweakHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		PotionEffect potion = event.getPlayer().getPotionEffect(PotionEffectType.HASTE);
		if (potion != null && potion.getAmplifier() > 1) {
			Blocks.breakSound(event.getBlock());
			event.setInstaBreak(true);
		}
	}
}
