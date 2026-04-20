package me.colingrimes.tweaky.config.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.config.Option;
import me.colingrimes.tweaky.config.option.SimpleOption;
import me.colingrimes.tweaky.menu.tweak.util.TweakItem;
import me.colingrimes.tweaky.message.Message;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.bukkit.Items;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Menus extends Configuration {

	public final Message TWEAK_MENU_NAME       = message("tweak-menu.name", "&8Tweaks (&2{count}&8)");
	public final Message FILTER_MENU_TITLE     = message("filter-menu.title", "&8Drop Filter");
	public final Message FILTER_MENU_ITEM_NAME = message("filter-menu.item-name", "&a{item}");
	public final Message FILTER_MENU_ITEM_LORE = message("filter-menu.item-lore", "&7Click to Remove");

	// Tweak category items.
	public final Option<Map<TweakCategory, Items.Builder>> TWEAK_MENU_CATEGORIES = option("tweak-menu.categories", sec -> {
		Map<TweakCategory, Items.Builder> categories = new HashMap<>();
		for (TweakCategory category : TweakCategory.values()) {
			String name = category.name().toLowerCase();
			if (sec.contains(name)) {
				categories.put(category, Items.create().config(sec.getConfigurationSection(name)));
			}
		}
		return categories;
	});

	// Additional tweak items.
	public final Option<TweakItem> TWEAK_BREAK_ENDER_CHEST_DROP         = getTweak("break-ender-chest.drop");
	public final Option<TweakItem> TWEAK_BREAK_ENDER_CHEST_PROTECTION   = getTweak("break-ender-chest.protection");
	public final Option<TweakItem> TWEAK_LADDER_TELEPORTATION_AUTOMATIC = getTweak("ladder-teleportation.automatic");
	public final Option<TweakItem> TWEAK_LADDER_TELEPORTATION_MANUAL    = getTweak("ladder-teleportation.manual");
	public final Option<TweakItem> TWEAK_LADDER_TELEPORTATION_CLICK     = getTweak("ladder-teleportation.click");
	public final Option<TweakItem> TWEAK_NIGHT_VISION                   = getTweak("night-vision");
	public final Option<TweakItem> TWEAK_HAPPY_GHAST_SPEED              = getTweak("happy-ghast-speed");

	// Special case -- Showing only the active tweaks in the lore.
	public final Option<TweakItem> TWEAK_KEEP_INVENTORY = option("tweak-menu.tweaks.keep-inventory", sec -> {
		if (sec == null) {
			return TweakItem.create("No config: tweak-menu.tweaks.keep-inventory");
		}

		TweakItem item = TweakItem.create();
		Optional.ofNullable(sec.getString("type")).ifPresent(item::material);
		Optional.ofNullable(sec.getString("name")).ifPresent(item::name);
		Optional.ofNullable(sec.getString("usage")).ifPresent(item::usage);

		if (plugin.getSettings().TWEAK_KEEP_INVENTORY_XP.get()) {
			Optional.ofNullable(sec.getString("lore.xp")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_KEEP_INVENTORY_ARMOR.get()) {
			Optional.ofNullable(sec.getString("lore.armor")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_KEEP_INVENTORY_TOOLS.get()) {
			Optional.ofNullable(sec.getString("lore.tools")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_KEEP_INVENTORY_STACKABLES.get() > 0) {
			Optional.ofNullable(sec.getString("lore.stackables")).ifPresent(item::lore);
			item.placeholder("{percent}", plugin.getSettings().TWEAK_KEEP_INVENTORY_STACKABLES.get().intValue());
		}
		if (plugin.getSettings().TWEAK_KEEP_INVENTORY_UNSTACKABLES.get() > 0) {
			Optional.ofNullable(sec.getString("lore.unstackables")).ifPresent(item::lore);
			item.placeholder("{chance}", plugin.getSettings().TWEAK_KEEP_INVENTORY_UNSTACKABLES.get().intValue());
		}

		return item;
	});

	// Special case -- Showing only the active tweaks in the lore.
	public final Option<TweakItem> TWEAK_SNOWBALLS = option("tweak-menu.tweaks.snowballs", sec -> {
		if (sec == null) {
			return TweakItem.create("No config: tweak-menu.tweaks.snowballs");
		}

		TweakItem item = TweakItem.create();
		Optional.ofNullable(sec.getString("type")).ifPresent(item::material);
		Optional.ofNullable(sec.getString("name")).ifPresent(item::name);
		Optional.ofNullable(sec.getString("usage")).ifPresent(item::usage);

		if (plugin.getSettings().TWEAK_SNOWBALLS_DAMAGE.get()) {
			Optional.ofNullable(sec.getString("lore.damage")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_KNOCKBACK.get()) {
			Optional.ofNullable(sec.getString("lore.knockback")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_FORM_SNOW.get()) {
			Optional.ofNullable(sec.getString("lore.form-snow")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_ADD_SNOW_LAYER.get()) {
			Optional.ofNullable(sec.getString("lore.add-snow-layer")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_FORM_ICE.get()) {
			Optional.ofNullable(sec.getString("lore.form-ice")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_BREAK_PLANTS.get()) {
			Optional.ofNullable(sec.getString("lore.break-plants")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_EXTINGUISH_ENTITIES.get()) {
			Optional.ofNullable(sec.getString("lore.extinguish-entities")).ifPresent(item::lore);
		}
		if (plugin.getSettings().TWEAK_SNOWBALLS_EXTINGUISH_FIRE.get()) {
			Optional.ofNullable(sec.getString("lore.extinguish-fire")).ifPresent(item::lore);
		}

		return item;
	});

	public Menus(@Nonnull Tweaky plugin) {
		super(plugin, "menus.yml");
	}

	/**
	 * Gets the {@link TweakItem} option for the specified tweak ID.
	 *
	 * @param id the tweak ID
	 * @return the tweak item option
	 */
	@Nonnull
	public Option<TweakItem> getTweak(@Nonnull String id) {
		String menuPath = "tweak-menu.tweaks." + id.toLowerCase().replace("_", "-");
		Optional<Option<TweakItem>> existingOption = getOption(menuPath);
		if (existingOption.isPresent()) {
			return existingOption.get();
		}

		Option<TweakItem> option = new SimpleOption<>(provider -> {
			if (provider.getSection(menuPath).isEmpty()) {
				return TweakItem.create("No config: " + menuPath);
			}

			TweakItem item = TweakItem.create();
			provider.getString(menuPath + ".type").ifPresent(item::material);
			provider.getString(menuPath + ".name").ifPresent(item::name);
			provider.getStringList(menuPath + ".lore").ifPresent(item::lore);
			provider.getStringList(menuPath + ".usage").ifPresent(item::usage);
			return item;
		});
		return register(menuPath, option);
	}
}
