package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.implementation.hidden.BreedingIndicatorTweak_RoseStackerFix;
import me.colingrimes.tweaky.tweak.implementation.misc.SnowballTweak;
import me.colingrimes.tweaky.tweak.implementation.text.BreedingIndicatorTweak;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.io.Introspector;
import me.colingrimes.tweaky.util.io.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public class TweakManager {

	private final Tweaky plugin;
	private final List<String> allTweakIds = new ArrayList<>();
	private final List<Tweak> allTweaks = new ArrayList<>();
	private final List<Tweak> enabledTweaks = new ArrayList<>();
	private final Map<TweakCategory, Set<Tweak>> categoryToTweaks = new HashMap<>();
	private int tweakCount = 0;
	private boolean initialized = false;

	public TweakManager(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	/**
	 * Initializes all tweak classes. This performs a number of actions:
	 * <ul>
	 *   <li>Dynamically loads all classes containing tweaks.</li>
	 *   <li>Creates an instance of all tweak classes found.</li>
	 *   <li>Sets the category of the tweak based on its package.</lI>
	 *   <li>Gets the tweak IDs of all valid tweaks (to be displayed in the /tweaky list command).</li>
	 * </ul>
	 */
	public void init() {
		if (initialized) {
			return;
		}

		List<Tweak> tweakClasses = Introspector.getTweaks(plugin);
		List<Tweak> filteredClasses = checkForHooks(tweakClasses);
		allTweaks.addAll(filteredClasses);
		allTweaks.forEach(this::initializeCategory);
		Logger.log("Found " + allTweaks.size() + " tweak classes.");

		// Get all tweak IDs.
		for (Tweak tweak : allTweaks.stream().sorted(Comparator.comparing(Tweak::getId)).toList()) {
			if (tweak instanceof SnowballTweak) {
				allTweakIds.addAll(List.of("snowballs-add-snow-layer", "snowballs-break-plants", "snowballs-damage", "snowballs-extinguish-entities", "snowballs-extinguish-fire", "snowballs-form-ice", "snowballs-form-snow", "snowballs-knockback"));
			} else if (tweak.getProperties().getCategory() != TweakCategory.UNKNOWN) {
				allTweakIds.add(tweak.getId().replace("_", "-"));
			}
		}

		initialized = true;
	}

	/**
	 * Registers all tweaks. This performs a number of actions:
	 * <ul>
	 *   <li>Unregisters, shuts down, and cleans up all previously enabled tweaks.</li>
	 *   <li>Gets all enabled tweak classes and initializes them.</li>
	 *   <li>Caches and returns the number of tweaks that were enabled.</li>
	 * </ul>
	 */
	public int register() {
		// Clean up.
		shutdown();

		// Registration.
		List<Tweak> enabledClasses = allTweaks.stream().filter(Tweak::isEnabled).toList();
		for (Tweak tweak : enabledClasses) {
			tweak.enable();
			enabledTweaks.add(tweak);
			categoryToTweaks.computeIfAbsent(tweak.getProperties().getCategory(), k -> new HashSet<>()).add(tweak);
		}

		// Tweak count.
		tweakCount = enabledTweaks.stream().mapToInt(Tweak::getCount).sum();
		Logger.log("Registered " + tweakCount + " tweaks.");
		return tweakCount;
	}

	/**
	 * Handles the proper shutdown of tweaks.
	 */
	public void shutdown() {
		enabledTweaks.forEach(Tweak::disable);
		enabledTweaks.clear();
		categoryToTweaks.clear();
	}

	/**
	 * Gets all tweak IDs, including disabled tweaks.
	 *
	 * @return the list of all tweaks
	 */
	@Nonnull
	public List<String> getAllTweaks() {
		return allTweakIds;
	}

	/**
	 * Gets all enabled tweaks.
	 *
	 * @return the list of enabled tweaks
	 */
	@Nonnull
	public List<Tweak> getTweaks() {
		return enabledTweaks;
	}

	/**
	 * Gets all enabled tweaks for the specified player.
	 *
	 * @param player the player
	 * @return the list of enabled tweaks for the player
	 */
	@Nonnull
	public List<Tweak> getTweaks(@Nonnull Player player) {
		return enabledTweaks.stream().filter(tweak -> tweak.hasPermission(player)).toList();
	}

	/**
	 * Gets all enabled tweaks for the specified player and category.
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
	 * Gets the number of enabled tweaks.
	 *
	 * @return the count of enabled tweaks
	 */
	public int getTweakCount() {
		return tweakCount;
	}

	/**
	 * Gets the number of enabled tweaks for the specific player.
	 *
	 * @return the count of enabled tweaks for the player
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
