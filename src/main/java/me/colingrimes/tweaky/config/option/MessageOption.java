package me.colingrimes.tweaky.config.option;

import me.colingrimes.tweaky.config.manager.ConfigurationProvider;
import me.colingrimes.tweaky.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class MessageOption<T> implements Option<T>, Message<Component> {

	private final Function<ConfigurationProvider, T> function;
	private T value;
	private Component component;

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
		component = Message.of(value).toComponent();
	}

	@Nonnull
	@Override
	public Component getContent() {
		return component;
	}

	@Override
	public void send(@Nonnull CommandSender recipient) {
		recipient.sendMessage(component);
	}
}