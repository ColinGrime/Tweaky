package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RecipeUnlockAllTweak extends Tweak {

	private final List<NamespacedKey> recipes = new ArrayList<>();

	public RecipeUnlockAllTweak(@Nonnull Tweaky plugin) {
		super(plugin, "recipe_unlock_all");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_RECIPE_UNLOCK_ALL.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_RECIPE_UNLOCK_ALL.get().material(Material.CRAFTING_TABLE);
	}

	@Override
	public void init() {
		Bukkit.recipeIterator().forEachRemaining(recipe -> {
			if (recipe instanceof Keyed keyed) {
				recipes.add(keyed.getKey());
			}
		});
	}

	@Override
	public void shutdown() {
		recipes.clear();
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		if (hasPermission(event.getPlayer())) {
			event.getPlayer().discoverRecipes(recipes);
		}
	}
}
