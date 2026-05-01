package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.tweak.Tweak;

/**
 * Represents a {@link Tweak} that has at least one configuration option.
 */
public interface ConfigurableTweak extends Tweak {

	/**
	 * Gets the number of configuration options for the tweak.
	 *
	 * @return the option count
	 */
	default int getOptionCount() {
		return 1;
	}
}
