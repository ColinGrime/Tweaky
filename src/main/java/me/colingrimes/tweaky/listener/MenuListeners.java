package me.colingrimes.tweaky.listener;

import me.colingrimes.tweaky.menu.Gui;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;

public class MenuListeners implements Listener {

    @EventHandler
    public void onInventoryClick(@Nonnull InventoryClickEvent event) {
        // Allow editing your own inventory while having a custom one open.
        Inventory clicked = event.getClickedInventory();
        if (clicked == null || (clicked.getType() == InventoryType.PLAYER && event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Gui gui = Gui.players.get(player);
        if (gui == null) {
            return;
        }

        event.setCancelled(true);
        if (gui.isValid()) {
            gui.getSlot(event.getRawSlot()).handle(event);
        } else {
            gui.close();
        }
    }

    @EventHandler
    public void onInventoryDrag(@Nonnull InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        Gui gui = Gui.players.get(player);
        boolean playerInventoryDrag = event.getRawSlots().stream().allMatch(slot -> slot >= event.getInventory().getSize());
        if (gui == null || playerInventoryDrag) {
            return;
        }

        event.setCancelled(true);
        if (!gui.isValid()) {
            gui.close();
        }
    }

    @EventHandler
    public void onInventoryClose(@Nonnull InventoryCloseEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
        invalidate(event.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(@Nonnull PlayerChangedWorldEvent event) {
        invalidate(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(@Nonnull PlayerTeleportEvent event) {
        invalidate(event.getPlayer());
    }

    /**
     * Invalidates a valid {@link Gui}.
     *
     * @param humanEntity the entity
     */
    private void invalidate(@Nonnull HumanEntity humanEntity) {
        if (!(humanEntity instanceof Player player)) {
            return;
        }

        Gui gui = Gui.players.get(player);
        if (gui != null) {
            gui.invalidate();
        }
    }
}