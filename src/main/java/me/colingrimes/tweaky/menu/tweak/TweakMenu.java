package me.colingrimes.tweaky.menu.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;

public class TweakMenu extends Gui {

	// The specific order to show the tweaks.
	private static final List<String> ORDER = List.of(
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
			"weapon_swing_through_grass",
			"doors_double",
			"doors_iron",
			"enchanting_lapis",
			"xp_fill",
			"armor_swap",
			"entity_ignite",
			"entity_silence",
			"entity_dye",
			"entity_equip",
			"happy_ghast_placement",
			"happy_ghast_speed",
			"portal_protection",
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
			"drops_magnet",
			"snowballs",
			"torch_throw",
			"water_bottle_convert_lava",
			"water_bottle_craft"
	);

	private final Tweaky plugin;

	public TweakMenu(@Nonnull Tweaky plugin, @Nonnull Player player) {
		super(player, "&8Tweaks (&2" + plugin.getTweakCount() + "&8)", 6);
		this.plugin = plugin;
	}

	@Override
	public void draw() {
		List<Tweak> tweaks = plugin.getTweaks().stream().sorted(Comparator.comparingInt(t -> ORDER.contains(t.getId()) ? ORDER.indexOf(t.getId()) : Integer.MAX_VALUE)).toList();
		for (int i=0; i<Math.min(54, tweaks.size()); i++) {
			TweakItem tweakItem = tweaks.get(i).getGuiItem();
			ItemStack item = tweakItem.hide().build();

			// Change any skulls to the player skull.
			if (item.getItemMeta() instanceof SkullMeta skull) {
				skull.setOwningPlayer(player);
				item.setItemMeta(skull);
			}

			// Set the item in the display.
			getSlot(i).setItem(item);

			// Set usage message to be sent on click.
			getSlot(i).bind(e -> {
				tweakItem.sendUsage(e.getWhoClicked());
				close();
			}, ClickType.LEFT, ClickType.RIGHT);
		}
	}
}