package me.colingrimes.tweaky.message;

import me.colingrimes.tweaky.message.implementation.ListMessage;
import me.colingrimes.tweaky.message.implementation.TextMessage;
import me.colingrimes.tweaky.util.Types;
import me.colingrimes.tweaky.util.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class designed for managing and applying multiple placeholders
 * within different types of input, including strings, lists of strings, or components.
 * <p>
 * In addition, this class also supports the use of PlaceholderAPI placeholders.
 */
public class Placeholders {

	private final Map<String, Message<?>> placeholders = new HashMap<>();

	/**
	 * Creates a new placeholders object.
	 *
	 * @return the placeholders object
	 */
	@Nonnull
	public static Placeholders create() {
		return new Placeholders();
	}

	/**
	 * Creates a new placeholders object.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	@Nonnull
	public static <T> Placeholders of(@Nonnull String placeholder, @Nonnull T value) {
		return new Placeholders().add(placeholder, value);
	}

	/**
	 * Creates a new placeholders object.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 */
	@Nonnull
	public static Placeholders of(@Nonnull String placeholder, @Nonnull Message<?> value) {
		return new Placeholders().add(placeholder, value);
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	@Nonnull
	public <T> Placeholders add(@Nonnull String placeholder, @Nonnull T value) {
		placeholders.put(placeholder, Message.of(String.valueOf(value)));
		return this;
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 *
	 * @param placeholder the placeholder you want to add
	 * @param value       the value you want to replace the placeholder with
	 * @return the placeholders object
	 */
	@Nonnull
	public Placeholders add(@Nonnull String placeholder, @Nonnull Message<?> value) {
		placeholders.put(placeholder, value);
		return this;
	}

	/**
	 * Applies all placeholders in a string.
	 *
	 * @param str the string to apply placeholders to
	 * @return the new string with applied placeholders
	 */
	@Nonnull
	public TextMessage apply(@Nullable String str) {
		if (str == null) {
			return new TextMessage();
		}
		for (Map.Entry<String, Message<?>> replacement : placeholders.entrySet()) {
			str = str.replace(replacement.getKey(), replacement.getValue().toText());
		}
		return new TextMessage(Text.color(str));
	}

	/**
	 * Applies all placeholders in all strings.
	 *
	 * @param strList the list of strings to apply placeholders to
	 * @return the new list of strings with applied placeholders
	 */
	@Nonnull
	public ListMessage apply(@Nullable List<String> strList) {
		if (strList == null || strList.isEmpty()) {
			return new ListMessage();
		}

		List<String> converted = new ArrayList<>();
		strList.forEach(str -> converted.add(apply(str).getContent()));
		return new ListMessage(converted);
	}

	/**
	 * Applies all placeholders in a message.
	 *
	 * @param message the message to apply placeholders to
	 * @return the new message with applied placeholders
	 */
	@Nonnull
	public <T> Message<?> apply(@Nonnull Message<T> message) {
		if (message.getContent() instanceof String content) {
			return apply(content);
		} else if (Types.asStringList(message.getContent()).isPresent()) {
			return apply(Types.asStringList(message.getContent()).get());
		} else {
			throw new IllegalArgumentException("Invalid message content type: " + message.getContent().getClass().getName());
		}
	}
}