package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;

public class RecipeGoldenMelonTweak extends RecipeTweak {

	public RecipeGoldenMelonTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_golden_melon");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapelessRecipe(key, new ItemStack(Material.GLISTERING_MELON_SLICE))
				.addIngredient(Material.GOLD_INGOT)
				.addIngredient(Material.MELON_SLICE);
	}
}
