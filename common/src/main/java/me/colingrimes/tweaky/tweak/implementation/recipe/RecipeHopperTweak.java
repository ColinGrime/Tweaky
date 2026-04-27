package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeHopperTweak extends RecipeTweak {

	private static final List<Material> LOGS = List.copyOf(Tag.LOGS.getValues());

	public RecipeHopperTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_hopper");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapedRecipe(key, new ItemStack(Material.HOPPER))
				.shape("ILI",
					   "ILI",
					   " I ")
				.setIngredient('I', new RecipeChoice.MaterialChoice(Material.IRON_INGOT))
				.setIngredient('L', new RecipeChoice.MaterialChoice(LOGS));
	}
}
