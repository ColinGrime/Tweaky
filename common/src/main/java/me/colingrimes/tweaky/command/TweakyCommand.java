package me.colingrimes.tweaky.command;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakMenu;
import me.colingrimes.tweaky.tweak.manager.TweakQuery;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TweakyCommand implements CommandExecutor, TabCompleter {

	private final Tweaky plugin;

	public TweakyCommand(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!sender.hasPermission("tweaky.admin")) {
			plugin.getMessages().PLAYER_NO_PERMISSION.send(sender);
			return true;
		}

		// Lists all tweaks.
		if (args.length >= 1 && args[0].equalsIgnoreCase("list")) {
			listTweaks(sender);
			return true;
		}

		// Toggle individual tweaks.
		if (args.length >= 1 && args[0].equalsIgnoreCase("toggle")) {
			if (args.length >= 2) {
				plugin.getTweakManager().toggle(args[1], sender);
			} else if (sender instanceof Player player) {
				TweakMenu.admin(plugin, player).open();
			} else {
				plugin.getMessages().ADMIN_USAGE_TWEAKY_TOGGLE.send(sender);
			}
			return true;
		}

		// Reload configurations.
		if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
			plugin.getConfigManager().reload();
			int amount = plugin.getTweakManager().register();
			plugin.getMessages().ADMIN_SUCCESS_RELOADED.replace("{amount}", amount).send(sender);
			return true;
		}

		plugin.getMessages().ADMIN_USAGE_TWEAKY.send(sender);
		return true;
	}

	@Nullable
	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!sender.hasPermission("tweaky.admin") || args.length == 0) {
			return null;
		} else if (args.length == 1) {
			return List.of("list", "toggle", "reload");
		}

		if (args[0].equalsIgnoreCase("toggle")) {
			return plugin.getTweakManager().getAllTweakIds().stream().filter(tweak -> tweak.contains(args[1].toLowerCase())).toList();
		}

		return null;
	}

	private void listTweaks(@Nonnull CommandSender sender) {
		List<String> tweaks = new ArrayList<>();
		for (String id : plugin.getTweakManager().getAllTweakIds()) {
			String defPath = "tweaks." + id;
			String nestedPath = defPath + ".toggle";
			if (plugin.getConfig().getBoolean(defPath, false) || plugin.getConfig().getBoolean(nestedPath, false)) {
				tweaks.add("&a" + id);
			} else {
				tweaks.add("&c" + id);
			}
		}

		plugin.getMessages().ADMIN_GENERAL_LIST
				.replace("{enabled}", plugin.getTweakManager().getTweakCount(TweakQuery.enabled()))
				.replace("{amount}", plugin.getTweakManager().getAllTweakIds().size())
				.replace("{tweaks}", String.join("&f, ", tweaks))
				.send(sender);
	}
}
