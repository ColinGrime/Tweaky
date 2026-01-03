package me.colingrimes.tweaky.config.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.config.option.Option;
import me.colingrimes.tweaky.config.option.SimpleOption;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.message.Message;

import javax.annotation.Nonnull;
import java.util.Optional;

public class Menus extends Configuration {

	private Tweaky plugin;
	public final Message<?> TWEAK_MENU_NAME = message("tweak-menu.tweaks.name", "&8Tweaks (&2{count}&8)");
	public final Option<TweakItem> TWEAK_ANVIL_COLOR                    = tweak("tweak-menu.tweaks.anvil-color");
	public final Option<TweakItem> TWEAK_ANVIL_REPAIR                   = tweak("tweak-menu.tweaks.anvil-repair");
	public final Option<TweakItem> TWEAK_ARMOR_SWAP                     = tweak("tweak-menu.tweaks.armor-swap");
	public final Option<TweakItem> TWEAK_BEE_CAPTURE                    = tweak("tweak-menu.tweaks.bee-capture");
	public final Option<TweakItem> TWEAK_BREAK_DEEPSLATE                = tweak("tweak-menu.tweaks.break-deepslate");
	public final Option<TweakItem> TWEAK_BREAK_ENDER_CHEST_DROP         = tweak("tweak-menu.tweaks.break-ender-chest.drop");
	public final Option<TweakItem> TWEAK_BREAK_ENDER_CHEST_PROTECTION   = tweak("tweak-menu.tweaks.break-ender-chest.protection");
	public final Option<TweakItem> TWEAK_BREAK_GLASS                    = tweak("tweak-menu.tweaks.break-glass");
	public final Option<TweakItem> TWEAK_BREAK_LEAVES                   = tweak("tweak-menu.tweaks.break-leaves");
	public final Option<TweakItem> TWEAK_BREAK_PLANTS                   = tweak("tweak-menu.tweaks.break-plants");
	public final Option<TweakItem> TWEAK_BREEDING_INDICATOR             = tweak("tweak-menu.tweaks.breeding-indicator");
	public final Option<TweakItem> TWEAK_CAULDRON_CONCRETE              = tweak("tweak-menu.tweaks.cauldron-concrete");
	public final Option<TweakItem> TWEAK_CAULDRON_MUD                   = tweak("tweak-menu.tweaks.cauldron-mud");
	public final Option<TweakItem> TWEAK_COORDINATES                    = tweak("tweak-menu.tweaks.coordinates");
	public final Option<TweakItem> TWEAK_CROPS_BONE_MEAL                = tweak("tweak-menu.tweaks.crops-bone-meal");
	public final Option<TweakItem> TWEAK_CROPS_HARVEST                  = tweak("tweak-menu.tweaks.crops-harvest");
	public final Option<TweakItem> TWEAK_CROPS_PROTECTION               = tweak("tweak-menu.tweaks.crops-protection");
	public final Option<TweakItem> TWEAK_DEATH_NOTIFY                   = tweak("tweak-menu.tweaks.death-notify");
	public final Option<TweakItem> TWEAK_DOORS_DOUBLE                   = tweak("tweak-menu.tweaks.doors-double");
	public final Option<TweakItem> TWEAK_DOORS_IRON                     = tweak("tweak-menu.tweaks.doors-iron");
	public final Option<TweakItem> TWEAK_DROPS_FILTER                    = tweak("tweak-menu.tweaks.drops-filter");
	public final Option<TweakItem> TWEAK_DROPS_MAGNET                   = tweak("tweak-menu.tweaks.drops-magnet");
	public final Option<TweakItem> TWEAK_ENCHANTING_LAPIS               = tweak("tweak-menu.tweaks.enchanting-lapis");
	public final Option<TweakItem> TWEAK_ENTITY_DYE                     = tweak("tweak-menu.tweaks.entity-dye");
	public final Option<TweakItem> TWEAK_ENTITY_EQUIP                   = tweak("tweak-menu.tweaks.entity-equip");
	public final Option<TweakItem> TWEAK_ENTITY_IGNITE                  = tweak("tweak-menu.tweaks.entity-ignite");
	public final Option<TweakItem> TWEAK_ENTITY_SILENCE                 = tweak("tweak-menu.tweaks.entity-silence");
	public final Option<TweakItem> TWEAK_FORTUNE_SILK_SWAP              = tweak("tweak-menu.tweaks.fortune-silk-swap");
	public final Option<TweakItem> TWEAK_HAPPY_GHAST_PLACEMENT          = tweak("tweak-menu.tweaks.happy-ghast-placement");
	public final Option<TweakItem> TWEAK_HAPPY_GHAST_SPEED              = tweak("tweak-menu.tweaks.happy-ghast-speed");
	public final Option<TweakItem> TWEAK_HAY_BALE_BREAD                 = tweak("tweak-menu.tweaks.hay-bale-bread");
	public final Option<TweakItem> TWEAK_HORSE_STATISTICS               = tweak("tweak-menu.tweaks.horse-statistics");
	public final Option<TweakItem> TWEAK_INVENTORY_CRAFTING             = tweak("tweak-menu.tweaks.inventory-crafting");
	public final Option<TweakItem> TWEAK_INVENTORY_ENDER_CHEST          = tweak("tweak-menu.tweaks.inventory-ender-chest");
	public final Option<TweakItem> TWEAK_ITEM_FRAME_CLICK_THROUGH       = tweak("tweak-menu.tweaks.item-frame-click-through");
	public final Option<TweakItem> TWEAK_ITEM_FRAME_INVISIBLE           = tweak("tweak-menu.tweaks.item-frame-invisible");
	public final Option<TweakItem> TWEAK_LADDER_PLACEMENT               = tweak("tweak-menu.tweaks.ladder-placement");
	public final Option<TweakItem> TWEAK_LADDER_TELEPORTATION_AUTOMATIC = tweak("tweak-menu.tweaks.ladder-teleportation.automatic");
	public final Option<TweakItem> TWEAK_LADDER_TELEPORTATION_MANUAL    = tweak("tweak-menu.tweaks.ladder-teleportation.manual");
	public final Option<TweakItem> TWEAK_NIGHT_VISION                   = tweak("tweak-menu.tweaks.night-vision");
	public final Option<TweakItem> TWEAK_PET_PROTECTION                 = tweak("tweak-menu.tweaks.pet-protection");
	public final Option<TweakItem> TWEAK_PORTAL_PROTECTION              = tweak("tweak-menu.tweaks.portal-protection");
	public final Option<TweakItem> TWEAK_RECIPE_UNLOCK_ALL              = tweak("tweak-menu.tweaks.recipe-unlock-all");
	public final Option<TweakItem> TWEAK_REVERT_FARMLAND                = tweak("tweak-menu.tweaks.revert-farmland");
	public final Option<TweakItem> TWEAK_REVERT_PATH                    = tweak("tweak-menu.tweaks.revert-path");
	public final Option<TweakItem> TWEAK_REVERT_STRIPPED                = tweak("tweak-menu.tweaks.revert-stripped");
	public final Option<TweakItem> TWEAK_ROTTEN_FLESH_TO_LEATHER        = tweak("tweak-menu.tweaks.rotten-flesh-to-leather");
	public final Option<TweakItem> TWEAK_SPONGE_IGNITE                  = tweak("tweak-menu.tweaks.sponge-ignite");
	public final Option<TweakItem> TWEAK_TORCH_THROW                    = tweak("tweak-menu.tweaks.torch-throw");
	public final Option<TweakItem> TWEAK_VEHICLE_PICKUP                 = tweak("tweak-menu.tweaks.vehicle-pickup");
	public final Option<TweakItem> TWEAK_VILLAGER_FOLLOW                = tweak("tweak-menu.tweaks.villager-follow");
	public final Option<TweakItem> TWEAK_WATER_BOTTLE_CONVERT_LAVA      = tweak("tweak-menu.tweaks.water-bottle-convert-lava");
	public final Option<TweakItem> TWEAK_WATER_BOTTLE_CRAFT             = tweak("tweak-menu.tweaks.water-bottle-craft");
	public final Option<TweakItem> TWEAK_WEAPON_SWING_THROUGH           = tweak("tweak-menu.tweaks.weapon-swing-through");
	public final Option<TweakItem> TWEAK_XP_FILL                        = tweak("tweak-menu.tweaks.xp-fill");

	// Special case -- Showing only the active tweaks in the lore.
	public final Option<TweakItem> TWEAK_SNOWBALLS = option("tweak-menu.tweaks.snowballs", sec -> {
		if (sec == null) {
			return TweakItem.create("No config: tweak-menu.tweaks.snowballs");
		}

		TweakItem item = TweakItem.create();
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

	// Drop Filter Menu
	public final Message<?> FILTER_MENU_TITLE     = message("filter-menu.title", "&8Drop Filter");
	public final Message<?> FILTER_MENU_ITEM_NAME = message("filter-menu.item-name", "&a{item}");
	public final Message<?> FILTER_MENU_ITEM_LORE = message("filter-menu.item-lore", "&7Click to Remove");

	public Menus(@Nonnull Tweaky plugin) {
		super(plugin, "menus.yml");
		this.plugin = plugin;
	}

	/**
	 * Creates a {@link TweakItem} option.
	 *
	 * @param path the path to the tweak item
	 * @return the option for the tweak item
	 */
	@Nonnull
	private Option<TweakItem> tweak(@Nonnull String path) {
		Option<TweakItem> option = new SimpleOption<>(provider -> {
			if (provider.getSection(path).isEmpty()) {
				return TweakItem.create("No config: " + path);
			}

			TweakItem item = TweakItem.create();
			provider.getString(path + ".name").ifPresent(item::name);
			provider.getStringList(path + ".lore").ifPresent(item::lore);
			provider.getStringList(path + ".usage").ifPresent(item::usage);
			return item;
		});
		return register(option);
	}
}
