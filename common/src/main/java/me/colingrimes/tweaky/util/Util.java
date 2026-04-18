package me.colingrimes.tweaky.util;

import com.google.common.base.Preconditions;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Util {

	/**
	 * Retrieves all locations around a location.
	 *
	 * @param location the location
	 * @param radius the radius around the location
	 * @return all blocks around the location
	 */
	@Nonnull
	public static List<Location> around(@Nonnull Location location, double radius) {
		Location corner1 = location.clone().add(+radius, +radius, +radius);
		Location corner2 = location.clone().add(-radius, -radius, -radius);
		return between(corner1, corner2);
	}

	/**
	 * Retrieves all locations between two locations.
	 *
	 * @param corner1 the first corner
	 * @param corner2 the second corner
	 * @return all blocks between the two locations
	 */
	@Nonnull
	public static List<Location> between(@Nonnull Location corner1, @Nonnull Location corner2) {
		int x1 = corner1.getBlockX(), y1 = corner1.getBlockY(), z1 = corner1.getBlockZ();
		int x2 = corner2.getBlockX(), y2 = corner2.getBlockY(), z2 = corner2.getBlockZ();
		Location min = new Location(corner1.getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
		Location max = new Location(corner1.getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));

		List<Location> locations = new ArrayList<>();
		for (int y=max.getBlockY(); y>=min.getBlockY(); y--) {
			for (int x=min.getBlockX(); x<=max.getBlockX(); x++) {
				for (int z=min.getBlockZ(); z<=max.getBlockZ(); z++) {
					locations.add(new Location(corner1.getWorld(), x, y, z));
				}
			}
		}
		return locations;
	}

	/**
	 * Gets the direction from the source vector to the target vector.
	 *
	 * @param source the source vector
	 * @param target the target vector
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Vector source, @Nonnull Vector target) {
		return target.subtract(source).normalize();
	}

	/**
	 * Gets the direction from the source location to the target location.
	 *
	 * @param source the source location
	 * @param target the target location
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Location source, @Nonnull Location target) {
		return direction(source.toVector(), target.toVector());
	}

	/**
	 * Gets the direction from the source entity to the target entity.
	 *
	 * @param source the source entity
	 * @param target the target entity
	 * @return the resulting unit vector
	 */
	@Nonnull
	public static Vector direction(@Nonnull Entity source, @Nonnull Entity target) {
		return direction(source.getLocation(), target.getLocation());
	}

	/**
	 * Gets all nearby entities.
	 *
	 * @param location the location
	 * @param distance the distance
	 * @return all nearby entities
	 */
	@Nonnull
	public static Collection<Entity> nearby(@Nonnull Location location, double distance) {
		return nearby(location, distance, distance, distance);
	}

	/**
	 * Gets all nearby entities.
	 *
	 * @param location the location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return all nearby entities
	 */
	@Nonnull
	public static Collection<Entity> nearby(@Nonnull Location location, double x, double y, double z) {
		Preconditions.checkNotNull(location.getWorld(), "World is null.");
		return location.getWorld().getNearbyEntities(location, x, y, z);
	}

	/**
	 * Gets all nearby entities of the specified type.
	 *
	 * @param entityType the type of entity to filter by
	 * @param location the location
	 * @param distance the distance
	 * @return all nearby entities of the specified type
	 */
	@Nonnull
	public static <T extends Entity> Collection<T> nearby(@Nonnull Class<T> entityType, @Nonnull Location location, double distance) {
		return nearby(location, distance)
				.stream()
				.filter(entityType::isInstance)
				.map(entityType::cast)
				.toList();
	}

	/**
	 * Gets all nearby entities of the specified type.
	 *
	 * @param entityType the type of entity to filter by
	 * @param location the location
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @return all nearby entities of the specified type
	 */
	@Nonnull
	public static <T extends Entity> Collection<T> nearby(@Nonnull Class<T> entityType, @Nonnull Location location, double x, double y, double z) {
		return nearby(location, x, y, z)
				.stream()
				.filter(entityType::isInstance)
				.map(entityType::cast)
				.toList();
	}

	/**
	 * Converts the string into the given Enum value.
	 *
	 * @param enumType the enum to parse into
	 * @param value    the string value to parse
	 * @return the converted enum value
	 */
	@Nullable
	public static <E extends Enum<E>> E parse(@Nonnull Class<E> enumType, @Nullable String value) {
		return Arrays.stream(enumType.getEnumConstants()).filter(e -> e.name().equalsIgnoreCase(value)).findFirst().orElse(null);
	}
}
