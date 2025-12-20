package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * Allows you to bone meal a few additional crops:
 * <li>Nether Wart</li>
 * <li>Sugar Cane</li>
 * <li>Cactus</li>
 */
public class CropBoneMealTweak extends Tweak {

	public CropBoneMealTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_bone_meal");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CROPS_BONE_MEAL.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.NETHER_WART)
				.name("&aBone Meal Anything")
				.lore("&7Can be used on any Crop.")
				.usage("&eUsage: &aUse Bone Meal on any Crop to make it grow.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		EquipmentSlot hand = event.getHand();
		if (hand == null || !Players.shouldHandleHand(player, hand, i -> i.getType() == Material.BONE_MEAL)) {
			return;
		}

		Block block = event.getClickedBlock();
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || block == null || !(block.getBlockData() instanceof Ageable crop)) {
			return;
		}

		Material blockType = block.getType();
		ItemStack item = player.getInventory().getItem(hand);

		// Grows height crops.
		if (blockType == Material.SUGAR_CANE || blockType == Material.CACTUS) {
			Location location = block.getLocation().clone();
			while (location.getBlock().getType() == blockType) {
				location.add(0, 1, 0);
			}

			Block top = location.getBlock();
			if (top.getType().isAir()) {
				Sounds.play(block, Sound.ITEM_BONE_MEAL_USE);
				Items.remove(item);
				top.setType(blockType);
				event.setCancelled(true);
			}
		}

		// Grows nether wart.
		if (blockType == Material.NETHER_WART && crop.getAge() < crop.getMaximumAge()) {
			Sounds.play(block, Sound.ITEM_BONE_MEAL_USE);
			Items.remove(item);
			Blocks.edit(block, Ageable.class, c -> c.setAge(c.getAge() + 1));
			event.setCancelled(true);
		}
	}
}
