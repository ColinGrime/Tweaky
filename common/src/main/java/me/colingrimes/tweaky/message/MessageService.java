package me.colingrimes.tweaky.message;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Provides platform-specific handling for rendering and applying {@link Message} objects.
 */
public interface MessageService {

	/**
	 * Sends the message to the {@link CommandSender} recipient.
	 *
	 * @param recipient the recipient
	 * @param message the message to send
	 */
	void send(@Nonnull CommandSender recipient, @Nonnull Message message);

	/**
	 * Sends the title to the {@link CommandSender} recipient.
	 *
	 * @param recipient the recipient
	 * @param message the message to send
	 * @param fadeInTicks the ticks to fade in
	 * @param stayTicks the ticks to stay
	 * @param fadeOutTicks the ticks to fade out
	 */
	void title(@Nonnull CommandSender recipient, @Nonnull Message message, int fadeInTicks, int stayTicks, int fadeOutTicks);

	/**
	 * Sends the subtitle to the {@link CommandSender} recipient.
	 *
	 * @param recipient the recipient
	 * @param message the message to send
	 * @param fadeInTicks the ticks to fade in
	 * @param stayTicks the ticks to stay
	 * @param fadeOutTicks the ticks to fade out
	 */
	void subtitle(@Nonnull CommandSender recipient, @Nonnull Message message, int fadeInTicks, int stayTicks, int fadeOutTicks);

	/**
	 * Gets the name of the item.
	 *
	 * @param item the item
	 * @return the name of the item
	 */
	@Nonnull
	Message getName(@Nonnull ItemStack item);

	/**
	 * Sets the name of the item to the message content.
	 *
	 * @param item the item
	 * @param message the message to use
	 */
	void setName(@Nonnull ItemStack item, @Nonnull Message message);

	/**
	 * Sets the lore of the item to the message content.
	 *
	 * @param item the item
	 * @param message the message to use
	 */
	void setLore(@Nonnull ItemStack item, @Nonnull Message message);
}
