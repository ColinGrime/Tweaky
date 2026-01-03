package me.colingrimes.tweaky.util.bukkit;

import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class Blocks {

	private static final List<Material> PLANTS = List.of(
			Material.SHORT_GRASS,
			Material.TALL_GRASS,
			Material.FERN,
			Material.LARGE_FERN,
			Material.DEAD_BUSH,
			Material.LEAF_LITTER,
			Material.BUSH
	);

	/**
	 * Edits the block data of a block and sets it to the block.
	 *
	 * @param block the block to edit
	 * @param type the type of block data
	 * @param editor the changes you want applied to the block data
	 * @param <T> the block data type
	 */
	public static <T extends BlockData> void edit(@Nullable Block block, @Nonnull Class<T> type, @Nonnull Consumer<T> editor) {
		if (block == null || !type.isInstance(block.getBlockData())) {
			return;
		}

		T blockData = type.cast(block.getBlockData());
		editor.accept(blockData);
		block.setBlockData(blockData);
	}

	/**
	 * Breaks the block naturally and plays the block's break sound.
	 *
	 * @param block the block to break
	 */
	public static void destroy(@Nonnull Block block) {
		breakSound(block);
		block.breakNaturally();
	}

	/**
	 * Breaks the block naturally and plays the block's break sound.
	 *
	 * @param block the block to break
	 * @param item the item that broke the block
	 */
	public static void destroy(@Nonnull Block block, @Nonnull ItemStack item) {
		breakSound(block);
		block.breakNaturally(item);
	}

	/**
	 * Plays the block's place sound.
	 *
	 * @param block the block
	 */
	public static void placeSound(@Nonnull Block block) {
		Sounds.play(block, block.getBlockData().getSoundGroup().getPlaceSound());
	}

	/**
	 * Plays the block's break sound.
	 *
	 * @param block the block
	 */
	public static void breakSound(@Nonnull Block block) {
		Sounds.play(block, block.getBlockData().getSoundGroup().getBreakSound());
	}

	/**
	 * Checks if the specified block is interactable.
	 *
	 * @param block the block to check
	 * @return true if the block is interactable
	 */
	public static boolean isInteractable(@Nullable Block block) {
		if (block == null) {
			return false;
		}

		Material type = block.getType();
		if (!type.isInteractable()) {
			return false;
		}

		return !Tag.STAIRS.isTagged(type) && !Tag.FENCES.isTagged(type);
	}

	/**
	 * Checks if the specified type is a plant (flowers, grasses).
	 *
	 * @param type the type to check
	 * @return true if the material is a plant
	 */
	public static boolean isPlant(@Nonnull Material type) {
		return Tag.FLOWERS.isTagged(type) || Tag.SMALL_FLOWERS.isTagged(type) || PLANTS.contains(type);
	}
}
