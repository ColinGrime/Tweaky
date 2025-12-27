package me.colingrimes.tweaky.message;

import me.colingrimes.tweaky.message.implementation.ListMessage;
import me.colingrimes.tweaky.message.implementation.TextMessage;
import me.colingrimes.tweaky.util.Types;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A versatile and generic message interface that can be used in various contexts.
 *
 * @param <T> the type of the message content
 */
public interface Message<T> {

    /**
     * Constructs a new {@link Message} object. Must be either:
     * <ul>
     *     <li>A string for {@link TextMessage}'s.</li>
     *     <li>A list of strings for {@link ListMessage}'s.</li>
     * </ul>
     *
     * @param content the content of the message
     * @return the new message if supported
     * @throws IllegalArgumentException if the content is not of a supported type
     */
    @SuppressWarnings("unchecked")
    static <T> Message<T> of(@Nonnull T content) {
        if (content instanceof String) {
            return (Message<T>) new TextMessage((String) content);
        } else if (Types.asStringList(content).isPresent()) {
            return (Message<T>) new ListMessage(Types.asStringList(content).get());
        } else {
            throw new IllegalArgumentException("Unsupported message content type: " + content.getClass().getName());
        }
    }

    /**
     * Gets the content of the message.
     *
     * @return the content of the message
     */
    @Nonnull
    T getContent();

    /**
     * Converts the message content to a text format.
     * Supports color codes.
     *
     * @return the text representation of the message content
     */
    @Nonnull
    default String toText() {
        T content = getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (Types.asStringList(content).isPresent()) {
            return String.join("\n", Types.asStringList(content).get());
        } else {
            return "";
        }
    }

    /**
     * Converts the message content to a list of text.
     * Supports color codes.
     *
     * @return the list of text of the message content
     */
    @Nonnull
    default List<String> toTextList() {
        return List.of(toText().split("\n"));
    }

    /**
     * Sends the message to a {@link CommandSender} recipient.
     *
     * @param recipient the command sender recipient of the message
     */
    void send(@Nonnull CommandSender recipient);

    /**
     * Sends the message to a {@link Player} recipient.
     *
     * @param recipient the player recipient of the message
     */
    default void send(@Nonnull Player recipient) {
        send((CommandSender) recipient);
    }

    /**
     * Applies {@link Placeholders} to the message and returns the result.
     *
     * @param placeholders the placeholders to apply to the message
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull Placeholders placeholders) {
        return placeholders.apply(this);
    }


    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the placeholder key
     * @param value1 the placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1) {
        return replace(Placeholders.of(key1, value1));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the first placeholder key
     * @param value1 the first placeholder value
     * @param key2   the second placeholder key
     * @param value2 the second placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2) {
        return replace(Placeholders.of(key1, value1).add(key2, value2));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the first placeholder key
     * @param value1 the first placeholder value
     * @param key2   the second placeholder key
     * @param value2 the second placeholder value
     * @param key3   the third placeholder key
     * @param value3 the third placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the first placeholder key
     * @param value1 the first placeholder value
     * @param key2   the second placeholder key
     * @param value2 the second placeholder value
     * @param key3   the third placeholder key
     * @param value3 the third placeholder value
     * @param key4   the fourth placeholder key
     * @param value4 the fourth placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3,
                               @Nonnull String key4, @Nonnull Object value4) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3).add(key4, value4));
    }
}