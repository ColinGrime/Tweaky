package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;

public class LadderPlacementTweak extends Tweak {

	public LadderPlacementTweak(@Nonnull Tweaky plugin) {
		super(plugin, "ladder_placement");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_LADDER_PLACEMENT.get();
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Block block = event.getBlock();
		if (!event.isRightClick() || !event.isItem(Material.LADDER) || !(block.getBlockData() instanceof Ladder ladder) || !event.canBuild()) {
			return;
		}

		Vector direction = event.getPlayer().getLocation().getPitch() <= 0 ? new Vector(0, 1, 0) : new Vector(0, -1, 0);
		while (block.getType() == Material.LADDER) {
			block = block.getLocation().add(direction).getBlock();
		}

		place(event.getItem(), block, ladder.getFacing());
		event.setCancelled(true);
	}

	/**
	 * Places ladders in the player's hand starting at the specified location.
	 *
	 * @param hand the player's hand (ladders)
	 * @param block the starting block
	 * @param ladderFacing the face of the ladder
	 */
	private void place(@Nonnull ItemStack hand, @Nonnull Block block, @Nonnull BlockFace ladderFacing) {
		if (!canBePlacedOn(block, block.getRelative(ladderFacing.getOppositeFace()), ladderFacing)) {
			return;
		}

		block.setType(Material.LADDER);
		Blocks.placeSound(block);
		Blocks.edit(block, Ladder.class, l -> l.setFacing(ladderFacing));
		Items.remove(hand);
	}

	/**
	 * Checks if a ladder can be placed on the block.
	 *
	 * @param block the block where the ladder will go
	 * @param clickedBlock the block that was clicked
	 * @param ladderFacing the face of the ladder
	 * @return true if the ladder can be placed on it
	 */
	private boolean canBePlacedOn(@Nonnull Block block, @Nonnull Block clickedBlock, @Nonnull BlockFace ladderFacing) {
		boolean validBlock = block.getType().isAir() || block.isLiquid() || Blocks.isPlant(block.getType());
		if (!validBlock || !clickedBlock.getType().isSolid() || !clickedBlock.getBlockData().isFaceSturdy(ladderFacing, BlockSupport.FULL)) {
			return false;
		}

		return switch(ladderFacing) {
			case NORTH, EAST, SOUTH, WEST -> true;
			default -> false;
		};
	}
}
