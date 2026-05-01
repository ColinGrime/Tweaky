package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.tweak.Tweak;

/**
 * Represents a {@link Tweak} class that contains more than one tweak.
 */
public interface MultiTweak extends Tweak {

	/**
	 * Gets the number of tweaks that are enabled by the tweak class.
	 *
	 * @return the number of enabled tweaks
	 */
	int getCount();

	/**
	 * Gets the number of tweaks that the tweak class holds.
	 *
	 * @return the number of tweaks
	 */
	int getTotalCount();
}
