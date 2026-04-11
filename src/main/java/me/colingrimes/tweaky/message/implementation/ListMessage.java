package me.colingrimes.tweaky.message.implementation;

import me.colingrimes.tweaky.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of messages that is sent as plain text.
 * Text is automatically colored when sent.
 */
public class ListMessage implements Message<List<String>> {

    private final List<String> content;

    public ListMessage() {
        this(new ArrayList<>());
    }

    public ListMessage(@Nonnull List<String> content) {
        this.content = content;
    }

    @Nonnull
    @Override
    public List<String> getContent() {
        return content;
    }

    @Override
    public void send(@Nonnull CommandSender recipient) {
        toTextList().forEach(recipient::sendMessage);
    }
}