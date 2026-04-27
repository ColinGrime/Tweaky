package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import javax.annotation.Nonnull;

public class RecipeGodAppleTweak extends RecipeTweak {

	public RecipeGodAppleTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_god_apple");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapedRecipe(key, new ItemStack(Material.ENCHANTED_GOLDEN_APPLE))
				.shape("GGG",
					   "GAG",
					   "GGG")
				.setIngredient('G', Material.GOLD_BLOCK)
				.setIngredient('A', Material.APPLE);
	}
}
