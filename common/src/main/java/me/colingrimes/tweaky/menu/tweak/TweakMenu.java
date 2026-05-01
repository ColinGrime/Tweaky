package me.colingrimes.tweaky.menu.tweak;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.slot.Pattern;
import me.colingrimes.tweaky.menu.tweak.category.CategoryMenuAdmin;
import me.colingrimes.tweaky.menu.tweak.category.CategoryMenu;
import me.colingrimes.tweaky.menu.tweak.util.TweakMenuConstants;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.manager.TweakQuery;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;

import static me.colingrimes.tweaky.menu.tweak.util.TweakMenuConstants.CATEGORY_ORDER;

public class TweakMenu extends Gui {

	private final Pattern MAIN_MENU;
	private final TweakQuery query;
	private final BiConsumer<String, List<Tweak>> openCategoryMenu;

	/**
	 * Opens the tweak menu for regular players.
	 *
	 * @param plugin the plugin
	 * @param player the player
	 * @return the tweak menu
	 */
	@Nonnull
	public static TweakMenu player(@Nonnull Tweaky plugin, @Nonnull Player player) {
		TweakQuery query = TweakQuery.enabled();
		String title = plugin.getMenus().TWEAK_MENU_NAME.replace("{count}", plugin.getTweakManager().getTweakCount(query)).toText();
		return new TweakMenu(plugin, player, title, TweakQuery.enabled(), (titleCategory, tweaks) -> new CategoryMenu(plugin, player, titleCategory, tweaks).open());
	}

	/**
	 * Opens the tweak menu for admins. This will allow them to toggle tweaks in the category menus.
	 *
	 * @param plugin the plugin
	 * @param player the admin
	 * @return the tweak menu
	 */
	@Nonnull
	public static TweakMenu admin(@Nonnull Tweaky plugin, @Nonnull Player player) {
		TweakQuery query = TweakQuery.all();
		String title = plugin.getMenus().TWEAK_MENU_NAME.replace("{count}", plugin.getTweakManager().getTweakCount(query)).toText() + " &8- &4ADMIN";
		return new TweakMenu(plugin, player, title, TweakQuery.all(), (titleCategory, tweaks) -> new CategoryMenuAdmin(plugin, player, titleCategory + " &8- &4ADMIN", tweaks).open());
	}

	private TweakMenu(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull String title, @Nonnull TweakQuery query, @Nonnull BiConsumer<String, List<Tweak>> openCategoryMenu) {
		super(plugin, player, title, 5);
		this.MAIN_MENU = TweakMenuConstants.getMainMenuPattern();
		this.query = query;
		this.openCategoryMenu = openCategoryMenu;
	}

	@Override
	public void draw() {
		int i = 1;
		for (TweakCategory category : CATEGORY_ORDER) {
			char ch = (char) ('0' + i);
			i += 1;

			List<Tweak> tweaks = plugin.getTweakManager().getTweaks(query.inCategory(category));
			ItemStack item = plugin.getMenus().TWEAK_MENU_CATEGORIES.get().get(category).placeholder("{count}", Tweak.count(tweaks, query)).build();

			MAIN_MENU.item(ch, item);
			MAIN_MENU.bind(ch, ClickType.LEFT, __ -> {
				String title = Text.recolor(plugin.getMessageService().getName(item).toText(), "&8");
				openCategoryMenu.accept(title, tweaks);
			});
		}

		MAIN_MENU.fill(this);
	}
}
