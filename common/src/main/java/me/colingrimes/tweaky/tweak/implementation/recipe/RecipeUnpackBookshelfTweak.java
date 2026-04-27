package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;

public class RecipeUnpackBookshelfTweak extends RecipeTweak {

	public RecipeUnpackBookshelfTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_unpack_bookshelf");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapelessRecipe(key, new ItemStack(Material.BOOK, 3)).addIngredient(Material.BOOKSHELF);
	}
}
