package me.colingrimes.tweaky.message;

import me.colingrimes.tweaky.TweakyBukkit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.List;

public class BukkitMessageService implements MessageService {

	private final TweakyBukkit plugin;

	public BukkitMessageService(@Nonnull TweakyBukkit plugin) {
		this.plugin = plugin;
	}

	@Override
	public void send(@Nonnull CommandSender recipient, @Nonnull Message message) {
		plugin.getAudiences().sender(recipient).sendMessage(message.getComponent());
	}

	@Override
	public void title(@Nonnull CommandSender recipient, @Nonnull Message message, int fadeInTicks, int stayTicks, int fadeOutTicks) {
		List<Component> components = message.getComponents();
		Title.Times times = Title.Times.times(Ticks.duration(fadeInTicks), Ticks.duration(stayTicks), Ticks.duration(fadeOutTicks));
		if (components.size() == 1) {
			plugin.getAudiences().sender(recipient).showTitle(Title.title(components.getFirst(), Component.empty(), times));
		} else if (components.size() > 1) {
			plugin.getAudiences().sender(recipient).showTitle(Title.title(components.getFirst(), components.get(1), times));
		}
	}

	@Override
	public void subtitle(@Nonnull CommandSender recipient, @Nonnull Message message, int fadeInTicks, int stayTicks, int fadeOutTicks) {
		Title.Times times = Title.Times.times(Ticks.duration(fadeInTicks), Ticks.duration(stayTicks), Ticks.duration(fadeOutTicks));
		plugin.getAudiences().sender(recipient).showTitle(Title.title(Component.empty(), message.getComponent(), times));
	}

	@Override
	public void setName(@Nonnull ItemStack item, @Nonnull Message message) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(message.toText());
			item.setItemMeta(meta);
		}
	}

	@Override
	public void setLore(@Nonnull ItemStack item, @Nonnull Message message) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setLore(message.toTextList());
			item.setItemMeta(meta);
		}
	}
}
