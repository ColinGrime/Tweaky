package me.colingrimes.tweaky.config.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.message.Message;

import javax.annotation.Nonnull;

public class Messages extends Configuration {

	public final Message<?> PLAYER_NO_PERMISSION = message("player.no-permission", "&7[&eTweaky&7] &cYou lack the required permission for this command.");
	public final Message<?> ADMIN_GENERAL_LIST = message("admin.general.list", "&6Loaded Tweaks &7(&f{amount}&7): {tweaks}");
	public final Message<?> ADMIN_SUCCESS_TOGGLE_ON = message("admin.success.toggle-on", "&7[&eTweaky&7] &aYou have toggled on the &e{tweak} &atweak.");
	public final Message<?> ADMIN_SUCCESS_TOGGLE_OFF = message("admin.success.toggle-off", "&7[&eTweaky&7] &cYou have toggled off the &e{tweak} &ctweak.");
	public final Message<?> ADMIN_SUCCESS_RELOADED = message("admin.success.reloaded", "&7[&eTweaky&7] &aReloaded configuration files. Registered &e{amount} &atweaks.");
	public final Message<?> ADMIN_FAILURE_INVALID_TWEAK = message("admin.failure.invalid-tweak", "&7[&eTweaky&7] &cThere is no tweak with the name &e{tweak}&c.");
	public final Message<?> ADMIN_USAGE_TWEAKY = message("admin.usage.tweaky",
			"&8&l&m━━━━━━━━━━━━━&7 &e&lTweaky &cAdmin Commands &8&l&m━━━━━━━━━━━━━",
			"&7- &c/tweaky list &e: &7Lists all enabled/disabled tweaks.",
			"&7- &c/tweaky toggle <tweak_id> &e: &7Toggles the specific tweak.",
			"&7- &c/tweaky reload &e: &7Reloads config files.",
			"&8&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
	);
	public final Message<?> ADMIN_USAGE_TWEAKY_TOGGLE = message("admin.usage.tweaky-toggle",
			"&eUsage: &c/tweaky toggle <tweak_id>",
			"&c► &7Toggles the specific tweak."
	);

	public Messages(@Nonnull Tweaky plugin) {
		super(plugin, "messages.yml");
	}
}
