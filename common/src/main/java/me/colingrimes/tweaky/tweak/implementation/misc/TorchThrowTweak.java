package me.colingrimes.tweaky.tweak.implementation.misc;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Events;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockSupport;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class TorchThrowTweak extends DefaultTweak {

	public TorchThrowTweak(@Nonnull Tweaky plugin) {
		super(plugin, "torch_throw");
	}

	@TweakHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (event.getItemDrop().getItemStack().getType() == Material.TORCH) {
			Scheduler.sync().runRepeating((task) -> checkTorch(task, event.getItemDrop(), event.getPlayer()), 1L, 1L);
		}
	}

	/**
	 * Checks the torch to see if it should be placed.
	 *
	 * @param task the torch task
	 * @param item the torch item
	 * @param player the player who threw the torch
	 */
	private void checkTorch(@Nonnull Task task, @Nonnull Item item, @Nonnull Player player) {
		if (!item.isValid()) {
			task.stop();
			return;
		}

		Block below = item.getLocation().clone().add(0, -1, 0).getBlock();
		boolean readyToPlace = Math.abs(item.getVelocity().getY()) < 0.01 && below.getType().isSolid() && below.getBlockData().isFaceSturdy(BlockFace.UP, BlockSupport.CENTER);
		if (!readyToPlace) {
			return;
		}

		task.stop();

		Block block = item.getLocation().getBlock();
		if (!block.getType().isAir() || !Events.canPlace(player, block, block.getRelative(BlockFace.DOWN))) {
			return;
		}

		Blocks.placeSound(block);
		block.setType(Material.TORCH);

		if (item.getItemStack().getAmount() > 1) {
			ItemStack itemStack = item.getItemStack();
			itemStack.setAmount(itemStack.getAmount() - 1);
			item.setItemStack(itemStack);
		} else {
			item.remove();
		}
	}
}
