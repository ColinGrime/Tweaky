package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class CauldronMudTweak extends Tweak {

	private final Map<UUID, Instant> items = new HashMap<>();
	private BukkitTask task;

	public CauldronMudTweak(@Nonnull Tweaky plugin) {
		super(plugin, "cauldron_mud");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CAULDRON_MUD.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_CAULDRON_MUD.get().material(Material.MUD);
	}

	@Override
	public void init() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			Instant now = Instant.now();
			var iterator = items.entrySet().iterator();
			while (iterator.hasNext()) {
				var entry = iterator.next();
				if (Duration.between(now, entry.getValue()).isNegative()) {
					iterator.remove();
					continue;
				}

				Entity entity = Bukkit.getEntity(entry.getKey());
				if (tick(entity)) {
					iterator.remove();
				}
			}
		}, 1L, 1L);
	}

	@Override
	public void shutdown() {
		items.clear();
		task.cancel();
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

		Item dirt = event.getItemDrop();
		if (dirt.getItemStack().getType() == Material.DIRT) {
			items.put(dirt.getUniqueId(), Instant.now().plusSeconds(3));
		}
	}

	private boolean tick(@Nullable Entity entity) {
		if (!(entity instanceof Item item) || !entity.isValid()) {
			return true;
		}

		Location location = item.getLocation();
		Block block = location.getBlock();
		if (block.getType() != Material.WATER_CAULDRON || !(block.getBlockData() instanceof Levelled cauldron)) {
			return false;
		}

		Sounds.play(block, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON);
		item.getWorld().dropItem(location, new ItemStack(Material.MUD, item.getItemStack().getAmount()));
		item.remove();

		if (settings.TWEAK_CAULDRON_CONCRETE_USE_WATER.get()) {
			if (cauldron.getLevel() > 1) {
				Blocks.edit(block, Levelled.class, l -> l.setLevel(l.getLevel() - 1));
			} else {
				block.setType(Material.CAULDRON);
			}
		}

		return true;
	}
}
