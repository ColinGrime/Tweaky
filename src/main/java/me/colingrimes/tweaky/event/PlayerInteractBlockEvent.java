package me.colingrimes.tweaky.event;

import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * A custom event that simplifies the handling of {@link PlayerInteractEvent} when dealing with block interactions.
 * Only fires for {@link org.bukkit.inventory.EquipmentSlot#HAND}.
 */
public class PlayerInteractBlockEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final PlayerInteractEvent event;
    private final Player player;
    private final ItemStack item;
    private final Block block;
    private final BlockFace blockFace;
    private final Action action;

    /**
     * Constructs a new {@link PlayerInteractBlockEvent} from a {@link PlayerInteractEvent}.
     *
     * @param event the player interact block event
     */
    public PlayerInteractBlockEvent(@Nonnull PlayerInteractEvent event) {
        this.event = event;
        this.player = event.getPlayer();
        this.item = event.getItem() != null ? event.getItem() : player.getInventory().getItemInMainHand();
        this.block = event.getClickedBlock();
        this.blockFace = event.getBlockFace();
        this.action = event.getAction();
    }

    /**
     * Gets the player who interacted.
     *
     * @return the player
     */
    @Nonnull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's inventory who interacted.
     *
     * @return the player inventory
     */
    @Nonnull
    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    /**
     * Gets the item held by the player when they interacted.
     *
     * @return the held item
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the item type held by the player when they interacted.
     *
     * @return the held item type
     */
    @Nonnull
    public Material getItemType() {
        return item.getType();
    }

    /**
     * Gets the clicked block.
     *
     * @return the clicked block
     */
    @Nonnull
    public Block getBlock() {
        return block;
    }

    /**
     * Gets the clicked block type.
     *
     * @return the clicked block ype
     */
    @Nonnull
    public Material getBlockType() {
        return block.getType();
    }

    /**
     * Gets the face of the block that was clicked.
     *
     * @return the clicked block face
     */
    @Nonnull
    public BlockFace getBlockFace() {
        return blockFace;
    }


    /**
     * Gets the clicked block location.
     *
     * @return the clicked block location
     */
    @Nonnull
    public Location getLocation() {
        return block.getLocation().clone();
    }

    /**
     * Checks if the player left-clicked the block.
     *
     * @return true if the player left-clicked the block
     */
    public boolean isLeftClick() {
        return action == Action.LEFT_CLICK_BLOCK;
    }

    /**
     * Checks if the player shift-left-clicked the block.
     *
     * @return true if the player shift-left-clicked the block
     */
    public boolean isShiftLeftClick() {
        return isLeftClick() && player.isSneaking();
    }

    /**
     * Checks if the player right-clicked the block.
     *
     * @return true if the player right-clicked the block
     */
    public boolean isRightClick() {
        return action == Action.RIGHT_CLICK_BLOCK;
    }

    /**
     * Checks if the player shift-right-clicked the block.
     *
     * @return true if the player shift-right-clicked the block
     */
    public boolean isShiftRightClick() {
        return isRightClick() && player.isSneaking();
    }

    /**
     * Checks if the held item matches one of the given materials.
     *
     * @param materials materials to check
     * @return true if the held item matches one of the materials
     */
    public boolean isItem(@Nonnull Material...materials) {
        return Arrays.stream(materials).anyMatch(m -> m == item.getType());
    }

    /**
     * Checks if the held item matches one of the given tags.
     *
     * @param tags the tags to check
     * @return true if the held item matches one of the tags
     */
    @SafeVarargs
	public final boolean isItem(@Nonnull Tag<Material>... tags) {
        return Arrays.stream(tags).anyMatch(t -> t.isTagged(item.getType()));
    }

    /**
     * Checks if the clicked block matches one of the given materials.
     *
     * @param materials materials to check
     * @return true if the block matches one of the materials
     */
    public boolean isBlock(@Nonnull Material...materials) {
        return Arrays.stream(materials).anyMatch(m -> m == block.getType());
    }

    /**
     * Checks if the clicked block matches one of the given tags.
     *
     * @param tags the tags to check
     * @return true if the block matches one of the tags
     */
    @SafeVarargs
    public final boolean isBlock(@Nonnull Tag<Material>... tags) {
        return Arrays.stream(tags).anyMatch(t -> t.isTagged(block.getType()));
    }

    /**
     * Checks if the player can build on the block.
     *
     * @return true if the player can build on the block.
     */
    public boolean canBuild() {
        return Players.canBuild(player, block);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    @Nonnull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Nonnull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}