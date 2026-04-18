package me.colingrimes.tweaky.config.option;

import me.colingrimes.tweaky.config.manager.ConfigurationProvider;
import me.colingrimes.tweaky.message.Message;
import net.kyori.adventure.text.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class MessageOption<T> extends Message implements Option<T> {

	private final Function<ConfigurationProvider, T> function;
	private T value;
	private Message message;

	public MessageOption(@Nonnull Function<ConfigurationProvider, T> function) {
		this.function = function;
	}

	@Nonnull
	@Override
	public T get() {
		return value;
	}

	@Override
	public void reload(@Nullable ConfigurationProvider adapter) {
		value = function.apply(adapter);
		message = Message.of(value);
	}

	@Nonnull
	@Override
	public Component getComponent() {
		return message.getComponent();
	}

	@Nonnull
	@Override
	public List<Component> getComponents() {
		return message.getComponents();
	}
}
