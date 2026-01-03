package me.colingrimes.tweaky.menu;

import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Represents a single slot in a {@link Gui}.
 */
public interface Slot {

	/**
	 * Handles all click events on the slot.
	 *
	 * @param event inventory click event
	 */
	void handle(@Nonnull InventoryClickEvent event);

	/**
	 * Gets the {@link Gui} of the slot.
	 *
	 * @return the gui of the slot
	 */
	@Nonnull
	Gui getGui();

	/**
	 * Gets the ID number of the slot.
	 * <p>
	 * This is the slot number that corresponds to its
	 * placement in the {@link Gui} that it is associated with.
	 *
	 * @return the id of the slot
	 */
	int getId();

	/**
	 * Gets the item of the slot.
	 *
	 * @return the item of the slot
	 */
	@Nullable
	ItemStack getItem();

	/**
	 * Sets the item of the slot.
	 *
	 * @param item the item of the slot
	 * @return the slot object
	 */
	@Nonnull
	Slot setItem(@Nullable ItemStack item);

	/**
	 * Sets an empty item of the slot.
	 *
	 * @param material the material of the item
	 * @return the slot object
	 */
	@Nonnull
	default Slot setItem(@Nonnull Material material) {
		return setItem(Items.of(material).name("").build());
	}

	/**
	 * Binds a click type to a handler that will perform an action on click.
	 *
	 * @param type the click type
	 * @param handler the action to perform on the click
	 * @return the slot object
	 */
	@Nonnull
	Slot bind(@Nonnull ClickType type, @Nonnull Consumer<InventoryClickEvent> handler);

	/**
	 * Binds a click type to a handler that will perform an action on click.
	 *
	 * @param handler the action to perform on the click
	 * @param types the click types
	 * @return the slot object
	 */
	@Nonnull
	default Slot bind(@Nonnull Consumer<InventoryClickEvent> handler, @Nonnull ClickType... types) {
		Arrays.stream(types).forEach(t -> bind(t, handler));
		return this;
	}

	/**
	 * Clears all bindings of all click types.
	 */
	void clearBindings();
}
