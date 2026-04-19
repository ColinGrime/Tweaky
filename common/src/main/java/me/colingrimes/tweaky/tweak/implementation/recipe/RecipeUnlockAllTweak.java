package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RecipeUnlockAllTweak extends DefaultTweak {

	private final List<NamespacedKey> recipes = new ArrayList<>();

	public RecipeUnlockAllTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_unlock_all");
	}

	@Override
	public void onEnable() {
		Bukkit.recipeIterator().forEachRemaining(recipe -> {
			if (recipe instanceof Keyed keyed) {
				recipes.add(keyed.getKey());
			}
		});
	}

	@Override
	public void onDisable() {
		recipes.clear();
	}

	@TweakHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		event.getPlayer().discoverRecipes(recipes);
	}
}
