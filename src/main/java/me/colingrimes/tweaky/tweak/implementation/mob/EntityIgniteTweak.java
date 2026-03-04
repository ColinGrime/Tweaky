package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;

public class EntityIgniteTweak extends DefaultTweak {

	public EntityIgniteTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_ignite");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.anyItem(Material.FLINT_AND_STEEL, Material.FIRE_CHARGE)
				.entity(e -> e.getType() != EntityType.PLAYER && e.getType() != EntityType.CREEPER);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		useItem(event.getPlayer(), event.getHand(), event.getRightClicked().getLocation());
		event.getRightClicked().setFireTicks(20 * 8);
		event.setCancelled(true);
	}

	/**
	 * Uses the item in the player's hand at the given location.
	 *
	 * @param player the player
	 * @param hand the hand that is used
	 * @param location the location that the item is used
	 */
	private void useItem(@Nonnull Player player, @Nonnull EquipmentSlot hand, @Nonnull Location location) {
		Sound sound = player.getInventory().getItem(hand).getType() == Material.FLINT_AND_STEEL ? Sound.ITEM_FLINTANDSTEEL_USE : Sound.ITEM_FIRECHARGE_USE;
		Players.use(player, hand, sound, location);
	}
}
