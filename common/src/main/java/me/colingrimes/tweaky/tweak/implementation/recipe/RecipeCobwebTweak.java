package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import javax.annotation.Nonnull;

public class RecipeCobwebTweak extends RecipeTweak {

	public RecipeCobwebTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_cobweb");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapedRecipe(key, new ItemStack(Material.COBWEB))
				.shape("S S",
					   " S ",
					   "S S")
				.setIngredient('S', Material.STRING);
	}
}
