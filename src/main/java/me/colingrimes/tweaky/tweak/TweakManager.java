package me.colingrimes.tweaky.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.util.Introspector;
import me.colingrimes.tweaky.util.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TweakManager {

	private static final Set<String> PAPER_ONLY_TWEAKS = Set.of("VillagerFollowTweak, FortuneSilkSwapTweak");
	private final Tweaky plugin;
	private final List<Tweak> tweaks;
	private final List<String> availableTweaks;
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
		List<Class<?>> classes = Introspector.getClasses(plugin.getClass().getClassLoader(), plugin.getClass().getPackage().getName() + ".tweak.implementation");
		List<Class<?>> safeClasses = classes.stream().filter(this::paperCheck).toList();
		List<Tweak> tweakClasses = Introspector.instantiateClasses(safeClasses, Tweak.class, plugin);
		tweaks.addAll(tweakClasses.stream().filter(Tweak::isEnabled).toList());
		tweaks.forEach(Tweak::init);

		// Tweak count.
		tweakCount = tweaks.stream().mapToInt(Tweak::getCount).sum();
		Logger.log(plugin, "Registered " + tweakCount + " tweaks.");
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
	 * Checks if the given class is a Paper-only tweak.
	 * If it is, Paper must be enabled for it to pass.
	 *
	 * @param clazz the class
	 * @return true if the tweak passes
	 */
	private boolean paperCheck(@Nonnull Class<?> clazz) {
		if (plugin.isPaper()) {
			return true;
		}

		String[] words = clazz.getName().split("\\.");
		String tweak = words[words.length - 1];
		return !PAPER_ONLY_TWEAKS.contains(tweak);
	}
}
