package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class RecipeUnpackWoolTweak extends RecipeTweak {

	private static final List<Material> WOOLS = List.copyOf(Tag.WOOL.getValues());

	public RecipeUnpackWoolTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_unpack_wool");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapelessRecipe(key, new ItemStack(Material.STRING, 4)).addIngredient(new RecipeChoice.MaterialChoice(WOOLS));
	}
}
