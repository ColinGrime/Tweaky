package me.colingrimes.tweaky.config.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Configuration;
import me.colingrimes.tweaky.config.option.Option;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Settings extends Configuration {

	public final Option<Boolean>       TWEAK_ANVIL_COLOR                   = option("tweaks.anvil-color");
	public final Option<Boolean>       TWEAK_ANVIL_REPAIR                  = option("tweaks.anvil-repair");
	public final Option<Boolean>       TWEAK_ARMOR_SWAP                    = option("tweaks.armor-swap");
	public final Option<Boolean>       TWEAK_BEE_CAPTURE                   = option("tweaks.bee-capture");
	public final Option<Boolean>       TWEAK_BREAK_DEEPSLATE               = option("tweaks.break-deepslate");
	public final Option<Boolean>       TWEAK_BREAK_ENDER_CHEST             = option("tweaks.break-ender-chest.toggle");
	public final Option<String>        TWEAK_BREAK_ENDER_CHEST_MODE        = option("tweaks.break-ender-chest.mode", "Protection");
	public final Option<Boolean>       TWEAK_BREAK_GLASS                   = option("tweaks.break-glass.toggle");
	public final Option<Set<Material>> TWEAK_BREAK_GLASS_MATERIALS         = option("tweaks.break-glass", sec -> sec.getStringList("materials").stream().map(m -> Material.getMaterial(m.toUpperCase())).filter(Objects::nonNull).collect(Collectors.toSet()));
	public final Option<Boolean>       TWEAK_BREAK_LEAVES                  = option("tweaks.break-leaves");
	public final Option<Boolean>       TWEAK_BREAK_PLANTS                  = option("tweaks.break-plants");
	public final Option<Boolean>       TWEAK_BREEDING_INDICATOR            = option("tweaks.breeding-indicator");
	public final Option<Boolean>       TWEAK_CAULDRON_CONCRETE             = option("tweaks.cauldron-concrete.toggle");
	public final Option<Boolean>       TWEAK_CAULDRON_CONCRETE_USE_WATER   = option("tweaks.cauldron-concrete.use-water");
	public final Option<Boolean>       TWEAK_CAULDRON_MUD                  = option("tweaks.cauldron-mud.toggle");
	public final Option<Boolean>       TWEAK_CAULDRON_MUD_USE_WATER        = option("tweaks.cauldron-mud.use-water");
	public final Option<Boolean>       TWEAK_COORDINATES                   = option("tweaks.coordinates");
	public final Option<Boolean>       TWEAK_CROPS_BONE_MEAL               = option("tweaks.crops-bone-meal");
	public final Option<Boolean>       TWEAK_CROPS_HARVEST                 = option("tweaks.crops-harvest");
	public final Option<Boolean>       TWEAK_CROPS_PROTECTION              = option("tweaks.crops-protection");
	public final Option<Boolean>       TWEAK_DEATH_NOTIFY                  = option("tweaks.death-notify");
	public final Option<Boolean>       TWEAK_DOORS_DOUBLE                  = option("tweaks.doors-double");
	public final Option<Boolean>       TWEAK_DOORS_IRON                    = option("tweaks.doors-iron");
	public final Option<Boolean>       TWEAK_DROPS_FILTER                  = option("tweaks.drops-filter");
	public final Option<Boolean>       TWEAK_DROPS_MAGNET                  = option("tweaks.drops-magnet");
	public final Option<Boolean>       TWEAK_ENCHANTING_LAPIS              = option("tweaks.enchanting-lapis");
	public final Option<Boolean>       TWEAK_ENTITY_DYE                    = option("tweaks.entity-dye");
	public final Option<Boolean>       TWEAK_ENTITY_EQUIP                  = option("tweaks.entity-equip");
	public final Option<Boolean>       TWEAK_ENTITY_IGNITE                 = option("tweaks.entity-ignite");
	public final Option<Boolean>       TWEAK_ENTITY_SILENCE                = option("tweaks.entity-silence");
	public final Option<Boolean>       TWEAK_FORTUNE_SILK_SWAP             = option("tweaks.fortune-silk-swap.toggle");
	public final Option<Integer>       TWEAK_FORTUNE_SILK_SWAP_COST        = option("tweaks.fortune-silk-swap.cost", 30);
	public final Option<Boolean>       TWEAK_HAPPY_GHAST_PLACEMENT         = option("tweaks.happy-ghast-placement");
	public final Option<Boolean>       TWEAK_HAPPY_GHAST_SPEED             = option("tweaks.happy-ghast-speed.toggle");
	public final Option<Double>        TWEAK_HAPPY_GHAST_SPEED_VALUE       = option("tweaks.happy-ghast-speed.value", 1.5);
	public final Option<Boolean>       TWEAK_HAY_BALE_BREAD                = option("tweaks.hay-bale-bread");
	public final Option<Boolean>       TWEAK_HORSE_STATISTICS              = option("tweaks.horse-statistics");
	public final Option<Boolean>       TWEAK_INVENTORY_CRAFTING            = option("tweaks.inventory-crafting");
	public final Option<Boolean>       TWEAK_INVENTORY_ENDER_CHEST         = option("tweaks.inventory-ender-chest.toggle");
	public final Option<Integer>       TWEAK_INVENTORY_ENDER_CHEST_COST    = option("tweaks.inventory-ender-chest.cost", 30);
	public final Option<Boolean>       TWEAK_ITEM_FRAME_CLICK_THROUGH      = option("tweaks.item-frame-click-through");
	public final Option<Boolean>       TWEAK_ITEM_FRAME_INVISIBLE          = option("tweaks.item-frame-invisible");
	public final Option<Boolean>       TWEAK_LADDER_PLACEMENT              = option("tweaks.ladder-placement");
	public final Option<Boolean>       TWEAK_LADDER_TELEPORTATION          = option("tweaks.ladder-teleportation.toggle");
	public final Option<String>        TWEAK_LADDER_TELEPORTATION_CONTROL  = option("tweaks.ladder-teleportation.control", "Automatic");
	public final Option<Boolean>       TWEAK_NIGHT_VISION                  = option("tweaks.night-vision");
	public final Option<Boolean>       TWEAK_PET_PROTECTION                = option("tweaks.pet-protection");
	public final Option<Boolean>       TWEAK_PORTAL_PROTECTION             = option("tweaks.portal-protection");
	public final Option<Boolean>       TWEAK_RECIPE_UNLOCK_ALL             = option("tweaks.recipe-unlock-all");
	public final Option<Boolean>       TWEAK_REVERT_FARMLAND               = option("tweaks.revert-farmland");
	public final Option<Boolean>       TWEAK_REVERT_PATH                   = option("tweaks.revert-path");
	public final Option<Boolean>       TWEAK_REVERT_STRIPPED               = option("tweaks.revert-stripped");
	public final Option<Boolean>       TWEAK_ROTTEN_FLESH_TO_LEATHER       = option("tweaks.rotten-flesh-to-leather");
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
	public final Option<Boolean>       TWEAK_SPONGE_IGNITE                 = option("tweaks.sponge-ignite");
	public final Option<Boolean>       TWEAK_TORCH_THROW                   = option("tweaks.torch-throw");
	public final Option<Boolean>       TWEAK_VEHICLE_PICKUP                = option("tweaks.vehicle-pickup");
	public final Option<Boolean>       TWEAK_VILLAGER_FOLLOW               = option("tweaks.villager-follow");
	public final Option<Boolean>       TWEAK_WATER_BOTTLE_CONVERT_LAVA     = option("tweaks.water-bottle-convert-lava");
	public final Option<Boolean>       TWEAK_WATER_BOTTLE_CRAFT            = option("tweaks.water-bottle-craft.toggle");
	public final Option<Integer>       TWEAK_WATER_BOTTLE_CRAFT_AMOUNT     = option("tweaks.water-bottle-craft.amount", 8);
	public final Option<Boolean>       TWEAK_WEAPON_SWING_THROUGH          = option("tweaks.weapon-swing-through");
	public final Option<Boolean>       TWEAK_XP_FILL                       = option("tweaks.xp-fill.toggle");
	public final Option<Integer>       TWEAK_XP_FILL_COST                  = option("tweaks.xp-fill.cost", 8);
	public final Option<Boolean> UPDATE_CHECKER_LOG     = option("update-checker.log", true);
	public final Option<Boolean> UPDATE_CHECKER_NOTIFY  = option("update-checker.notify", true);
	public final Option<Boolean> ENABLE_METRICS         = option("enable-metrics", true);

	public Settings(@Nonnull Tweaky plugin) {
		super(plugin, "config.yml");
	}
}
