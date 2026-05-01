package me.colingrimes.tweaky.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;

public class PaperMessageService implements MessageService {

	@Override
	public void send(@Nonnull CommandSender recipient, @Nonnull Message message) {
		recipient.sendMessage(message.getComponent());
	}

	@Override
	public void title(@Nonnull CommandSender recipient, @Nonnull Message message, int fadeInTicks, int stayTicks, int fadeOutTicks) {
		List<Component> components = message.getComponents();
		if (components.size() == 1) {
			recipient.showTitle(Title.title(components.getFirst(), Component.empty(), fadeInTicks, stayTicks, fadeOutTicks));
		} else if (components.size() > 1) {
			recipient.showTitle(Title.title(components.getFirst(), components.get(1), fadeInTicks, stayTicks, fadeOutTicks));
		}
	}

	@Override
	public void subtitle(@Nonnull CommandSender recipient, @Nonnull Message message, int fadeInTicks, int stayTicks, int fadeOutTicks) {
		recipient.showTitle(Title.title(Component.empty(), message.getComponent(), fadeInTicks, stayTicks, fadeOutTicks));
	}

	@Nonnull
	@Override
	public Message getName(@Nonnull ItemStack item) {
		if (item.getItemMeta() == null || !item.getItemMeta().hasCustomName()) {
			return Message.empty();
		}

		Component component = item.getItemMeta().customName();
		return component != null ? Message.of(component) : Message.empty();
	}

	@Override
	public void setName(@Nonnull ItemStack item, @Nonnull Message message) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.customName(message.getComponent());
			item.setItemMeta(meta);
		}
	}

	@Override
	public void setLore(@Nonnull ItemStack item, @Nonnull Message message) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.lore(message.getComponents());
			item.setItemMeta(meta);
		}
	}
}
