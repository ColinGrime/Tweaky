package me.colingrimes.tweaky.tweak.manager;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.implementation.hidden.BreedingIndicatorTweak_RoseStackerFix;
import me.colingrimes.tweaky.tweak.implementation.misc.SnowballTweak;
import me.colingrimes.tweaky.tweak.implementation.text.BreedingIndicatorTweak;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.io.Introspector;
import me.colingrimes.tweaky.util.io.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.*;

public class TweakManager {

	private final Tweaky plugin;
	private final List<String> allTweakIds = new ArrayList<>();
	private final Set<Tweak> allTweaks = new HashSet<>();
	private final Set<Tweak> enabledTweaks = new HashSet<>();
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
		// Clean up previously enabled tweaks that are now disabled.
		Iterator<Tweak> iterator = enabledTweaks.iterator();
		while (iterator.hasNext()) {
			Tweak tweak = iterator.next();
			if (!tweak.isEnabled()) {
				tweak.disable();
				iterator.remove();
			}
		}

		// Register newly enabled tweaks.
		for (Tweak tweak : allTweaks.stream().filter(Tweak::isEnabled).toList()) {
			if (!enabledTweaks.contains(tweak)) {
				tweak.enable();
				enabledTweaks.add(tweak);
			}
		}

		// Tweak count.
		int tweakCount = getTweakCount(TweakQuery.enabled());
		Logger.log("Registered " + tweakCount + " tweaks.");
		return tweakCount;
	}

	/**
	 * Handles the proper shutdown of tweaks.
	 */
	public void shutdown() {
		enabledTweaks.forEach(Tweak::disable);
		enabledTweaks.clear();
	}

	/**
	 * Attempts to toggle the tweak with the specified ID.
	 *
	 * @param id the tweak ID to toggle
	 * @param sender the sender who requested the toggle
	 */
	public void toggle(@Nonnull String id, @Nonnull CommandSender sender) {
		String defPath = "tweaks." + id.toLowerCase().replace("_", "-");
		String nestedPath = defPath + ".toggle";

		boolean value;
		if (plugin.getConfig().contains(nestedPath)) {
			value = !plugin.getConfig().getBoolean(nestedPath);
			plugin.getConfig().set(nestedPath, value);
		} else if (plugin.getConfig().contains(defPath)) {
			value = !plugin.getConfig().getBoolean(defPath);
			plugin.getConfig().set(defPath, value);
		} else {
			plugin.getMessages().ADMIN_FAILURE_INVALID_TWEAK.replace("{tweak}", id).send(sender);
			return;
		}

		plugin.saveConfig();
		if (value) {
			plugin.getMessages().ADMIN_SUCCESS_TOGGLE_ON.replace("{tweak}", id).send(sender);
		} else {
			plugin.getMessages().ADMIN_SUCCESS_TOGGLE_OFF.replace("{tweak}", id).send(sender);
		}

		// Reloads the changed config and re-register tweaks.
		plugin.getConfigManager().reload();
		plugin.getTweakManager().register();
	}

	/**
	 * Gets all tweak IDs in alphabetical order.
	 *
	 * @return the list of tweaks in alphabetical order
	 */
	@Nonnull
	public List<String> getAllTweakIds() {
		return List.copyOf(allTweakIds);
	}

	/**
	 * Gets the tweaks according to the query.
	 *
	 * @param query the tweak query
	 * @return the list of tweaks
	 */
	@Nonnull
	public List<Tweak> getTweaks(@Nonnull TweakQuery query) {
		Set<Tweak> tweaks = query.includeAll() ? allTweaks : enabledTweaks;
		return tweaks.stream()
				.filter(t -> query.getPlayer() == null || t.hasPermission(query.getPlayer()))
				.filter(t -> query.getCategory() == null || t.getProperties().getCategory() == query.getCategory())
				.toList();
	}

	/**
	 * Gets the number of tweaks according to the query.
	 *
	 * @param query the tweak query
	 * @return the count of tweaks
	 */
	public int getTweakCount(@Nonnull TweakQuery query) {
		return Tweak.count(getTweaks(query), query);
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
