package me.colingrimes.tweaky.menu.paginated;

import me.colingrimes.tweaky.menu.Gui;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * The PaginatedGui abstract class.
 * Extend this class to create multipage GUI menus.
 */
public abstract class PaginatedGui extends Gui {

    private final Map<Integer, PaginatedGui> pages;
    private final int pageNumber;

    /**
     * Constructs a PaginatedGui instance.
     *
     * @param player     the player who will view the GUI
     * @param title      the title of the GUI
     * @param rows       the number of rows in the GUI
     * @param pageNumber the page number of this GUI
     */
    public PaginatedGui(@Nonnull Player player, @Nonnull String title, int rows, int pageNumber) {
        super(player, title, rows);
        this.pageNumber = pageNumber;
        this.pages = new HashMap<>();
    }

    /**
     * Returns the page number of this PaginatedGui.
     *
     * @return the page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Gets the {@link PaginatedGui} for the specified page number.
     *
     * @param pageNumber the page number
     * @return the gui for the specified page number
     */
    @Nonnull
    public PaginatedGui getPage(int pageNumber) {
        return pages.get(pageNumber);
    }

    /**
     * Adds a {@link PaginatedGui} for the specified page number.
     *
     * @param pageNumber the page number
     * @param gui        the gui for the specified page number
     */
    public void addPage(int pageNumber, @Nonnull PaginatedGui gui) {
        pages.put(pageNumber, gui);
    }

    /**
     * Opens the next page.
     */
    public void nextPage() {
        PaginatedGui nextPage = pages.get(pageNumber + 1);
        if (nextPage != null) {
            nextPage.open();
        }
    }

    /**
     * Opens the previous page.
     */
    public void previousPage() {
        PaginatedGui previousPage = pages.get(pageNumber - 1);
        if (previousPage != null) {
            previousPage.open();
        }
    }
}
