package me.colingrimes.tweaky.menu.tweak.category;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.tweak.TweakMenu;
import me.colingrimes.tweaky.menu.tweak.recipe.RecipePreview;
import me.colingrimes.tweaky.menu.tweak.util.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
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

public class CategoryMenu extends Gui implements Listener {

	private final List<Tweak> tweaks;
	private boolean reopenTweakMenu = true;

	public CategoryMenu(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull String title, @Nonnull List<Tweak> tweaks) {
		super(plugin, player, title, (int) (Math.ceil(tweaks.size() / 7.0) + 2));
		this.tweaks = tweaks.stream().sorted(Comparator.comparingInt(t -> TWEAK_ORDER.contains(t.getId()) ? TWEAK_ORDER.indexOf(t.getId()) : Integer.MAX_VALUE)).toList();
	}

	@Override
	protected void onClose() {
		if (reopenTweakMenu) {
			TweakMenu.player(plugin, player).open();
		}
	}

	@Override
	public void draw() {
		for (int i=0; i<Math.min(28, tweaks.size()); i++) {
			int slot = (i + 10) + (i / 7 * 2);
			Tweak tweak = tweaks.get(i);
			TweakItem tweakItem = tweak.getGuiItem();
			ItemStack item = tweakItem.build();

			// Change any skulls to the player skull.
			if (item.getItemMeta() instanceof SkullMeta skull) {
				skull.setOwningPlayer(player);
				item.setItemMeta(skull);
			}

			// Set the item in the display.
			getSlot(slot).setItem(item);

			if (tweak instanceof RecipeTweak recipeTweak) {
				// Set preview to be sent on click (RECIPE CATEGORY ONLY).
				getSlot(slot).bind(__ -> {
					reopenTweakMenu = false;
					RecipePreview.of(plugin, player, recipeTweak, () -> new CategoryMenu(plugin, player, title, tweaks).open()).open();
				}, ClickType.LEFT, ClickType.RIGHT);
			} else {
				// Set usage message to be sent on click.
				getSlot(slot).bind(e -> {
					tweakItem.sendUsage(e.getWhoClicked());
					reopenTweakMenu = false;
					invalidate();
				}, ClickType.LEFT, ClickType.RIGHT);
			}
		}

		int size = inventory.getSize();
		for (int i=0; i<size; i++) {
			if (i < 9 || i >= size - 9 || i % 9 == 0 || (i + 1) % 9 == 0) {
				String name = "&7Click on any Tweak to learn how to use it.";
				getSlot(i).setItem(Items.of(Material.BLACK_STAINED_GLASS_PANE).name(name).build());
			}
		}
	}
}
