package me.colingrimes.tweaky.config.option;

import me.colingrimes.tweaky.config.manager.ConfigurationProvider;
import me.colingrimes.tweaky.message.Message;
import me.colingrimes.tweaky.util.Types;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MessageOption<T> implements Option<T>, Message<List<String>> {

	private final Function<ConfigurationProvider, T> function;
	private T value;
	private List<String> messages;

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

		// Convert string message.
		if (value instanceof String) {
			messages = List.of(Text.color((String) value));
		}

		// Convert list of string messages.
		if (Types.asStringList(value).isPresent()) {
			messages = Types.asStringList(value).get().stream().map(Text::color).collect(Collectors.toList());
		}
	}

	@Nonnull
	@Override
	public List<String> getContent() {
		return messages;
	}

	@Override
	public void send(@Nonnull CommandSender recipient) {
		messages.forEach(recipient::sendMessage);
	}
}