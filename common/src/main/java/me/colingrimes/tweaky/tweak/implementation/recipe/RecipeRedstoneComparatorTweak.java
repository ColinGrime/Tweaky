package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;

public class RecipeRedstoneComparatorTweak extends RecipeTweak {

	public RecipeRedstoneComparatorTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_redstone_comparator");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapelessRecipe(key, new ItemStack(Material.COMPARATOR))
				.addIngredient(Material.REPEATER)
				.addIngredient(Material.QUARTZ);
	}
}
