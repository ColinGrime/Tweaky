package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.annotation.Nonnull;

public class PetProtectionTweak extends Tweak {

	public PetProtectionTweak(@Nonnull Tweaky plugin) {
		super(plugin, "pet_protection");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_PET_PROTECTION.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.WOLF_SPAWN_EGG)
				.name("&aPet Protection")
				.lore("&7Protects your Pets from yourself.")
				.usage("&eUsage: &aPrevents you from attacking your own Pet.");
	}

	@EventHandler
	public void onEntityDamageByEntity(@Nonnull EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player player && event.getEntity() instanceof Tameable tameable && player.equals(tameable.getOwner())) {
			event.setCancelled(true);
		}
	}
}
