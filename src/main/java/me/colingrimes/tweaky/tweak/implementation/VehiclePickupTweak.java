package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VehiclePickupTweak extends Tweak {

	public VehiclePickupTweak(@Nonnull Tweaky plugin) {
		super(plugin, "vehicle_pickup");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_VEHICLE_PICKUP.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_VEHICLE_PICKUP.get().material(Material.MINECART);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || event.getHand() != EquipmentSlot.HAND || !player.isSneaking() || !(event.getRightClicked() instanceof Vehicle vehicle)) {
			return;
		}

		VehicleDamageEvent vehicleDamage = new VehicleDamageEvent(vehicle, player, 0);
		Bukkit.getPluginManager().callEvent(vehicleDamage);
		if (vehicleDamage.isCancelled()) {
			return;
		}

		ItemStack item = getVehicleItem(vehicle);
		if (item == null) {
			return;
		}

		player.swingMainHand();
		Sounds.play(vehicle, Sound.ENTITY_PLAYER_ATTACK_CRIT);
		vehicle.remove();
		event.setCancelled(true);

		if (!player.getInventory().addItem(item).isEmpty()) {
			vehicle.getWorld().dropItemNaturally(vehicle.getLocation().add(0, 0.5, 0), item);
		}
	}

	/**
	 * Converts a vehicle entity into its corresponding item form.
	 *
	 * @param entity the entity to convert
	 * @return the vehicle item
	 */
	@Nullable
	public ItemStack getVehicleItem(@Nonnull Entity entity) {
		return switch (entity.getType()) {
			// Boats
			case OAK_BOAT               -> new ItemStack(Material.OAK_BOAT);
			case SPRUCE_BOAT            -> new ItemStack(Material.SPRUCE_BOAT);
			case BIRCH_BOAT             -> new ItemStack(Material.BIRCH_BOAT);
			case JUNGLE_BOAT            -> new ItemStack(Material.JUNGLE_BOAT);
			case ACACIA_BOAT            -> new ItemStack(Material.ACACIA_BOAT);
			case DARK_OAK_BOAT          -> new ItemStack(Material.DARK_OAK_BOAT);
			case MANGROVE_BOAT          -> new ItemStack(Material.MANGROVE_BOAT);
			case BAMBOO_RAFT            -> new ItemStack(Material.BAMBOO_RAFT);
			// Mine Carts
			case MINECART               -> new ItemStack(Material.MINECART);
			case CHEST_MINECART         -> new ItemStack(Material.CHEST_MINECART);
			case FURNACE_MINECART       -> new ItemStack(Material.FURNACE_MINECART);
			case TNT_MINECART           -> new ItemStack(Material.TNT_MINECART);
			case HOPPER_MINECART        -> new ItemStack(Material.HOPPER_MINECART);
			case COMMAND_BLOCK_MINECART -> new ItemStack(Material.COMMAND_BLOCK_MINECART);
			// Default
			default -> null;
		};
	}
}
