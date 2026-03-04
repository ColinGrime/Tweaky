package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Door;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DoorDoubleTweak extends DefaultTweak {

	public DoorDoubleTweak(@Nonnull Tweaky plugin) {
		super(plugin, "doors_double");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.buildable()
				.mainHand()
				.rightClick()
				.block(Tag.DOORS)
				// Ignore iron doors if the iron door tweak is off.
				.block(b -> b.getType() != Material.IRON_DOOR || settings.TWEAK_DOORS_IRON.get());
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteractBlock(@Nonnull PlayerInteractBlockEvent event) {
		Blocks.edit(getDoubleDoor(event.getBlock()), Door.class, d -> d.setOpen(!d.isOpen()));
	}

	/**
	 * Gets the adjacent door block if available.
	 *
	 * @param doorBlock the door block
	 * @return the adjacent door (making up the double door) or null
	 */
	@Nullable
	private Block getDoubleDoor(@Nonnull Block doorBlock) {
		if (!(doorBlock.getBlockData() instanceof Door door)) {
			return null;
		}

		Block adjacent = doorBlock.getRelative(getAdjacentDoorFace(door));
		if (!(adjacent.getBlockData() instanceof Door adjacentDoor)) {
			return null;
		}

		// Verify the adjacent door lines up perfectly with the other door.
		if (door.getHinge() == adjacentDoor.getHinge() || door.getHalf() != adjacentDoor.getHalf()) {
			return null;
		}

		return adjacent;
	}

	/**
	 * Gets the {@link BlockFace} to the adjacent door.
	 *
	 * @param door the door
	 * @return the adjacent door face
	 */
	@Nonnull
	private BlockFace getAdjacentDoorFace(@Nonnull Door door) {
		BlockFace adjacent = switch (door.getFacing()) {
			case NORTH -> BlockFace.EAST;
			case EAST -> BlockFace.SOUTH;
			case SOUTH -> BlockFace.WEST;
			case WEST -> BlockFace.NORTH;
			default -> BlockFace.SELF;
		};
		return door.getHinge() == Door.Hinge.LEFT ? adjacent : adjacent.getOppositeFace();
	}
}
