package me.colingrimes.tweaky.menu.tweak.recipe;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import javax.annotation.Nonnull;

public final class RecipeUtil {

	/**
	 * Gets the inventory type from the recipe.
	 *
	 * @param recipe the recipe
	 * @return the inventory type (or chest if not found)
	 */
	@Nonnull
	public static InventoryType getInventoryTypeFromRecipe(@Nonnull Recipe recipe) {
		if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
			return InventoryType.WORKBENCH;
		} else if (recipe instanceof FurnaceRecipe) {
			return InventoryType.FURNACE;
		} else {
			return InventoryType.CHEST;
		}
	}
}
