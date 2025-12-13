package me.colingrimes.tweaky.config;

import me.colingrimes.tweaky.Tweaky;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Settings {

	private final List<Option<?>> options = new ArrayList<>();
	public final Option<Boolean>       TWEAK_ANVIL_COLOR                   = option("tweaks.anvil-color");
	public final Option<Boolean>       TWEAK_ANVIL_REPAIR                  = option("tweaks.anvil-repair");
	public final Option<Boolean>       TWEAK_ARMOR_SWAP                    = option("tweaks.armor-swap");
	public final Option<Boolean>       TWEAK_BEE_CAPTURE                   = option("tweaks.bee-capture");
	public final Option<Boolean>       TWEAK_CONCRETE_CONVERSION           = option("tweaks.concrete-conversion.toggle");
	public final Option<Boolean>       TWEAK_CONCRETE_CONVERSION_USE_WATER = option("tweaks.concrete-conversion.use-water");
	public final Option<Boolean>       TWEAK_CROPS_BONE_MEAL               = option("tweaks.crops-bone-meal");
	public final Option<Boolean>       TWEAK_CROPS_HARVEST                 = option("tweaks.crops-harvest");
	public final Option<Boolean>       TWEAK_CROPS_LAWN_MOWER              = option("tweaks.crops-lawn-mower");
	public final Option<Boolean>       TWEAK_CROPS_TRAMPLE_PROOF           = option("tweaks.crops-trample-proof");
	public final Option<Boolean>       TWEAK_DEEPSLATE_BREAK               = option("tweaks.deepslate-break");
	public final Option<Boolean>       TWEAK_DOORS_DOUBLE                  = option("tweaks.doors-double");
	public final Option<Boolean>       TWEAK_DOORS_IRON                    = option("tweaks.doors-iron");
	public final Option<Boolean>       TWEAK_DROPS_MAGNET                  = option("tweaks.drops-magnet");
	public final Option<Boolean>       TWEAK_ENCHANTING_LAPIS              = option("tweaks.enchanting-lapis");
	public final Option<Boolean>       TWEAK_ENTITY_EQUIP                  = option("tweaks.entity-equip");
	public final Option<Boolean>       TWEAK_ENTITY_SET_ON_FIRE            = option("tweaks.entity-set-on-fire");
	public final Option<Boolean>       TWEAK_FIRE_DRIES_SPONGES            = option("tweaks.fire-dries-sponges");
	public final Option<Boolean>       TWEAK_GLASS_BREAK                   = option("tweaks.glass-break.toggle");
	public final Option<Set<Material>> TWEAK_GLASS_BREAK_MATERIALS         = option("tweaks.glass-break", sec -> sec.getStringList("materials").stream().map(m -> Material.getMaterial(m.toUpperCase())).filter(Objects::nonNull).collect(Collectors.toSet()));
	public final Option<Boolean>       TWEAK_HAPPY_GHAST_PLACEMENT         = option("tweaks.happy-ghast-placement");
	public final Option<Boolean>       TWEAK_HAPPY_GHAST_SPEED             = option("tweaks.happy-ghast-speed.toggle");
	public final Option<Double>        TWEAK_HAPPY_GHAST_SPEED_VALUE       = option("tweaks.happy-ghast-speed.value", 1.5);
	public final Option<Boolean>       TWEAK_HAY_BALE_BREAD                = option("tweaks.hay-bale-bread");
	public final Option<Boolean>       TWEAK_HORSE_STATISTICS              = option("tweaks.horse-statistics.toggle");
	public final Option<List<String>>  TWEAK_HORSE_STATISTICS_MESSAGE      = option("tweaks.horse-statistics.message", List.of());
	public final Option<Boolean>       TWEAK_INVENTORY_CRAFTING            = option("tweaks.inventory-crafting");
	public final Option<Boolean>       TWEAK_ITEM_FRAME_CLICK_THROUGH      = option("tweaks.item-frame-click-through");
	public final Option<Boolean>       TWEAK_ITEM_FRAME_INVISIBLE          = option("tweaks.item-frame-invisible");
	public final Option<Boolean>       TWEAK_LADDER_PLACEMENT              = option("tweaks.ladder-placement");
	public final Option<Boolean>       TWEAK_LADDER_TELEPORTATION          = option("tweaks.ladder-teleportation.toggle");
	public final Option<String>        TWEAK_LADDER_TELEPORTATION_CONTROL  = option("tweaks.ladder-teleportation.control", "Automatic");
	public final Option<Boolean>       TWEAK_LEAF_CUTTER                   = option("tweaks.leaf-cutter");
	public final Option<Boolean>       TWEAK_MUD_CONVERSION                = option("tweaks.mud-conversion.toggle");
	public final Option<Boolean>       TWEAK_MUD_CONVERSION_USE_WATER      = option("tweaks.mud-conversion.use-water");
	public final Option<Boolean>       TWEAK_NAME_TAG_DYE                  = option("tweaks.name-tag-dye");
	public final Option<Boolean>       TWEAK_PORTAL_EXPLOSION_PROOF        = option("tweaks.portal-explosion-proof");
	public final Option<Boolean>       TWEAK_RECIPE_UNLOCK_ALL             = option("tweaks.recipe-unlock-all");
	public final Option<Boolean>       TWEAK_SNOWBALLS_ADD_SNOW_LAYER      = option("tweaks.snowballs-add-snow-layer");
	public final Option<Boolean>       TWEAK_SNOWBALLS_BREAK_PLANTS        = option("tweaks.snowballs-break-plants");
	public final Option<Boolean>       TWEAK_SNOWBALLS_DAMAGE              = option("tweaks.snowballs-damage.toggle");
	public final Option<Double>        TWEAK_SNOWBALLS_DAMAGE_AMOUNT       = option("tweaks.snowballs-damage.amount", 1.0);
	public final Option<Boolean>       TWEAK_SNOWBALLS_EXTINGUISH_ENTITIES = option("tweaks.snowballs-extinguish-entities");
	public final Option<Boolean>       TWEAK_SNOWBALLS_EXTINGUISH_FIRE     = option("tweaks.snowballs-extinguish-fire");
	public final Option<Boolean>       TWEAK_SNOWBALLS_FORM_ICE            = option("tweaks.snowballs-form-ice");
	public final Option<Boolean>       TWEAK_SNOWBALLS_FORM_SNOW           = option("tweaks.snowballs-form-snow");
	public final Option<Boolean>       TWEAK_SNOWBALLS_KNOCKBACK           = option("tweaks.snowballs-knockback.toggle");
	public final Option<Double>        TWEAK_SNOWBALLS_KNOCKBACK_AMOUNT    = option("tweaks.snowballs-knockback.amount", 0.5);
	public final Option<Boolean>       TWEAK_TORCH_THROW                   = option("tweaks.torch-throw");
	public final Option<Boolean>       TWEAK_VEHICLE_PICKUP                = option("tweaks.vehicle-pickup");
	public final Option<Boolean>       TWEAK_WATER_BOTTLE_CONVERT_LAVA     = option("tweaks.water-bottle-convert-lava");
	public final Option<Boolean>       TWEAK_WATER_BOTTLE_CRAFT            = option("tweaks.water-bottle-craft.toggle");
	public final Option<Integer>       TWEAK_WATER_BOTTLE_CRAFT_AMOUNT     = option("tweaks.water-bottle-craft.amount", 8);
	public final Option<Boolean>       TWEAK_WEAPON_SWING_THROUGH_GRASS    = option("tweaks.weapon-swing-through-grass");
	public final Option<Boolean>       TWEAK_XP_FILL                       = option("tweaks.xp-fill.toggle");
	public final Option<Integer>       TWEAK_XP_FILL_COST                  = option("tweaks.xp-fill.cost", 8);
	public final Option<String>  RELOADED       = option("reloaded", "&2&l✓ &a&lTweaks &ahas been reloaded. Registered &l{amount} &atweaks.");
	public final Option<String>  RESET_RECIPES  = option("reset-recipes", "&2&l✓ &aRecipes have been reset for all online players.");
	public final Option<String>  NO_PERMISSION  = option("no-permission", "&4&l❌ &cYou lack the required permission for this command.");
	public final Option<Boolean> ENABLE_METRICS = option("enable-metrics", true);

	private final Tweaky plugin;

	public Settings(@Nonnull Tweaky plugin) {
		this.plugin = plugin;
		plugin.saveDefaultConfig();
	}

	/**
	 * Reloads the settings, updating the values of all options.
	 */
	public void reload() {
		plugin.reloadConfig();
		FileConfiguration config = plugin.getConfig();
		options.forEach(option -> option.reload(config));
	}

	@Nonnull
	private Option<Boolean> option(@Nonnull String path) {
		Option<Boolean> option = new Option<>(config -> config.getBoolean(path, false));
		options.add(option);
		return option;
	}

	@Nonnull
	private Option<String> option(@Nonnull String path, @Nonnull String def) {
		Option<String> option = new Option<>(config -> config.getString(path, def));
		options.add(option);
		return option;
	}

	@Nonnull
	private Option<List<String>> option(@Nonnull String path, @Nonnull List<String> def) {
		Option<List<String>> option = new Option<>(config -> {
			List<String> value = config.getStringList(path);
			return value.isEmpty() ? def : value;
		});
		options.add(option);
		return option;
	}

	@Nonnull
	private Option<Integer> option(@Nonnull String path, int def) {
		Option<Integer> option = new Option<>(config -> config.getInt(path, def));
		options.add(option);
		return option;
	}

	@Nonnull
	private Option<Double> option(@Nonnull String path, double def) {
		Option<Double> option = new Option<>(config -> config.getDouble(path, def));
		options.add(option);
		return option;
	}

	@Nonnull
	private Option<Boolean> option(@Nonnull String path, boolean def) {
		Option<Boolean> option = new Option<>(config -> config.getBoolean(path, def));
		options.add(option);
		return option;
	}

	@Nonnull
	private <T> Option<T> option(@Nonnull String path, @Nonnull Function<ConfigurationSection, T> extractor) {
		Option<T> option = new Option<>(config -> extractor.apply(config.getConfigurationSection(path)));
		options.add(option);
		return option;
	}
}
