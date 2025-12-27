package me.colingrimes.tweaky.config.option;

import me.colingrimes.tweaky.config.manager.ConfigurationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Represents a configuration option for a configuration file.
 *
 * @param <T> type of the option
 */
public interface Option<T> {

	@Nonnull
	static <T> Option<T> of(@Nonnull Function<ConfigurationProvider, T> function) {
		return new SimpleOption<>(function);
	}

	/**
	 * The value of the option.
	 *
	 * @return the value
	 */
	@Nonnull
	T get();

	/**
	 * Reloads the option from the given configuration provider.
	 *
	 * @param provider the provider
	 */
	void reload(@Nullable ConfigurationProvider provider);
}