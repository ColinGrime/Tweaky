package me.colingrimes.tweaky;

import me.colingrimes.tweaky.command.TweaksCommand;
import me.colingrimes.tweaky.command.TweakyCommand;
import me.colingrimes.tweaky.config.Settings;
import me.colingrimes.tweaky.listener.MenuListeners;
import me.colingrimes.tweaky.listener.PlayerListeners;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.tweak.TweakManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Tweaky extends JavaPlugin {

	private static Tweaky instance;
	private TweakManager tweakManager;
	private Settings settings;
	private boolean isPaper = false;
	private List<NamespacedKey> allRecipes;

	@Override
	public void onEnable() {
		instance = this;

		// Initialize settings.
		settings = new Settings(this);
		settings.reload();

		// Setup commands + listeners.
		Bukkit.getPluginCommand("tweaky").setExecutor(new TweakyCommand(this));
		Bukkit.getPluginCommand("tweaks").setExecutor(new TweaksCommand(this));
		Bukkit.getPluginManager().registerEvents(new MenuListeners(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);

		// Register all the tweaks.
		tweakManager = new TweakManager(this);
		tweakManager.register();

		// Check for Metrics.
		if (settings.ENABLE_METRICS.get()) {
			new Metrics(this, 25315);
		}
	}

	@Override
	public void onDisable() {
		new HashSet<>(Gui.players.values()).forEach(Gui::invalidate);
		tweakManager.shutdown();
	}

	@Nonnull
	public static Tweaky getInstance() {
		return instance;
	}

	/**
	 * Gets the manager responsible for tweaks.
	 *
	 * @return the tweak manager
	 */
	@Nonnull
	public TweakManager getTweakManager() {
		return tweakManager;
	}

	/**
	 * Gets all the settings for the plugin.
	 *
	 * @return the plugin's settings
	 */
	@Nonnull
	public Settings getSettings() {
		return settings;
	}

	/**
	 * Checks if the server is running Paper.
	 *
	 * @return true if the server is a Paper server
	 */
	public boolean isPaper() {
		if (isPaper) {
			return true;
		}
		try {
			Class.forName("com.destroystokyo.paper.ParticleBuilder");
			isPaper = true;
			return true;
		} catch (ClassNotFoundException ignored) {
			return false;
		}
	}

	/**
	 * Gets a list of all recipes in Minecraft.
	 *
	 * @return a list of all recipes
	 */
	@Nonnull
	public List<NamespacedKey> getAllRecipes() {
		if (allRecipes != null) {
			return allRecipes;
		}

		List<NamespacedKey> recipes = new ArrayList<>();
		Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
		while (recipeIterator.hasNext()) {
			Recipe recipe = recipeIterator.next();
			if (recipe instanceof Keyed keyed) {
				recipes.add(keyed.getKey());
			}
		}
		allRecipes = recipes;
		return recipes;
	}
}
