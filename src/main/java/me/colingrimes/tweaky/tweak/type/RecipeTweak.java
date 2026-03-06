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

	public RecipeTweak(@Nonnull Tweaky plugin, @Nonnull String id) {
		super(plugin, id);
		this.RECIPE_KEY = new NamespacedKey(plugin, id + "_recipe");
	}

	@Override
	public final void init() {
		if (Bukkit.getRecipe(RECIPE_KEY) == null) {
			Bukkit.addRecipe(recipe(RECIPE_KEY));
		}
	}

	@Override
	public final void shutdown() {
		if (Bukkit.getRecipe(RECIPE_KEY) != null) {
			Bukkit.removeRecipe(RECIPE_KEY);
		}
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.setPermissionRequired(false);
	}

	/**
	 * Gets the recipe associated with this tweak.
	 *
	 * @param key the recipe key
	 * @return the recipe to add
	 */
	@Nonnull
	protected abstract Recipe recipe(@Nonnull NamespacedKey key);
}
