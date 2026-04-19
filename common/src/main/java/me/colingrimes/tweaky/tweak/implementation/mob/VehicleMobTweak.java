package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class VehicleMobTweak extends DefaultTweak {

	public VehicleMobTweak(@Nonnull Tweaky plugin) {
		super(plugin, "vehicle_mobs");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(i -> getVehicleEntity(i) != null)
				.entity(this::canRideVehicle);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		ItemStack item = player.getInventory().getItem(event.getHand());
		EntityType type = Objects.requireNonNull(getVehicleEntity(item));

		Vehicle vehicle = (Vehicle) entity.getWorld().spawnEntity(entity.getLocation(), type);
		vehicle.addPassenger(entity);

		Items.remove(item);
		Players.swingHand(player, event.getHand());
	}

	/**
	 * Converts a vehicle item into its corresponding entity form.
	 *
	 * @param item the item to convert
	 * @return the vehicle type
	 */
	@Nullable
	private EntityType getVehicleEntity(@Nonnull ItemStack item) {
		return switch (item.getType()) {
			// Boats
			case OAK_BOAT               -> EntityType.OAK_BOAT;
			case SPRUCE_BOAT            -> EntityType.SPRUCE_BOAT;
			case BIRCH_BOAT             -> EntityType.BIRCH_BOAT;
			case JUNGLE_BOAT            -> EntityType.JUNGLE_BOAT;
			case ACACIA_BOAT            -> EntityType.ACACIA_BOAT;
			case DARK_OAK_BOAT          -> EntityType.DARK_OAK_BOAT;
			case MANGROVE_BOAT          -> EntityType.MANGROVE_BOAT;
			case CHERRY_BOAT            -> EntityType.CHERRY_BOAT;
			case PALE_OAK_BOAT          -> EntityType.PALE_OAK_BOAT;
			case BAMBOO_RAFT            -> EntityType.BAMBOO_RAFT;
			// Mine Carts
			case MINECART               -> EntityType.MINECART;
			// Default
			default -> null;
		};
	}

	/**
	 * Checks if the entity can go inside a vehicle.
	 *
	 * @param entity the entity
	 * @return true if the entity can go inside a vehicle
	 */
	private boolean canRideVehicle(@Nonnull Entity entity) {
		return entity instanceof Animals || entity instanceof Villager;
	}
}
