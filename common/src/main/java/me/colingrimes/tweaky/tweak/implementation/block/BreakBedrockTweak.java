package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BreakBedrockTweak extends DefaultTweak {

	private final Map<Player, DamagedBlock> damagedBlocks = new HashMap<>();
	private Task task;

	public BreakBedrockTweak(@Nonnull Tweaky plugin) {
		super(plugin, "break_bedrock");
	}

	@Override
	protected void onEnable() {
		task = Scheduler.sync().runRepeating(() -> damagedBlocks.entrySet().removeIf(e -> e.getKey() == null || !e.getKey().isValid() || tick(e.getValue())), 2L, 2L);
	}

	@Override
	protected void onDisable() {
		task.stop();
		damagedBlocks.values().forEach(d -> setDestroyStage(d, 0));
		damagedBlocks.clear();
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(Material.NETHERITE_PICKAXE)
				.item(i -> i.getEnchantmentLevel(Enchantment.EFFICIENCY) >= 5)
				.block(Material.BEDROCK);
	}

	@TweakHandler
	public void onBlockDamage(@Nonnull BlockDamageEvent event) {
		PotionEffect potion = event.getPlayer().getPotionEffect(PotionEffectType.HASTE);
		if (potion != null && potion.getAmplifier() >= 1) {
			damagedBlocks.computeIfAbsent(event.getPlayer(), k -> new DamagedBlock(k, event.getBlock()));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockDamageAbort(@Nonnull BlockDamageAbortEvent event) {
		DamagedBlock damagedBlock = damagedBlocks.get(event.getPlayer());
		if (damagedBlock != null && setDestroyStage(damagedBlock, 0)) {
			damagedBlocks.remove(event.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerQuit(@Nonnull PlayerQuitEvent event) {
		DamagedBlock damagedBlock = damagedBlocks.get(event.getPlayer());
		if (damagedBlock != null && setDestroyStage(damagedBlock, 0)) {
			damagedBlocks.remove(event.getPlayer());
		}
	}

	private boolean tick(@Nonnull DamagedBlock damagedBlock) {
		// Slowly destroy the block in even increments.
		int breakTime = settings.TWEAK_BREAK_BEDROCK_SECONDS.get();
		double interval = breakTime / 10.0; // interval for each destroy stage
		double secondsPassed = Duration.between(damagedBlock.startTime, Instant.now()).toMillis() / 1000.0;
		int destroyStage = (int) (secondsPassed / interval) + 1;

		// Only update destroy stage if it has changed.
		if (damagedBlock.destroyStage != destroyStage) {
			return setDestroyStage(damagedBlock, destroyStage);
		}

		return false;
	}

	/**
	 * Sets the block's destroy stage for the specified player.
	 *
	 * @param damagedBlock the block to damage
	 * @param destroyStage the destroy stage of the block (1-10, 0 will reset, >10 will break)
	 * @return true if the block is done being destroyed
	 */
	private boolean setDestroyStage(@Nonnull DamagedBlock damagedBlock, int destroyStage) {
		damagedBlock.destroyStage = destroyStage;

		if (destroyStage <= 10) {
			Util.nearby(Player.class, damagedBlock.block.getLocation(), 10).forEach(p -> p.sendBlockDamage(damagedBlock.block.getLocation(), (float) (destroyStage / 10.0), damagedBlock.id));
		} else if (damagedBlock.player.breakBlock(damagedBlock.block)) {
			Blocks.breakSound(damagedBlock.block);
		} else {
			Util.nearby(Player.class, damagedBlock.block.getLocation(), 10).forEach(p -> p.sendBlockDamage(damagedBlock.block.getLocation(), 0f, damagedBlock.id));
		}

		// Remove the damaged block if it is reset.
		return destroyStage == 0 || destroyStage > 10;
	}

	private static class DamagedBlock {

		private final int id = new Random().nextInt();
		private final Instant startTime = Instant.now();
		private final Player player;
		private final Block block;
		private int destroyStage = 0; // 1-10 for block destroy stages

		public DamagedBlock(@Nonnull Player player, @Nonnull Block block) {
			this.player = player;
			this.block = block;
		}
	}
}
