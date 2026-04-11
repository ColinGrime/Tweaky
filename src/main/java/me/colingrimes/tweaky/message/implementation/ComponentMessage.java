package me.colingrimes.tweaky.message.implementation;

import me.colingrimes.tweaky.message.Message;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

/**
 * Represents a message that is sent as a {@link Component}.
 */
public class ComponentMessage implements Message<Component> {

	private final Component content;

	public ComponentMessage() {
		this(Component.empty());
	}

	public ComponentMessage(@Nonnull Component content) {
		this.content = content;
	}

	@Nonnull
	@Override
	public Component getContent() {
		return content;
	}

	@Override
	public void send(@Nonnull CommandSender recipient) {
		recipient.sendMessage(content);
	}
}