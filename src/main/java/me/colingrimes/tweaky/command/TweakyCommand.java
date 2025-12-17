package me.colingrimes.tweaky.command;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class TweakyCommand implements CommandExecutor {

	private final Tweaky plugin;

	public TweakyCommand(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!sender.hasPermission("tweaky.admin")) {
			plugin.getSettings().NO_PERMISSION.send(sender);
		} else if (args.length == 1 && args[0].equalsIgnoreCase("resetrecipes")) {
			Players.forEach(p -> p.undiscoverRecipes(plugin.getAllRecipes()));
			plugin.getSettings().RESET_RECIPES.send(sender);
		} else {
			plugin.getSettings().reload();
			int amount = plugin.registerTweaks();
			sender.sendMessage(Text.color(plugin.getSettings().RELOADED.get().replace("{amount}", String.valueOf(amount))));
		}
		return true;
	}
}
