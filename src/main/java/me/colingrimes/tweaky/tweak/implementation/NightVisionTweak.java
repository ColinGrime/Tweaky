package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.type.ToggleTweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;

public class NightVisionTweak extends ToggleTweak {

	public NightVisionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "night_vision", "nightvision", false);
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_NIGHT_VISION.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		ItemStack potion = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) potion.getItemMeta();
		meta.setBasePotionType(PotionType.NIGHT_VISION);
		potion.setItemMeta(meta);
		return menus.TWEAK_NIGHT_VISION.get().item(potion);
	}

	@Override
	protected void activateTweak(@Nonnull Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0, false, false));
	}

	@Override
	protected void deactivateTweak(@Nonnull Player player) {
		player.removePotionEffect(PotionEffectType.NIGHT_VISION);
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		activate(event.getPlayer());
	}

	@EventHandler
	public void onPlayerRespawn(@Nonnull PlayerRespawnEvent event) {
		Bukkit.getScheduler().runTask(plugin, () -> activate(event.getPlayer()));
	}

	@EventHandler
	public void onPlayerItemConsume(@Nonnull EntityPotionEffectEvent event) {
		if (!isToggled(event.getEntity()) || !hasPermission(event.getEntity())) {
			return;
		}

		if (event.getCause() == EntityPotionEffectEvent.Cause.MILK && event.getModifiedType() == PotionEffectType.NIGHT_VISION) {
			event.setCancelled(true);
		}
	}
}
