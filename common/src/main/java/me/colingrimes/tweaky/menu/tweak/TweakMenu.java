package me.colingrimes.tweaky.menu.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.pattern.Pattern;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.TweakManager;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class TweakMenu extends Gui {

	private static final List<TweakCategory> CATEGORY_ORDER = List.of(
			TweakCategory.MOBS,
			TweakCategory.BLOCKS,
			TweakCategory.CROPS,
			TweakCategory.TEXT,
			TweakCategory.CONVENIENCE,
			TweakCategory.RECIPES,
			TweakCategory.MISCELLANEOUS
	);

	private final Pattern MAIN_MENU = Pattern.create()
			.mask("XXXXXXXXX")
			.mask("XX1X2X3XX")
			.mask("XXXXXXXXX")
			.mask("X4X5X6X7X")
			.mask("XXXXXXXXX")
			.item('X', Items.of(Material.BLACK_STAINED_GLASS_PANE).name(" ").build());

	public TweakMenu(@Nonnull Tweaky plugin, @Nonnull Player player) {
		super(plugin, player, plugin.getMenus().TWEAK_MENU_NAME.replace("{count}", plugin.getTweakManager().getTweakCount(player)).toText(), 5);
	}

	@Override
	public void draw() {
		int i = 1;
		for (TweakCategory category : CATEGORY_ORDER) {
			char ch = (char) ('0' + i);
			i += 1;

			List<Tweak> tweaks = plugin.getTweakManager().getTweaks(player, category);
			ItemStack item = plugin.getMenus().TWEAK_MENU_CATEGORIES.get().get(category).placeholder("{count}", TweakManager.countTweaks(tweaks)).build();

			MAIN_MENU.item(ch, item);
			MAIN_MENU.bind(ch, ClickType.LEFT, __ -> TweakMenuCategory.of(plugin, player, category).open());
		}

		MAIN_MENU.fill(this);
	}
}
