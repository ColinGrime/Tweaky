package me.colingrimes.tweaky.config.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.config.option.Option;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Settings extends Configuration {

	// Tweak Toggles
	public final Option<Boolean>       TWEAK_DOORS_IRON                    = getTweak("doors-iron");
	public final Option<Boolean>       TWEAK_SNOWBALLS_ADD_SNOW_LAYER      = getTweak("snowballs-add-snow-layer");
	public final Option<Boolean>       TWEAK_SNOWBALLS_BREAK_PLANTS        = getTweak("snowballs-break-plants");
	public final Option<Boolean>       TWEAK_SNOWBALLS_DAMAGE              = getTweak("snowballs-damage");
	public final Option<Boolean>       TWEAK_SNOWBALLS_EXTINGUISH_ENTITIES = getTweak("snowballs-extinguish-entities");
	public final Option<Boolean>       TWEAK_SNOWBALLS_EXTINGUISH_FIRE     = getTweak("snowballs-extinguish-fire");
	public final Option<Boolean>       TWEAK_SNOWBALLS_FORM_ICE            = getTweak("snowballs-form-ice");
	public final Option<Boolean>       TWEAK_SNOWBALLS_FORM_SNOW           = getTweak("snowballs-form-snow");
	public final Option<Boolean>       TWEAK_SNOWBALLS_KNOCKBACK           = getTweak("snowballs-knockback");

	// Tweak Settings
	public final Option<Integer>       TWEAK_BREAK_BEDROCK_SECONDS         = option("tweaks.break-bedrock.seconds", 60);
	public final Option<String>        TWEAK_BREAK_ENDER_CHEST_MODE        = option("tweaks.break-ender-chest.mode", "Protection");
	public final Option<Set<Material>> TWEAK_BREAK_GLASS_MATERIALS         = option("tweaks.break-glass.materials", "tweaks.break-glass", sec -> sec.getStringList("materials").stream().map(m -> Material.getMaterial(m.toUpperCase())).filter(Objects::nonNull).collect(Collectors.toSet()));
	public final Option<Boolean>       TWEAK_CAULDRON_CONCRETE_USE_WATER   = option("tweaks.cauldron-concrete.use-water");
	public final Option<Boolean>       TWEAK_CAULDRON_MUD_USE_WATER        = option("tweaks.cauldron-mud.use-water");
	public final Option<Integer>       TWEAK_DURABILITY_INDICATOR_AMOUNT   = option("tweaks.durability-indicator.durability", 10);
	public final Option<Set<Material>> TWEAK_DURABILITY_INDICATOR_MATS     = option("tweaks.durability-indicator.materials", "tweaks.durability-indicator", sec -> sec.getStringList("materials").stream().map(m -> Material.getMaterial(m.toUpperCase())).filter(Objects::nonNull).collect(Collectors.toSet()));
	public final Option<Integer>       TWEAK_FORTUNE_SILK_SWAP_COST        = option("tweaks.fortune-silk-swap.cost", 30);
	public final Option<Double>        TWEAK_HAPPY_GHAST_SPEED_VALUE       = option("tweaks.happy-ghast-speed.value", 1.5);
	public final Option<Integer>       TWEAK_INVENTORY_ENDER_CHEST_COST    = option("tweaks.inventory-ender-chest.cost", 30);
	public final Option<Boolean>       TWEAK_KEEP_INVENTORY_XP             = option("tweaks.keep-inventory.xp");
	public final Option<Boolean>       TWEAK_KEEP_INVENTORY_ARMOR          = option("tweaks.keep-inventory.armor", true);
	public final Option<Boolean>       TWEAK_KEEP_INVENTORY_TOOLS          = option("tweaks.keep-inventory.tools", true);
	public final Option<Double>        TWEAK_KEEP_INVENTORY_UNSTACKABLES   = option("tweaks.keep-inventory.unstackables", "tweaks.keep-inventory", sec -> Double.parseDouble(sec.getString("unstackables", "0%").replace("%", "")));
	public final Option<Double>        TWEAK_KEEP_INVENTORY_STACKABLES     = option("tweaks.keep-inventory.stackables", "tweaks.keep-inventory", sec -> Double.parseDouble(sec.getString("stackables", "0%").replace("%", "")));
	public final Option<String>        TWEAK_LADDER_TELEPORTATION_CONTROL  = option("tweaks.ladder-teleportation.control", "Automatic");
	public final Option<Double>        TWEAK_SNOWBALLS_DAMAGE_AMOUNT       = option("tweaks.snowballs-damage.amount", 1.0);
	public final Option<Double>        TWEAK_SNOWBALLS_KNOCKBACK_AMOUNT    = option("tweaks.snowballs-knockback.amount", 0.5);
	public final Option<Integer>       TWEAK_WATER_BOTTLE_CRAFT_AMOUNT     = option("tweaks.water-bottle-craft.amount", 8);
	public final Option<Integer>       TWEAK_XP_FILL_COST                  = option("tweaks.xp-fill.cost", 8);

	// Everything Else
	public final Option<Boolean> UPDATE_CHECKER_LOG     = option("update-checker.log", true);
	public final Option<Boolean> UPDATE_CHECKER_NOTIFY  = option("update-checker.notify", true);
	public final Option<Boolean> ENABLE_METRICS         = option("enable-metrics", true);

	public Settings(@Nonnull Tweaky plugin) {
		super(plugin, "config.yml");
	}

	/**
	 * Gets the tweak option for the specified tweak ID.
	 *
	 * @param id the tweak ID
	 * @return the tweak option
	 */
	@Nonnull
	public Option<Boolean> getTweak(@Nonnull String id) {
		String defPath = "tweaks." + id.toLowerCase().replace("_", "-");
		String nestedPath = defPath + ".toggle";

		Optional<Option<Boolean>> defOption = getOption(defPath);
		if (defOption.isPresent()) {
			return defOption.get();
		}

		Optional<Option<Boolean>> nestedOption = getOption(nestedPath);
		if (nestedOption.isPresent()) {
			return nestedOption.get();
		}

		return hasPath(nestedPath) ? option(nestedPath) : option(defPath);
	}
}
