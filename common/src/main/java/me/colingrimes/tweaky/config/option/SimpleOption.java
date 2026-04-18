package me.colingrimes.tweaky.config.option;

import me.colingrimes.tweaky.config.manager.ConfigurationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class SimpleOption<T> implements Option<T> {

	private final Function<ConfigurationProvider, T> function;
	private T value;

	public SimpleOption(@Nonnull Function<ConfigurationProvider, T> function) {
		this.function = function;
	}

	/**
	 * The value of the option.
	 *
	 * @return the value
	 */
	@Nonnull
	@Override
	public T get() {
		return value;
	}

	/**
	 * Reloads the value of the option.
	 *
	 * @param adapter the configuration adapter
	 */
	@Override
	public void reload(@Nullable ConfigurationProvider adapter) {
		this.value = function.apply(adapter);
	}
}
