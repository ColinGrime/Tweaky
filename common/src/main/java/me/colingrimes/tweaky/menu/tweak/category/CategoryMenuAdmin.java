package me.colingrimes.tweaky.menu.tweak.category;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.tweak.TweakMenu;
import me.colingrimes.tweaky.menu.tweak.recipe.RecipePreview;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.type.ConfigurableTweak;
import me.colingrimes.tweaky.tweak.type.MultiTweak;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
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

import static me.colingrimes.tweaky.menu.tweak.util.TweakMenuConstants.TWEAK_ORDER;

public class CategoryMenuAdmin extends Gui implements Listener {

	private final List<Tweak> tweaks;
	private boolean reopenTweakMenu = true;

	public CategoryMenuAdmin(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull String title, @Nonnull List<Tweak> tweaks) {
		super(plugin, player, title, (int) (Math.ceil(tweaks.size() / 7.0) + 2));
		this.tweaks = tweaks.stream().sorted(Comparator.comparingInt(t -> TWEAK_ORDER.contains(t.getId()) ? TWEAK_ORDER.indexOf(t.getId()) : Integer.MAX_VALUE)).toList();
	}

	@Override
	protected void onClose() {
		if (reopenTweakMenu) {
			TweakMenu.admin(plugin, player).open();
		}
	}

	@Override
	public void draw() {
		for (int i=0; i<Math.min(28, tweaks.size()); i++) {
			int slot = (i + 10) + (i / 7 * 2);
			Tweak tweak = tweaks.get(i);
			Items.Builder tweakItem = tweak.getGuiItem().copy();

			// Add lore if the tweak is multi or configurable.
			if (tweak instanceof MultiTweak) {
				tweakItem.lore(List.of("&c→ &e&oCan only be toggled in the config.yml file."));
			} else if (tweak instanceof ConfigurableTweak configurableTweak) {
				tweakItem
						.lore()
						.lore("&c→ &e&oThis tweak has " + configurableTweak.getOptionCount() + " additional option(s)")
						.lore("   &e&othat can be set in the config.yml file.");
			}

			ItemStack item = tweakItem.build();

			// Change any skulls to the player skull.
			if (item.getItemMeta() instanceof SkullMeta skull) {
				skull.setOwningPlayer(player);
				item.setItemMeta(skull);
			}

			// Set the item in the display.
			getSlot(slot).setItem(updateItem(item, tweak.isEnabled()));

			// Toggle tweak on left click.
			if (!(tweak instanceof MultiTweak)) {
				getSlot(slot).bind(ClickType.LEFT, __ -> {
					plugin.getTweakManager().toggle(tweak.getId(), player);
					getSlot(slot).setItem(updateItem(item, tweak.isEnabled()));
				});
			}

			// Set preview to be sent on right click (RECIPE CATEGORY ONLY).
			if (tweak instanceof RecipeTweak recipeTweak) {
				getSlot(slot).bind(ClickType.RIGHT, __ -> {
					reopenTweakMenu = false;
					RecipePreview.of(plugin, player, recipeTweak, () -> new CategoryMenuAdmin(plugin, player, title, tweaks).open()).open();
				});
			}
		}

		int size = inventory.getSize();
		for (int i=0; i<size; i++) {
			if (i < 9 || i >= size - 9 || i % 9 == 0 || (i + 1) % 9 == 0) {
				String name = "&7Left-click to toggle. Right-click recipe tweaks to preview.";
				getSlot(i).setItem(Items.of(Material.BLACK_STAINED_GLASS_PANE).name(name).build());
			}
		}
	}

	/**
	 * Updates the item depending on the tweak enable/disable status.
	 *
	 * @param item the item
	 * @param enabled whether the tweak is enabled
	 * @return the updated item
	 */
	@Nonnull
	private ItemStack updateItem(@Nonnull ItemStack item, boolean enabled) {
		Material material = enabled ? item.getType() : Material.RED_STAINED_GLASS_PANE;
		String rename = Text.recolor(plugin.getMessageService().getName(item).toText(), enabled ? "&a" : "&c");
		return Items.of(item.clone()).material(material).name(rename).build();
	}
}
