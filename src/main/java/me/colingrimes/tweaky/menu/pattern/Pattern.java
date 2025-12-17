package me.colingrimes.tweaky.menu.pattern;

import com.google.common.base.Preconditions;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.Slot;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;

/**
 * Represents a pattern of slots in a {@link Gui} that can be filled with an item.
 */
public class Pattern {

    public static final Pattern BORDER = Pattern.create()
            .mask("111111111")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("100000001")
            .mask("111111111");

    private final List<String> patterns;
    private final Map<ClickType, Set<Consumer<InventoryClickEvent>>> handlers;
    private ItemStack item;

    /**
     * Creates a new Pattern instance.
     *
     * @return the new Pattern instance
     */
    @Nonnull
    public static Pattern create() {
        return new Pattern();
    }

    /**
     * Creates a new Pattern instance with the given item.
     *
     * @param item the item to place in the slots represented by '1' in the pattern
     * @return the new Pattern instance
     */
    @Nonnull
    public static Pattern of(@Nonnull ItemStack item) {
        return create().item(item);
    }

    /**
     * Creates a new Pattern instance with the given material.
     *
     * @param material the material to place in the slots represented by '1' in the pattern
     * @return the new Pattern instance
     */
    @Nonnull
    public static Pattern of(@Nonnull Material material) {
        return create().item(Items.of(material).name("").build());
    }

    private Pattern() {
        this.patterns = new ArrayList<>();
        this.handlers = new HashMap<>();
    }

    /**
     * Adds a new mask to the pattern.
     *
     * @param mask the pattern mask, using '0' for empty slots and '1' for slots to be filled
     * @return this Pattern instance for chaining
     * @throws IllegalArgumentException if the mask is too large, or it contains characters other than '0' and '1'
     * @throws IllegalStateException if more than 6 lines have been added to the pattern
     */
    @Nonnull
    public Pattern mask(@Nonnull String mask) {
        Preconditions.checkArgument(mask.length() <= 9, "Mask is too large to fit in the Gui.");
        Preconditions.checkArgument(mask.chars().allMatch(c -> c == '0' || c == '1'), "Mask can only contain '0' and '1' characters.");
        Preconditions.checkState(patterns.size() < 6, "Pattern cannot have more than 6 lines.");
        patterns.add(mask);
        return this;
    }

    /**
     * Sets the item to be placed in the slots represented by '1' in the pattern.
     *
     * @param item the item to place in the slots
     * @return this Pattern instance for chaining
     */
    @Nonnull
    public Pattern item(@Nonnull ItemStack item) {
        this.item = item;
        return this;
    }

    /**
     * Sets the material to be placed in the slots represented by '1' in the pattern.
     *
     * @param material the material to place in the slots
     * @return this Pattern instance for chaining
     */
    @Nonnull
    public Pattern item(@Nonnull Material material) {
        return item(Items.of(material).name("").build());
    }

    /**
     * Binds a click type to a handler that will perform an action on click.
     * The handler will be applied to all filled slots.
     *
     * @param type the click type
     * @param handler the action to perform on the click
     * @return this Pattern instance for chaining
     */
    @Nonnull
    public Pattern bind(@Nonnull ClickType type, @Nonnull Consumer<InventoryClickEvent> handler) {
        handlers.computeIfAbsent(type, __ -> new HashSet<>()).add(handler);
        return this;
    }

    /**
     * Fills the slots in the {@link Gui} based on the current pattern and item.
     *
     * @param gui the {@link Gui} in which to fill the slots
     */
    public void fill(@Nonnull Gui gui) {
        if (item == null) {
            item = Items.of(Material.STONE).name("&cNo item mapped.").build();
        }

        for (int i=0; i<patterns.size(); i++) {
            String row = patterns.get(i);
            for (int j=0; j<row.length(); j++) {
                if (row.charAt(j) == '1') {
                    Slot slot = gui.getSlot(i * 9 + j);
                    slot.setItem(item);
                    handlers.forEach((type, handlers) -> handlers.forEach(h -> slot.bind(type, h)));
                }
            }
        }
    }
}
