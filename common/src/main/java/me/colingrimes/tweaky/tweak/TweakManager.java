package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.implementation.hidden.BreedingIndicatorTweak_RoseStackerFix;
import me.colingrimes.tweaky.tweak.implementation.text.BreedingIndicatorTweak;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.io.Introspector;
import me.colingrimes.tweaky.util.io.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public class TweakManager {

	private final Tweaky plugin;
	private final List<Tweak> tweaks;
	private final List<String> availableTweaks;
	private final Map<TweakCategory, Set<Tweak>> categoryToTweaks = new HashMap<>();
	private int tweakCount = 0;

	public TweakManager(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
		this.tweaks = new ArrayList<>();
		this.availableTweaks = plugin.getConfig().getConfigurationSection("tweaks").getKeys(false).stream().toList();
	}

	/**
	 * Registers all tweaks. This performs a number of actions:
	 * <ul>
	 *   <li>Unregisters, shuts down, and cleans up all previously activated tweaks.</li>
	 *   <li>Gets all tweak classes and initializes all enabled ones (ensuring compatability).</li>
	 *   <li>Activates all enabled tweaks to set them up properly.</li>
	 *   <li>Caches and returns the number of tweaks that were activated.</li>
	 * </ul>
	 */
	public int register() {
		// Clean up.
		shutdown();

		// Registration.
		List<Tweak> tweakClasses = Introspector.getTweaks(plugin);
		List<Tweak> enabledClasses = tweakClasses.stream().filter(Tweak::isEnabled).collect(Collectors.toList());
		List<Tweak> filteredClasses = checkForHooks(enabledClasses);

		for (Tweak tweak : filteredClasses) {
			initializeCategory(tweak);
			tweak.init();
			tweaks.add(tweak);
			categoryToTweaks.computeIfAbsent(tweak.getProperties().getCategory(), k -> new HashSet<>()).add(tweak);
		}

		// Tweak count.
		tweakCount = tweaks.stream().mapToInt(Tweak::getCount).sum();
		Logger.log("Registered " + tweakCount + " tweaks.");
		return tweakCount;
	}

	/**
	 * Handles the proper shutdown of tweaks.
	 */
	public void shutdown() {
		tweaks.forEach(tweak -> {
			HandlerList.unregisterAll(tweak);
			tweak.shutdown();
		});
		tweaks.clear();
		categoryToTweaks.clear();
	}

	/**
	 * Gets the names of all available tweaks, including disabled tweaks.
	 *
	 * @return the list of all available tweak names
	 */
	@Nonnull
	public List<String> getAvailableTweaks() {
		return availableTweaks;
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
	 * Gets all the enabled tweaks for the specified player.
	 *
	 * @param player the player
	 * @return the list of enabled tweaks for the player
	 */
	@Nonnull
	public List<Tweak> getTweaks(@Nonnull Player player) {
		return tweaks.stream().filter(tweak -> tweak.hasPermission(player)).toList();
	}

	/**
	 * Gets all the enabled tweaks for the specified player and category.
	 *
	 * @param player the player
	 * @param category the category
	 * @return the list of enabled tweaks for the player and category
	 */
	@Nonnull
	public List<Tweak> getTweaks(@Nonnull Player player, @Nonnull TweakCategory category) {
		return categoryToTweaks.computeIfAbsent(category, k -> new HashSet<>()).stream().filter(tweak -> tweak.hasPermission(player)).toList();
	}

	/**
	 * Gets the number of activated tweaks.
	 *
	 * @return the count of activated tweaks
	 */
	public int getTweakCount() {
		return tweakCount;
	}

	/**
	 * Gets the number of activated tweaks for the specific player.
	 *
	 * @return the count of activated tweaks for the player
	 */
	public int getTweakCount(@Nonnull Player player) {
		return getTweaks(player).stream().mapToInt(Tweak::getCount).sum();
	}

	/**
	 * Gets the number of tweaks from the list of tweaks.
	 *
	 * @param tweaks the tweaks to count
	 * @return the number of tweaks
	 */
	public static int countTweaks(@Nonnull List<Tweak> tweaks) {
		return tweaks.stream().mapToInt(Tweak::getCount).sum();
	}

	/**
	 * Checks for hooks to fix potential conflicts from other plugins.
	 *
	 * @return updated list of tweaks if applicable
	 */
	@Nonnull
	private List<Tweak> checkForHooks(@Nonnull List<Tweak> tweaks) {
		ListIterator<Tweak> iterator = tweaks.listIterator();
		while (iterator.hasNext()) {
			checkForHooks(iterator, iterator.next());
		}
		return tweaks;
	}

	/**
	 * Checks for any hooks to fix potential conflicts from other plugins.
	 *
	 * @param iterator the tweak iteraotr
	 * @param tweak the current tweak of the iterator
	 */
	private void checkForHooks(@Nonnull ListIterator<Tweak> iterator, @Nonnull Tweak tweak) {
		boolean breedingIndicatorRoseStackerHook = tweak instanceof BreedingIndicatorTweak && Bukkit.getPluginManager().isPluginEnabled("ProtocolLib") && Bukkit.getPluginManager().isPluginEnabled("RoseStacker");
		if (breedingIndicatorRoseStackerHook) {
			iterator.remove();
			iterator.add(new BreedingIndicatorTweak_RoseStackerFix(plugin));
		}
	}

	/**
	 * Initializes the category of the tweak by looking at its package name.
	 *
	 * @param tweak the tweak
	 */
	private void initializeCategory(@Nonnull Tweak tweak) {
		String[] packages = tweak.getClass().getPackageName().split("\\.");
		String packageName = packages[packages.length-1];
		TweakCategory category = switch (packageName) {
			case "mob" -> TweakCategory.MOBS;
			case "block" -> TweakCategory.BLOCKS;
			case "crop" -> TweakCategory.CROPS;
			case "text" -> TweakCategory.TEXT;
			case "convenience" -> TweakCategory.CONVENIENCE;
			case "recipe" -> TweakCategory.RECIPES;
			case "misc" -> TweakCategory.MISCELLANEOUS;
			default -> TweakCategory.UNKNOWN;
		};

		// Set the category if it wasn't already set by the tweak.
		if (tweak.getProperties().getCategory() == TweakCategory.UNKNOWN) {
			tweak.getProperties().setCategory(category);
		}
	}
}
