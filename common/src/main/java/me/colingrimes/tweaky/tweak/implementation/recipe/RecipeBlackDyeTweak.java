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

public class RecipeBlackDyeTweak extends RecipeTweak {

	private static final List<Material> COALS = List.copyOf(Tag.ITEMS_COALS.getValues());

	public RecipeBlackDyeTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_black_dye");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		return new ShapelessRecipe(key, new ItemStack(Material.BLACK_DYE)).addIngredient(new RecipeChoice.MaterialChoice(COALS));
	}
}
