package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import javax.annotation.Nonnull;

public class BeeCaptureTweak extends Tweak {

	public BeeCaptureTweak(@Nonnull Tweaky plugin) {
		super(plugin, "bee_capture");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_BEE_CAPTURE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.BEE_SPAWN_EGG)
				.name("&aBee Capture &8(Right Click)")
				.lore("&7Click on Bees to capture them.")
				.lore()
				.lore("&8Requires (1):")
				.lore(" &7Bee Nest")
				.lore(" &7Beehive")
				.usage("&eUsage: &aRight Click a Bee with a Bee Nest to capture it.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		if (event.getHand() != EquipmentSlot.HAND || !(event.getRightClicked() instanceof Bee bee)) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (!(item.getItemMeta() instanceof BlockStateMeta meta) || !(meta.getBlockState() instanceof Beehive beehive) || beehive.isFull()) {
			return;
		}

		beehive.addEntity(bee);
		meta.setBlockState(beehive);

		if (item.getAmount() == 1) {
			item.setItemMeta(meta);
			return;
		}

		// Handles multiple beehive items.
		ItemStack copy = item.clone();
		copy.setAmount(1);
		copy.setItemMeta(meta);
		Items.give(event.getPlayer(), copy);
		Items.remove(item);
	}
}
