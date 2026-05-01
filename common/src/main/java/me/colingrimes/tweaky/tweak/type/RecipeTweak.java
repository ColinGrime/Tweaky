package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;

import javax.annotation.Nonnull;

/**
 * Represents a {@link Tweak} whose action involves adding a new recipe.
 */
public abstract class RecipeTweak extends DefaultTweak {

	protected final NamespacedKey RECIPE_KEY;
	protected final Recipe recipe;

	public RecipeTweak(@Nonnull Tweaky plugin, @Nonnull String id) {
		super(plugin, id);
		this.RECIPE_KEY = new NamespacedKey(plugin, id);
		this.recipe = recipe(RECIPE_KEY);
	}

	@Override
	public final void enable() {
		super.enable();
		if (Bukkit.getRecipe(RECIPE_KEY) == null) {
			Bukkit.addRecipe(recipe);
		}
	}

	@Override
	public final void disable() {
		super.disable();
		if (Bukkit.getRecipe(RECIPE_KEY) != null) {
			Bukkit.removeRecipe(RECIPE_KEY);
		}
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.setPermissionRequired(false);
	}

	/**
	 * Gets the recipe to be added to the game.
	 *
	 * @param key the recipe key
	 * @return the recipe to add
	 */
	@Nonnull
	protected abstract Recipe recipe(@Nonnull NamespacedKey key);

	/**
	 * Gets the recipe associated with this tweak.
	 *
	 * @return the recipe
	 */
	@Nonnull
	public Recipe getRecipe() {
		return recipe;
	}
}
