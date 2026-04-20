package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.Option;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * Represents a {@link Tweak} whose action involves throwing an item in a cauldron.
 */
public abstract class CauldronTweak extends DefaultTweak {

	private final Map<UUID, Instant> items = new HashMap<>();
	private final Function<Material, Material> typeConverter;
	private final Option<Boolean> useWater;
	private Task task;

	public CauldronTweak(@Nonnull Tweaky plugin, @Nonnull String id, @Nonnull Function<Material, Material> typeConverter, @Nonnull Option<Boolean> useWater) {
		super(plugin, id);
		this.typeConverter = typeConverter;
		this.useWater = useWater;
	}

	@Override
	protected void onEnable() {
		task = Scheduler.sync().runRepeating(() -> {
			Instant now = Instant.now();
			var iterator = items.entrySet().iterator();
			while (iterator.hasNext()) {
				var entry = iterator.next();
				if (Duration.between(now, entry.getValue()).isNegative()) {
					iterator.remove();
					continue;
				}

				Entity entity = Bukkit.getEntity(entry.getKey());
				if (handleConversion(entity)) {
					iterator.remove();
				}
			}
		}, 1L, 1L);
	}

	@Override
	protected void onDisable() {
		items.clear();
		task.stop();
	}

	@TweakHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		Item item = event.getItemDrop();
		Material type = typeConverter.apply(item.getItemStack().getType());
		if (type != null) {
			items.put(item.getUniqueId(), Instant.now().plusSeconds(3));
		}
	}

	/**
	 * Handles converting the item inside the cauldron to the new type.
	 *
	 * @param entity the entity to convert
	 * @return true if the item is done being processed
	 */
	private boolean handleConversion(@Nullable Entity entity) {
		if (!(entity instanceof Item item) || !entity.isValid()) {
			return true;
		}

		Block block = item.getLocation().getBlock();
		if (block.getType() != Material.WATER_CAULDRON || !(block.getBlockData() instanceof Levelled cauldron)) {
			return false;
		}

		Material newType = typeConverter.apply(item.getItemStack().getType());
		item.getWorld().dropItem(item.getLocation(), new ItemStack(newType, item.getItemStack().getAmount()));
		item.remove();
		Sounds.play(block, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON);

		if (useWater.get()) {
			if (cauldron.getLevel() > 1) {
				Blocks.edit(block, Levelled.class, l -> l.setLevel(l.getLevel() - 1));
			} else {
				block.setType(Material.CAULDRON);
			}
		}

		return true;
	}
}
