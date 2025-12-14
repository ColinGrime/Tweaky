package me.colingrimes.tweaky;

import me.colingrimes.tweaky.command.TweakCommand;
import me.colingrimes.tweaky.config.Settings;
import me.colingrimes.tweaky.listener.PlayerListeners;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Introspector;
import me.colingrimes.tweaky.util.Logger;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tweaky extends JavaPlugin {

	private static Tweaky instance;
	private final List<Tweak> tweaks = new ArrayList<>();
	private List<NamespacedKey> allRecipes;
	private Settings settings;

	@Override
	public void onEnable() {
		instance = this;

		// Initialize settings.
		settings = new Settings(this);
		settings.reload();

		// Setup commands + listeners.
		Bukkit.getPluginCommand("tweaky").setExecutor(new TweakCommand(this));
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);

		// Register all the tweaks.
		registerTweaks();

		// Check for Metrics.
		if (settings.ENABLE_METRICS.get()) {
			new Metrics(this, 25315);
		}
	}

	@Override
	public void onDisable() {
		tweaks.forEach(tweak -> {
			HandlerList.unregisterAll(tweak);
			tweak.shutdown();
		});
	}

	@Nonnull
	public static Tweaky getInstance() {
		return instance;
	}

	/**
	 * Gets all the enabled tweaks.
	 *
	 * @return the list of enabled tweaks
	 */
	@Nonnull
	public List<Tweak> getTweaks() {
		return tweaks;
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
	 * Registers all the tweaks.
	 */
	public int registerTweaks() {
		// Clean up.
		tweaks.forEach(tweak -> {
			HandlerList.unregisterAll(tweak);
			tweak.shutdown();
		});
		tweaks.clear();

		// Registration.
		List<Class<?>> classes = Introspector.getClasses(getClassLoader(), getClass().getPackage().getName() + ".tweak.implementation");
		List<Tweak> tweakClasses = Introspector.instantiateClasses(classes, Tweak.class, this);
		tweaks.addAll(tweakClasses.stream().filter(Tweak::isEnabled).toList());
		tweaks.forEach(Tweak::init);

		int count = tweaks.stream().mapToInt(Tweak::getCount).sum();
		Logger.log("Registered " + count + " tweaks.");
		return count;
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
