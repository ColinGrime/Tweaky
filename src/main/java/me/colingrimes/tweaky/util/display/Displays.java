package me.colingrimes.tweaky.util.display;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;

import javax.annotation.Nonnull;

public final class Displays {

	/**
	 * Creates an {@link ItemDisplay} at the specified location.
	 *
	 * @param location the location
	 * @param material the material
	 * @param transform the item display transform
	 * @return the created item display
	 */
	@Nonnull
	public static ItemDisplay createItem(@Nonnull Location location, @Nonnull Material material, @Nonnull ItemDisplay.ItemDisplayTransform transform) {
		Preconditions.checkArgument(location.getWorld() != null, "World is null.");
		return location.getWorld().spawn(location, ItemDisplay.class, (entity) -> {
			entity.setItemStack(new ItemStack(material));
			entity.setItemDisplayTransform(transform);
		});
	}

	/**
	 * Creates a {@link BlockDisplay} at the specified location.
	 *
	 * @param location the location
	 * @param block the block data
	 * @return the created block display
	 */
	@Nonnull
	public static BlockDisplay createBlock(@Nonnull Location location, @Nonnull BlockData block) {
		return createBlock(location, block, Transformations.basic());
	}

	/**
	 * Creates a {@link BlockDisplay} at the specified location.
	 *
	 * @param location the location
	 * @param block the block data
	 * @param transformation the transformation
	 * @return the created block display
	 */
	@Nonnull
	public static BlockDisplay createBlock(@Nonnull Location location, @Nonnull BlockData block, @Nonnull Transformation transformation) {
		Preconditions.checkArgument(location.getWorld() != null, "World is null.");
		return location.getWorld().spawn(location, BlockDisplay.class, (entity) -> {
			entity.setBlock(block);
			entity.setTransformation(transformation);
		});
	}

	/**
	 * Creates an {@link Interaction} at the specified location.
	 *
	 * @param location the location
	 * @return the created interaction
	 */
	@Nonnull
	public static Interaction createInteraction(@Nonnull Location location) {
		return createInteraction(location, 1, 1);
	}

	/**
	 * Creates an {@link Interaction} at the specified location.
	 *
	 * @param location the location
	 * @param height the height
	 * @param width the width
	 * @return the created interaction
	 */
	@Nonnull
	public static Interaction createInteraction(@Nonnull Location location, float width, float height) {
		Preconditions.checkArgument(location.getWorld() != null, "World is null.");
		return location.getWorld().spawn(location, Interaction.class, (entity) -> {
			entity.setInteractionWidth(width);
			entity.setInteractionHeight(height);
			entity.setResponsive(true);
		});
	}

	private Displays() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}