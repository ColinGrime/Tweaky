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

public class RecipeSticksTweak extends RecipeTweak {

	private static final List<Material> LOGS = List.copyOf(Tag.LOGS.getValues());

	public RecipeSticksTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_sticks");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapedRecipe(key, new ItemStack(Material.STICK, 16))
				.shape("L",
					   "L")
				.setIngredient('L', new RecipeChoice.MaterialChoice(LOGS));
	}
}
