package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
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

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		event.getPlayer().discoverRecipes(plugin.getAllRecipes());
	}
}
