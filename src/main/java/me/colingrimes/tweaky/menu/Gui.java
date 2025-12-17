package me.colingrimes.tweaky.menu;

import com.google.common.base.Preconditions;
import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.slot.SimpleSlot;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@link Gui} menu in the game.
 * Extend this class if you want basic menu functionalities.
 */
public abstract class Gui {

	/**
	 * Players currently viewing a {@link Gui} instance.
	 */
	public static final Map<Player, Gui> players = new HashMap<>();

	protected final Player player;
	protected final Inventory inventory;
	private final Map<Integer, Slot> slots = new HashMap<>();
	private boolean valid = true;

	public Gui(@Nonnull Player player, @Nonnull String title, int rows) {
		this.player = player;
		this.inventory = Bukkit.createInventory(player, rows * 9, Text.color(title));
	}

	/**
	 * Draws the {@link Gui} by placing all the items in the slots.
	 * This method is also called when {@link Gui#open()} is run.
	 */
	public abstract void draw();

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
	public Inventory getHandle() {
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
		Preconditions.checkArgument(slot >= 0 && slot < inventory.getSize(), "Invalid slot: " + slot);
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
		valid = false;
		Gui.players.remove(player);

		// Wipe out the inventory to ensure it cannot be interacted with further.
		inventory.clear();
		slots.values().forEach(Slot::clearBindings);
		close();
	}

	/**
	 * Opens the {@link Gui}.
	 * To ensure it is properly loaded, there is a 1 tick delay in the opening.
	 */
	public void open() {
		Preconditions.checkArgument(isValid(), "Gui has already been opened.");
		draw();

		// Close any other menus the player has open.
		if (Gui.players.containsKey(getPlayer())) {
			Gui.players.get(getPlayer()).close();
		}

		// Delay the opening by 1 tick to ensure inventory is ready.
		Bukkit.getScheduler().runTask(Tweaky.getInstance(), () -> {
			players.put(getPlayer(), this);
			getPlayer().openInventory(getHandle());
		});
	}

	/**
	 * Closes the {@link Gui}.
	 */
	public void close() {
		getPlayer().closeInventory();
	}
}
