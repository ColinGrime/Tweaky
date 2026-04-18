package me.colingrimes.tweaky.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Placeholders {

	private final Map<String, Message> placeholders = new HashMap<>();

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
	public static Placeholders of(@Nonnull String placeholder, @Nonnull Message value) {
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
		placeholders.put(placeholder, Message.of(value));
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
	public Placeholders add(@Nonnull String placeholder, @Nonnull Message value) {
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
	public Message apply(@Nullable String str) {
		if (str != null) {
			return apply(Message.of(str));
		} else {
			return Message.empty();
		}
	}

	/**
	 * Applies all placeholders in all strings.
	 *
	 * @param strList the list of strings to apply placeholders to
	 * @return the new list of strings with applied placeholders
	 */
	@Nonnull
	public Message apply(@Nullable List<String> strList) {
		if (strList != null) {
			return apply(Message.of(strList));
		} else {
			return Message.empty();
		}
	}

	/**
	 * Applies all placeholders in a component.
	 *
	 * @param component the component to apply placeholders to
	 * @return the new component with applied placeholders
	 */
	@Nonnull
	public Message apply(@Nullable Component component) {
		if (component != null) {
			return applyComponents(List.of(component));
		} else {
			return Message.empty();
		}
	}

	/**
	 * Applies all placeholders in all components.
	 *
	 * @param components the list of components to apply placeholders to
	 * @return the new list of components with applied placeholders
	 */
	@Nonnull
	public Message applyComponents(@Nullable List<Component> components) {
		if (components == null || components.isEmpty()) {
			return Message.empty();
		}

		List<Component> newComponents = new ArrayList<>();
		for (Component component : components) {
			for (Map.Entry<String, Message> replacement : placeholders.entrySet()) {
				component = component.replaceText(
						TextReplacementConfig
								.builder()
								.matchLiteral(replacement.getKey())
								.replacement(replacement.getValue().getComponent())
								.build()
				);
			}
			newComponents.add(component);
		}

		return Message.of(newComponents);
	}

	/**
	 * Applies all placeholders in a message.
	 *
	 * @param message the message to apply placeholders to
	 * @return the new message with applied placeholders
	 */
	@Nonnull
	public Message apply(@Nonnull Message message) {
		return applyComponents(message.getComponents());
	}
}