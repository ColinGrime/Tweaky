package me.colingrimes.tweaky.message.implementation;

import me.colingrimes.tweaky.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * Represents a message that is sent as plain text.
 * Text is automatically colored when sent.
 */
public class TextMessage implements Message<String> {

    private final String content;

    public TextMessage() {
        this("");
    }

    public TextMessage(@Nonnull String content) {
        this.content = content;
    }

    @Nonnull
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void send(@Nonnull CommandSender recipient) {
        recipient.sendMessage(toText());
    }
}