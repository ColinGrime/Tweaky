package me.colingrimes.tweaky.menu.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.pattern.MultiPattern;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.TweakManager;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;

public class TweakMenu extends Gui {

	// The specific order to show the tweaks.
	private static final List<String> TWEAK_ORDER = List.of(
			"anvil_color",
			"anvil_repair",
			"bedrock_break",
			"break_deepslate",
			"break_glass",
			"break_ender_chest",
			"break_leaves",
			"break_plants",
			"cauldron_concrete",
			"cauldron_mud",
			"coordinates",
			"death_notify",
			"crops_harvest",
			"crops_bone_meal",
			"crops_protection",
			"hay_bale_bread",
			"weapon_swing_through",
			"doors_double",
			"doors_iron",
			"enchanting_lapis",
			"xp_fill",
			"fortune_silk_swap",
			"armor_swap",
			"entity_ignite",
			"entity_silence",
			"entity_dye",
			"entity_equip",
			"happy_ghast_placement",
			"happy_ghast_speed",
			"portal_protection",
			"inventory_ender_chest",
			"inventory_crafting",
			"recipe_unlock_all",
			"item_frame_click_through",
			"item_frame_invisible",
			"ladder_placement",
			"ladder_teleportation",
			"horse_statistics",
			"bee_capture",
			"sponge_ignite",
			"vehicle_pickup",
			"drops_filter",
			"drops_magnet",
			"rotten_flesh_to_leather",
			"snowballs",
			"torch_throw",
			"villager_follow",
			"breeding_indicator",
			"pet_protection",
			"revert_farmland",
			"revert_path",
			"revert_stripped",
			"water_bottle_convert_lava",
			"water_bottle_craft",
			"night_vision"
	);

	private static final List<TweakCategory> CATEGORY_ORDER = List.of(
			TweakCategory.MOBS,
			TweakCategory.BLOCKS,
			TweakCategory.CROPS,
			TweakCategory.TEXT,
			TweakCategory.CONVENIENCE,
			TweakCategory.RECIPES,
			TweakCategory.MISCELLANEOUS
	);

	private final MultiPattern MAIN_MENU = MultiPattern.create()
			.mask("XXXXXXXXX")
			.mask("XX1X2X3XX")
			.mask("XXXXXXXXX")
			.mask("X4X5X6X7X")
			.mask("XXXXXXXXX")
			.item('X', Items.of(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());

	private final Tweaky plugin;

	public TweakMenu(@Nonnull Tweaky plugin, @Nonnull Player player) {
		super(player, plugin.getMenus().TWEAK_MENU_NAME.replace("{count}", plugin.getTweakManager().getTweakCount(player)).toText(), 5);
		this.plugin = plugin;
	}

	@Override
	public void draw() {
		int i = 1;
		for (TweakCategory category : CATEGORY_ORDER) {
			char ch = (char) ('0' + i);
			i += 1;

			List<Tweak> tweaks = plugin.getTweakManager().getTweaks(player, category);
			ItemStack item = plugin.getMenus().TWEAK_MENU_CATEGORIES.get().get(category).placeholder("{count}", TweakManager.countTweaks(tweaks)).build();
			String title = Text.color("&8" + item.getItemMeta().getDisplayName().substring(2));

			MAIN_MENU.item(ch, item);
			MAIN_MENU.bind(ch, ClickType.LEFT, __ -> new CategoryMenu(player, title, tweaks).open());
		}

		MAIN_MENU.fill(this);
	}

	public static class CategoryMenu extends Gui implements Listener {

		private final List<Tweak> tweaks;

		public CategoryMenu(@Nonnull Player player, @Nonnull String title, @Nonnull List<Tweak> tweaks) {
			super(player, title, (int) (Math.ceil(tweaks.size() / 7.0) + 2));
			this.tweaks = tweaks.stream().sorted(Comparator.comparingInt(t -> TWEAK_ORDER.contains(t.getId()) ? TWEAK_ORDER.indexOf(t.getId()) : Integer.MAX_VALUE)).toList();
		}

		@Override
		public void draw() {
			for (int i=0; i<Math.min(28, tweaks.size()); i++) {
				int slot = (i + 10) + (i / 7 * 2);
				TweakItem tweakItem = tweaks.get(i).getGuiItem();
				ItemStack item = tweakItem.hide().build();

				// Change any skulls to the player skull.
				if (item.getItemMeta() instanceof SkullMeta skull) {
					skull.setOwningPlayer(player);
					item.setItemMeta(skull);
				}

				// Set the item in the display.
				getSlot(slot).setItem(item);

				// Set usage message to be sent on click.
				getSlot(slot).bind(e -> {
					tweakItem.sendUsage(e.getWhoClicked());
					invalidate();
				}, ClickType.LEFT, ClickType.RIGHT);
			}

			int size = inventory.getSize();
			for (int i=0; i<size; i++) {
				if (i < 9 || i >= size - 9 || i % 9 == 0 || (i + 1) % 9 == 0) {
					getSlot(i).setItem(Material.BLACK_STAINED_GLASS_PANE);
				}
			}
		}
	}
}
