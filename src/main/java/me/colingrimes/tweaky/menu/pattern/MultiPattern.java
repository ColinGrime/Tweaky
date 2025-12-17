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
 * Represents a pattern of slots in a {@link Gui} that can be filled with different items.
 */
public class MultiPattern {

    public static final MultiPattern TWO_CHOICES = MultiPattern.create()
            .mask("LLLLMRRRR")
            .mask("LLLLMRRRR")
            .mask("LLLLMRRRR")
            .mask("LLLLMRRRR");

    private final List<String> patterns;
    private final Map<Character, ItemStack> items;
    private final Map<Character, Map<ClickType, Set<Consumer<InventoryClickEvent>>>> handlers;

    /**
     * Creates a new MultiPattern instance.
     *
     * @return the new MultiPattern instance
     */
    @Nonnull
    public static MultiPattern create() {
        return new MultiPattern();
    }

    private MultiPattern() {
        this.patterns = new ArrayList<>();
        this.items = new HashMap<>();
        this.handlers = new HashMap<>();
    }

    /**
     * Adds a new mask to the pattern.
     *
     * @param mask the pattern mask, using '0' for empty slots and any other character for slots to be filled
     * @return this MultiPattern instance for chaining
     * @throws IllegalArgumentException if the mask is too large
     * @throws IllegalStateException if more than 6 lines have been added to the pattern
     */
    @Nonnull
    public MultiPattern mask(@Nonnull String mask) {
        Preconditions.checkArgument(mask.length() <= 9, "Mask is too large to fit in the Gui.");
        Preconditions.checkState(patterns.size() < 6, "MultiPattern cannot have more than 6 lines.");
        patterns.add(mask);
        return this;
    }

    /**
     * Maps a character in the pattern to the given item.
     *
     * @param c the character to map to the item
     * @param item the item to place in the slots represented by the character in the pattern
     * @return this MultiPattern instance for chaining
     * @throws IllegalArgumentException if the character is a reserved character ('0')
     */
    @Nonnull
    public MultiPattern item(char c, @Nonnull ItemStack item) {
        Preconditions.checkArgument(c != '0', "Character '0' is reserved for empty slots and cannot be mapped to an item.");
        items.put(c, item);
        return this;
    }

    /**
     * Maps a character in the pattern to the given material.
     *
     * @param c the character to map to the material
     * @param material the material to place in the slots represented by the character in the pattern
     * @return this MultiPattern instance for chaining
     */
    @Nonnull
    public MultiPattern item(char c, @Nonnull Material material) {
        return item(c, Items.of(material).name("").build());
    }

    /**
     * Binds a click type to a handler that will perform an action on click.
     * The handler will be applied to the specified character.
     *
     * @param c the character to map to the handler
     * @param type the click type
     * @param handler the action to perform on the click
     * @return this MultiPattern instance for chaining
     */
    @Nonnull
    public MultiPattern bind(char c, @Nonnull ClickType type, @Nonnull Consumer<InventoryClickEvent> handler) {
        Map<ClickType, Set<Consumer<InventoryClickEvent>>> handlers = this.handlers.computeIfAbsent(c, __ -> new HashMap<>());
        handlers.computeIfAbsent(type, __ -> new HashSet<>()).add(handler);
        return this;
    }

    /**
     * Fills the slots in the {@link Gui} based on the current pattern and mapped items.
     *
     * @param gui the {@link Gui} in which to fill the slots
     */
    public void fill(@Nonnull Gui gui) {
        for (int i=0; i<patterns.size(); i++) {
            String row = patterns.get(i);
            for (int j=0; j<row.length(); j++) {
                char c = row.charAt(j);
                if (c != '0') {
                    Slot slot = gui.getSlot(i * 9 + j);
                    slot.setItem(items.computeIfAbsent(c, __ -> Items.of(Material.STONE).name("&cNo item mapped.").build()));

                    if (handlers.get(c) != null) {
                        handlers.get(c).forEach((type, handlers) -> handlers.forEach(h -> slot.bind(type, h)));
                    }
                }
            }
        }
    }
}
