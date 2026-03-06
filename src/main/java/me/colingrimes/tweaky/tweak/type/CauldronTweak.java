package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.option.Option;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Events;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * Represents a {@link Tweak} whose action involves throwing an item in a cauldron.
 */
public abstract class CauldronTweak extends DefaultTweak {

	private final Function<Material, Material> typeConverter;
	private final Option<Boolean> useWater;

	public CauldronTweak(@Nonnull Tweaky plugin, @Nonnull String id, @Nonnull Function<Material, Material> typeConverter, @Nonnull Option<Boolean> useWater) {
		super(plugin, id);
		this.typeConverter = typeConverter;
		this.useWater = useWater;
	}

	@TweakHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		Material type = typeConverter.apply(item.getItemStack().getType());
		if (type != null) {
			Scheduler.sync().runRepeating(task -> handleConversion(task, event.getPlayer(), item, type), 1L, 1L);
		}
	}

	/**
	 * Handles converting the item inside the cauldron to the new type.
	 *
	 * @param task the repeating task
	 * @param player the player
	 * @param item the item to convert
	 * @param newType the new type of the item
	 */
	private void handleConversion(@Nonnull Task task, @Nonnull Player player, @Nonnull Item item, @Nonnull Material newType) {
		if (!item.isValid()) {
			task.stop();
			return;
		}

		Block block = item.getLocation().getBlock();
		if (block.getType() != Material.WATER_CAULDRON || !(block.getBlockData() instanceof Levelled cauldron)) {
			return;
		}

		task.stop();

		// Check for permission.
		if (!Events.canInteract(player, block)) {
			return;
		}

		Sounds.play(block, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON);
		item.getWorld().dropItem(item.getLocation(), new ItemStack(newType, item.getItemStack().getAmount()));
		item.remove();

		if (useWater.get()) {
			if (cauldron.getLevel() > 1) {
				Blocks.edit(block, Levelled.class, l -> l.setLevel(l.getLevel() - 1));
			} else {
				block.setType(Material.CAULDRON);
			}
		}
	}
}
