package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nonnull;

public class RecipeLeatherTweak extends RecipeTweak {

	public RecipeLeatherTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_leather");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new FurnaceRecipe(
				key,
				new ItemStack(Material.LEATHER),
				Material.ROTTEN_FLESH,
				0.7f,
				200
		);
	}
}
