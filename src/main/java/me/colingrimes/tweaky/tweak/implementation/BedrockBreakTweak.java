package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BedrockBreakTweak extends Tweak {

	private final Map<Block, DamagedBlock> damagedBlocks = new HashMap<>();
	private BukkitTask task;

	public BedrockBreakTweak(@Nonnull Tweaky plugin) {
		super(plugin, "bedrock_break");
	}

	@Override
	public boolean isEnabled() {
		return false; // doesn't work right now
	}

	@Override
	public void init() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> new HashSet<>(damagedBlocks.values()).forEach(this::tick), 2L, 2L);
	}

	@Override
	public void shutdown() {
		task.cancel();
		damagedBlocks.values().forEach(d -> setDestroyStage(d, 0));
	}

	@EventHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		if (event.getBlock().getType() == Material.BEDROCK) {
			event.getBlock().setType(Material.AIR);
			Bukkit.getScheduler().runTaskLater(plugin, () -> event.getBlock().setType(Material.BEDROCK), 10L);
			damagedBlocks.computeIfAbsent(event.getBlock(), DamagedBlock::new);
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockDamageAbort(@Nonnull BlockDamageAbortEvent event) {
		DamagedBlock damagedBlock = damagedBlocks.get(event.getBlock());
		if (damagedBlock != null) {
			setDestroyStage(damagedBlock, 0);
			event.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
	}

	private void tick(@Nonnull DamagedBlock damagedBlock) {
		if (damagedBlock.block.getType() != Material.BEDROCK) {
			setDestroyStage(damagedBlock, 0);
			return;
		}

		// Slowly destroy the block in even increments.
		int breakTime = 60; // settings.TWEAK_BEDROCK_BREAK_SECONDS.get();
		double interval = breakTime / 10.0; // interval for each destroy stage
		double secondsPassed = Duration.between(damagedBlock.startTime, Instant.now()).toMillis() / 1000.0;
		int destroyStage = (int) (secondsPassed / interval) + 1;

		// Only update destroy stage if it has changed.
		if (damagedBlock.destroyStage != destroyStage) {
			setDestroyStage(damagedBlock, destroyStage);
		}
	}

	/**
	 * Sets the block's destroy stage for the specified player.
	 *
	 * @param damagedBlock the block to damage
	 * @param destroyStage the destroy stage of the block (1-10, 0 will reset, >10 will break)
	 */
	private void setDestroyStage(@Nonnull DamagedBlock damagedBlock, int destroyStage) {
		damagedBlock.destroyStage = destroyStage;

		if (destroyStage <= 10) {
			Players.forEach(p -> p.sendBlockDamage(damagedBlock.block.getLocation(), (float) (destroyStage / 10.0), p));
		} else {
			Blocks.destroy(damagedBlock.block);
		}

		// Remove the damaged block if it is reset.
		if (destroyStage == 0 || destroyStage > 10) {
			damagedBlocks.remove(damagedBlock.block);
		}
	}

	private static class DamagedBlock {
		private final Instant startTime = Instant.now();
		private final Block block;
		private int destroyStage = 0; // 1-10 for block destroy stages

		public DamagedBlock(@Nonnull Block block) {
			this.block = block;
		}
	}
}
