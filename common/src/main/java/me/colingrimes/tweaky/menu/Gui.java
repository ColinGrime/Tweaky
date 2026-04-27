package me.colingrimes.tweaky.menu;

import com.google.common.base.Preconditions;
import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.manager.MenuManager;
import me.colingrimes.tweaky.menu.slot.SimpleSlot;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@link Gui} menu in the game.
 * Extend this class if you want basic menu functionalities.
 */
public abstract class Gui {

	protected final Tweaky plugin;
	protected final MenuManager manager;
	protected final Player player;
	protected final Inventory inventory;
	private final Map<Integer, Slot> slots = new HashMap<>();
	private boolean valid = true;

	public Gui(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull String title, int rows) {
		this.plugin = plugin;
		this.manager = plugin.getMenuManager();
		this.player = player;
		this.inventory = Bukkit.createInventory(player, rows * 9, Text.color(title));
	}

	public Gui(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull InventoryType type, @Nonnull String title) {
		this.plugin = plugin;
		this.manager = plugin.getMenuManager();
		this.player = player;
		this.inventory = Bukkit.createInventory(player, type, title);
	}

	/**
	 * Runs when the menu is opened.
	 * This method is called once after the {@link Gui} is fully opened and all items are placed.
	 */
	protected void onOpen() {}

	/**
	 * Runs when the menu is closed.
	 * This method is called once after the {@link Gui} is fully closed, cleared, and invalidated.
	 */
	protected void onClose() {}

	/**
	 * Draws the {@link Gui} by placing all the items in the slots.
	 * This method is called when {@link Gui#open()} is run.
	 */
	protected abstract void draw();

	/**
	 * Opens the {@link Gui}.
	 * To ensure it is properly loaded, there is a 1 tick delay in the opening.
	 */
	public final void open() {
		Preconditions.checkArgument(isValid(), "Gui has already been opened.");
		draw();

		// Delay the opening by 1 tick to ensure inventory is ready.
		Scheduler.sync().run(() -> {
			manager.addMenu(player, this);
			player.openInventory(getInventory());
			onOpen();
		});
	}

	/**
	 * Closes the {@link Gui}.
	 */
	public final void close() {
		getPlayer().closeInventory();
	}

	/**
	 * Gets the player viewing the {@link Gui}.
	 *
	 * @return the player
	 */
	@Nonnull
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the inventory that the {@link Gui} is representing.
	 *
	 * @return the inventory
	 */
	@Nonnull
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Gets the {@link Slot} that corresponds to the slot number.
	 *
	 * @param slot the slot number
	 * @return the slot object
	 */
	@Nonnull
	public Slot getSlot(int slot) {
		return slots.computeIfAbsent(slot, i -> new SimpleSlot(this, i));
	}

	/**
	 * Gets whether the {@link Gui} is valid.
	 *
	 * @return true if valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Invalidates a {@link Gui}.
	 * This will prevent the menu from being interacted with further.
	 */
	public void invalidate() {
		if (!valid) {
			return;
		}

		valid = false;
		manager.removeMenu(player);

		// Wipe out the inventory to ensure it cannot be interacted with further.
		inventory.clear();
		slots.values().forEach(Slot::clearBindings);
		close();
		onClose();
	}
}
