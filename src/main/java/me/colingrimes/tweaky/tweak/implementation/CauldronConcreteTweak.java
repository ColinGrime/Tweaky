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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CauldronConcreteTweak extends Tweak {

	private static final Map<Material, Material> POWDER_TO_CONCRETE = new HashMap<>();

	static {
		POWDER_TO_CONCRETE.put(Material.WHITE_CONCRETE_POWDER,      Material.WHITE_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.LIGHT_GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.GRAY_CONCRETE_POWDER,       Material.GRAY_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.BLACK_CONCRETE_POWDER,      Material.BLACK_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.BROWN_CONCRETE_POWDER,      Material.BROWN_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.RED_CONCRETE_POWDER,        Material.RED_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.ORANGE_CONCRETE_POWDER,     Material.ORANGE_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.YELLOW_CONCRETE_POWDER,     Material.YELLOW_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.LIME_CONCRETE_POWDER,       Material.LIME_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.GREEN_CONCRETE_POWDER,      Material.GREEN_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.CYAN_CONCRETE_POWDER,       Material.CYAN_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.BLUE_CONCRETE_POWDER,       Material.BLUE_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.PURPLE_CONCRETE_POWDER,     Material.PURPLE_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.MAGENTA_CONCRETE_POWDER,    Material.MAGENTA_CONCRETE);
		POWDER_TO_CONCRETE.put(Material.PINK_CONCRETE_POWDER,       Material.PINK_CONCRETE);
	}

	private final Map<UUID, Instant> items = new HashMap<>();
	private BukkitTask task;

	public CauldronConcreteTweak(@Nonnull Tweaky plugin) {
		super(plugin, "cauldron_concrete");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CAULDRON_CONCRETE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_CAULDRON_CONCRETE.get().material(Material.WHITE_CONCRETE);
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

		Item powder = event.getItemDrop();
		Material concrete = POWDER_TO_CONCRETE.get(powder.getItemStack().getType());
		if (concrete != null) {
			items.put(powder.getUniqueId(), Instant.now().plusSeconds(3));
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

		Material concrete = POWDER_TO_CONCRETE.get(item.getItemStack().getType());
		item.getWorld().dropItem(location, new ItemStack(concrete, item.getItemStack().getAmount()));
		item.remove();
		Sounds.play(block, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON);

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
