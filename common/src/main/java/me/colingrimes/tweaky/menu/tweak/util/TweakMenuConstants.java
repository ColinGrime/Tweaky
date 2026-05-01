package me.colingrimes.tweaky.menu.tweak.util;

import me.colingrimes.tweaky.menu.slot.Pattern;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import java.util.List;

// Unsure if I should expose this in a config file, so for now I'll leave it here.
public class TweakMenuConstants {

	/**
	 * Gets the main menu pattern.
	 *
	 * @return the main menu pattern
	 */
	@Nonnull
	public static Pattern getMainMenuPattern() {
		return Pattern.create()
				.mask("XXXXXXXXX")
				.mask("XX1X2X3XX")
				.mask("XXXXXXXXX")
				.mask("X4X5X6X7X")
				.mask("XXXXXXXXX")
				.item('X', Items.of(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());
	}

	/**
	 * The order to place the category items in the Main Menu.
	 */
	public static final List<TweakCategory> CATEGORY_ORDER = List.of(
			TweakCategory.MOBS,
			TweakCategory.BLOCKS,
			TweakCategory.CROPS,
			TweakCategory.TEXT,
			TweakCategory.CONVENIENCE,
			TweakCategory.RECIPES,
			TweakCategory.MISCELLANEOUS
	);

	/**
	 * The specific order to show the tweaks in their corresponding tweak category menu.
	 */
	public static final List<String> TWEAK_ORDER = List.of(
			// Mobs
			"villager_follow",
			"villager_protection",
			"pet_protection",
			"happy_ghast_placement",
			"happy_ghast_speed",
			"horse_randomizer",
			"bee_capture",
			"entity_equip",
			"entity_ignite",
			"vehicle_mobs",
			"entity_silence",
			"entity_dye",
			// Blocks
			"break_ender_chest",
			"anvil_repair",
			"break_bedrock",
			"break_deepslate",
			"break_glass",
			"break_leaves",
			"break_plants",
			"cauldron_concrete",
			"cauldron_mud",
			"revert_farmland",
			"revert_path",
			"revert_stripped",
			"doors_double",
			"doors_iron",
			"sponge_ignite",
			"ladder_placement",
			// Crops
			"crops_harvest",
			"crops_protection",
			"crops_bone_meal",
			"hay_bale_bread",
			// Text
			"anvil_color",
			"coordinates",
			"death_notify",
			"horse_statistics",
			"breeding_indicator",
			"durability_indicator",
			// Convenience
			"fortune_silk_swap",
			"drops_filter",
			"enchanting_lapis",
			"xp_fill",
			"night_vision",
			"inventory_ender_chest",
			"inventory_crafting",
			"vehicle_pickup",
			"drops_magnet",
			"ladder_teleportation",
			"item_frame_click_through",
			"weapon_swing_through",
			"keep_inventory",
			// Recipes
			"recipe_unlock_all",
			"recipe_hopper",
			"recipe_chests",
			"recipe_sticks",
			"recipe_god_apple",
			"recipe_golden_carrot",
			"recipe_golden_melon",
			"recipe_redstone_comparator",
			"recipe_redstone_repeater",
			"recipe_unpack_quartz",
			"recipe_unpack_bookshelf",
			"recipe_unpack_book",
			"recipe_leather",
			"recipe_black_dye",
			"recipe_cobweb",
			"recipe_unpack_cobweb",
			"recipe_unpack_wool",
			"recipe_splash_water_bottles",
			// Misc
			"armor_swap",
			"snowballs",
			"water_bottle_convert_lava",
			"portal_protection",
			"item_frame_invisible",
			"armor_stand_arms",
			"torch_throw",
			"player_feed"
	);
}
