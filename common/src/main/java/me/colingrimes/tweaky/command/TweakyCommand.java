package me.colingrimes.tweaky.command;

import me.colingrimes.tweaky.Tweaky;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
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
			if (args.length == 1) {
				plugin.getMessages().ADMIN_USAGE_TWEAKY_TOGGLE.send(sender);
			} else {
				toggleTweaks(sender, args);
				plugin.getConfigManager().reload();
				plugin.getTweakManager().register();
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
			return plugin.getTweakManager().getAvailableTweaks().stream().filter(tweak -> tweak.contains(args[1].toLowerCase())).toList();
		}

		return null;
	}

	private void listTweaks(@Nonnull CommandSender sender) {
		List<String> tweaks = new ArrayList<>();
		for (String id : plugin.getTweakManager().getAvailableTweaks()) {
			String defPath = "tweaks." + id;
			String nestedPath = defPath + ".toggle";
			if (plugin.getConfig().getBoolean(defPath, false) || plugin.getConfig().getBoolean(nestedPath, false)) {
				tweaks.add("&a" + id);
			} else {
				tweaks.add("&c" + id);
			}
		}

		plugin.getMessages().ADMIN_GENERAL_LIST
				.replace("{enabled}", plugin.getTweakManager().getTweakCount())
				.replace("{amount}", plugin.getTweakManager().getAvailableTweaks().size())
				.replace("{tweaks}", String.join("&f, ", tweaks))
				.send(sender);
	}

	private void toggleTweaks(@Nonnull CommandSender sender, @Nonnull String[] args) {
		String defPath = "tweaks." + args[1].toLowerCase().replace("_", "-");
		String nestedPath = defPath + ".toggle";

		boolean value;
		if (plugin.getConfig().contains(nestedPath)) {
			value = !plugin.getConfig().getBoolean(nestedPath);
			plugin.getConfig().set(nestedPath, value);
		} else if (plugin.getConfig().contains(defPath)) {
			value = !plugin.getConfig().getBoolean(defPath);
			plugin.getConfig().set(defPath, value);
		} else {
			plugin.getMessages().ADMIN_FAILURE_INVALID_TWEAK.replace("{tweak}", args[1]).send(sender);
			return;
		}

		plugin.saveConfig();
		if (value) {
			plugin.getMessages().ADMIN_SUCCESS_TOGGLE_ON.replace("{tweak}", args[1]).send(sender);
		} else {
			plugin.getMessages().ADMIN_SUCCESS_TOGGLE_OFF.replace("{tweak}", args[1]).send(sender);
		}
	}
}
