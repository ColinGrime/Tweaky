package me.colingrimes.tweaky.menu.manager;

import me.colingrimes.tweaky.menu.Gui;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuManager {

	private final Map<Player, Gui> viewers = new HashMap<>();

	public void shutdown() {
		getMenus().forEach(Gui::invalidate);
	}

	/**
	 * Gets all the opened menus.
	 *
	 * @return the opened menus
	 */
	@Nonnull
	public List<Gui> getMenus() {
		return viewers.values().stream().toList();
	}

	/**
	 * Gets all the opened menus of the specified type.
	 *
	 * @param clazz the menu type
	 * @return the opened menus
	 */
	@Nonnull
	public <T> List<T> getMenus(@Nonnull Class<T> clazz) {
		return viewers.values().stream().filter(clazz::isInstance).map(clazz::cast).toList();
	}

	/**
	 * Gets the menu that the player is currently viewing (if any).
	 *
	 * @param player the player
	 * @return the opened menu
	 */
	@Nullable
	public Gui getMenu(@Nonnull Player player) {
		return viewers.get(player);
	}

	/**
	 * Associates the player with the menu, indicating they now have it opened.
	 * <p>
	 * Before it is associated, all other menus the player has opened are forced closed.
	 *
	 * @param player the player
	 * @param gui the gui they are opening
	 */
	public void addMenu(@Nonnull Player player, @Nonnull Gui gui) {
		closeMenu(player);
		viewers.put(player, gui);
	}

	/**
	 * Removes the menu from the player.
	 *
	 * @param player the player
	 */
	public void removeMenu(@Nonnull Player player) {
		viewers.remove(player);
	}

	/**
	 * Forces the player to close any opened menus.
	 *
	 * @param player the player
	 */
	public void closeMenu(@Nonnull Player player) {
		if (viewers.containsKey(player)) {
			viewers.get(player).invalidate();
		}
	}
}
