package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.annotation.Nonnull;

public class RecipeUnlockAllTweak extends Tweak {

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
		return TweakItem
				.of(Material.CRAFTING_TABLE)
				.name("&aUnlock All Recipes")
				.lore("&7All recipes are unlocked.")
				.usage("&eUsage: &aAutomatically unlock all recipes.");
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		event.getPlayer().discoverRecipes(plugin.getAllRecipes());
	}
}
