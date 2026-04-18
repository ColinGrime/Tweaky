package me.colingrimes.tweaky.message;

import me.colingrimes.tweaky.util.misc.Types;
import me.colingrimes.tweaky.util.text.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class Message {

    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder().character('&').extractUrls().build();
    private static MessageService messageService;
    private final List<Component> components;

    /**
     * Initializes the {@link Message} class with a platform-specific message service.
     *
     * @param service the message service
     */
    public static void init(@Nonnull MessageService service) {
        messageService = service;
    }

    /**
     * Creates an empty message.
     *
     * @return an empty message
     */
    public static Message empty() {
        return new Message();
    }

    /**
     * Creates a message with the specified content.
     *
     * @param content the content of the message
     * @return the message
     * @throws IllegalArgumentException if the content is not of a supported type
     */
    public static <T> Message of(@Nonnull T content) {
        if (content instanceof String string) {
            return new Message(LEGACY.deserialize(string));
        } else if (Types.asStringList(content).isPresent()) {
            return new Message(Types.asStringList(content).get().stream().map(c -> (Component) LEGACY.deserialize(c)).toList());
        } else if (content instanceof Component component) {
            return new Message(component);
        } else if (Types.asComponentList(content).isPresent()) {
            return new Message(Types.asComponentList(content).get());
        } else if (content instanceof Number number) {
            return new Message(LEGACY.deserialize(String.valueOf(number)));
        } else {
            throw new IllegalArgumentException("Unsupported message content type: " + content.getClass().getName());
        }
    }

    protected Message() {
        this(List.of(Component.empty()));
    }

    protected Message(@Nonnull Component component) {
        this(List.of(component));
    }

    protected Message(@Nonnull List<Component> components) {
        this.components = components;
    }

    @Nonnull
    private static MessageService service() {
        if (messageService == null) {
            throw new IllegalStateException("MessageService not initialized.");
        }
        return messageService;
    }

    /**
     * Gets the component of the message.
     *
     * @return the component of the message
     */
    @Nonnull
    public Component getComponent() {
        return Component.join(JoinConfiguration.newlines(), components);
    }

    /**
     * Gets the components of the message.
     *
     * @return the components of the message
     */
    @Nonnull
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Converts the message content to a colored text format.
     *
     * @return the text representation of the message content
     */
    @Nonnull
    public String toText() {
        return Text.color(LEGACY.serialize(getComponent()));
    }

    /**
     * Converts the message content to a list of colored text.
     *
     * @return the list of text of the message content
     */
    @Nonnull
    public List<String> toTextList() {
        return List.of(toText().split("\n"));
    }

    /**
     * Sends the message to the {@link CommandSender} recipient.
     *
     * @param recipient the recipient
     */
    public void send(@Nonnull CommandSender recipient) {
        service().send(recipient, this);
    }

    /**
     * Sends the title to the {@link CommandSender} recipient.
     *
     * @param recipient the recipient
     * @param fadeInTicks the ticks to fade in
     * @param stayTicks the ticks to stay
     * @param fadeOutTicks the ticks to fade out
     */
    public void title(@Nonnull CommandSender recipient, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        service().title(recipient, this, fadeInTicks, stayTicks, fadeOutTicks);
    }

    /**
     * Sends the subtitle to the {@link CommandSender} recipient.
     *
     * @param recipient the recipient
     * @param fadeInTicks the ticks to fade in
     * @param stayTicks the ticks to stay
     * @param fadeOutTicks the ticks to fade out
     */
    public void subtitle(@Nonnull CommandSender recipient, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        service().subtitle(recipient, this, fadeInTicks, stayTicks, fadeOutTicks);
    }

    /**
     * Sets the name of the item to the message content.
     *
     * @param item the item
     */
    public void setName(@Nonnull ItemStack item) {
        service().setName(item, this);
    }

    /**
     * Sets the lore of the item to the message content.
     *
     * @param item the item
     */
    public void setLore(@Nonnull ItemStack item) {
        service().setLore(item, this);
    }


    /**
     * Applies {@link Placeholders} to the message and returns the result.
     *
     * @param placeholders the placeholders to apply to the message
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    public Message replace(@Nonnull Placeholders placeholders) {
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
    public Message replace(@Nonnull String key1, @Nonnull Object value1) {
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
    public Message replace(@Nonnull String key1, @Nonnull Object value1,
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
    public Message replace(@Nonnull String key1, @Nonnull Object value1,
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
    public Message replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3,
                               @Nonnull String key4, @Nonnull Object value4) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3).add(key4, value4));
    }
}