package me.colingrimes.tweaky.command;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class TweaksCommand implements CommandExecutor {

	private final Tweaky plugin;

	public TweaksCommand(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (sender instanceof Player player) {
			new TweakMenu(plugin, player).open();
		}
		return true;
	}
}
