package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class EntitySetOnFireTweak extends Tweak {

	public EntitySetOnFireTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_set_on_fire");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ENTITY_SET_ON_FIRE.get();
	}

	@EventHandler
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Material type = item.getType();
		if (event.getHand() != EquipmentSlot.HAND || (type != Material.FLINT_AND_STEEL && type != Material.FIRE_CHARGE)) {
			return;
		}

		// Ignore creepers.
		if (event.getRightClicked().getType() == EntityType.CREEPER) {
			return;
		}

		event.getRightClicked().setFireTicks(20 * 8);
		event.setCancelled(true);
		player.swingMainHand();

		// Damage flint & steel.
		if (type == Material.FLINT_AND_STEEL && Util.damage(item)) {
			Util.sound(player, Sound.ENTITY_ITEM_BREAK);
		}

		// Use a single fire charge.
		if (type == Material.FIRE_CHARGE) {
			Util.removeSingle(item);
		}
	}
}
