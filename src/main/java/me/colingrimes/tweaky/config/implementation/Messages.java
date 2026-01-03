package me.colingrimes.tweaky.config.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.message.Message;

import javax.annotation.Nonnull;
import java.util.List;

public class Messages extends Configuration {

	public final Message<?> TWEAK_ENDER_CHEST_PROTECTED = message("tweaks.ender-chest-protected", "&2[Protected] &aYour Pickaxe needs &2Silk Touch &ato break this.");
	public final Message<?> TWEAK_COORDINATES_DAY       = message("tweaks.coordinates-day", "&a{x} &7| &a{y} &7| &a{z}");
	public final Message<?> TWEAK_COORDINATES_NIGHT     = message("tweaks.coordinates-night", "&c{x} &7| &c{y} &7| &c{z}");
	public final Message<?> TWEAK_DEATH_MESSAGE         = message("tweaks.death-message", "&7You died at (&a{x}&7, &a{y}&7, &a{z}&7) in the {world} &7with &a{levels} &7levels.");
	public final Message<?> TWEAK_FILTER_ADD            = message("tweaks.filter-add", "&2[Filter] &aAdded &e{item} &ato your drop filter.");
	public final Message<?> TWEAK_FILTER_REMOVE         = message("tweaks.filter-remove", "&4[Filter] &cRemoved &e{item} &cfrom your drop filter.");
	public final Message<?> TWEAK_FILTER_EXISTS         = message("tweaks.filter-exists", "&4[Filter] &cThat item is already in your drop filter.");
	public final Message<?> TWEAK_FILTER_FULL           = message("tweaks.filter-full", "&4[Filter] &cSorry, your drop filter is full.");
	public final Message<?> TWEAK_FILTER_ON             = message("tweaks.filter-on", "&2[Filter] &aYou have toggled on the drop filter.");
	public final Message<?> TWEAK_FILTER_OFF            = message("tweaks.filter-off", "&4[Filter] &cYou have toggled off the drop filter.");
	public final Message<?> TWEAK_FORTUNE_SUFFIX        = message("tweaks.fortune-suffix", "&7(&aFortune&7)");
	public final Message<?> TWEAK_SILK_TOUCH_SUFFIX     = message("tweaks.silk-touch-suffix", "&7(&aSilk Touch&7)");
	public final Message<?> TWEAK_HORSE_STATISTICS      = message("tweaks.horse-statistics", List.of());
	public final Message<?> PLAYER_NO_PERMISSION        = message("player.no-permission", "&7[&eTweaky&7] &cYou lack the required permission for this command.");
	public final Message<?> ADMIN_GENERAL_LIST          = message("admin.general.list", "&6Loaded Tweaks &7(&f{amount}&7): {tweaks}");
	public final Message<?> ADMIN_GENERAL_NEW_UPGRADE   = message("admin.general.new-update", List.of());
	public final Message<?> ADMIN_SUCCESS_TOGGLE_ON     = message("admin.success.toggle-on", "&7[&eTweaky&7] &aYou have toggled on the &e{tweak} &atweak.");
	public final Message<?> ADMIN_SUCCESS_TOGGLE_OFF    = message("admin.success.toggle-off", "&7[&eTweaky&7] &cYou have toggled off the &e{tweak} &ctweak.");
	public final Message<?> ADMIN_SUCCESS_RELOADED      = message("admin.success.reloaded", "&7[&eTweaky&7] &aReloaded configuration files. Registered &e{amount} &atweaks.");
	public final Message<?> ADMIN_FAILURE_INVALID_TWEAK = message("admin.failure.invalid-tweak", "&7[&eTweaky&7] &cThere is no tweak with the name &e{tweak}&c.");
	public final Message<?> ADMIN_USAGE_TWEAKY          = message("admin.usage.tweaky",
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
