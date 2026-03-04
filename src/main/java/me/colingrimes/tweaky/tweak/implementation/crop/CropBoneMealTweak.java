package me.colingrimes.tweaky.tweak.implementation.crop;

import com.google.common.base.Preconditions;
import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;

/**
 * Allows you to bone meal a few additional crops:
 * <li>Nether Wart</li>
 * <li>Sugar Cane</li>
 * <li>Cactus</li>
 */
public class CropBoneMealTweak extends DefaultTweak {

	public CropBoneMealTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_bone_meal");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.rightClick()
				.item(Material.BONE_MEAL)
				.anyBlock(Material.SUGAR_CANE, Material.CACTUS, Material.NETHER_WART);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		Block block = Preconditions.checkNotNull(event.getClickedBlock(), "Clicked block is null.");
		EquipmentSlot hand = Preconditions.checkNotNull(event.getHand(), "Hand is null.");
		if (handleHeightCrops(block) || handleNetherWart(block)) {
			Players.use(event.getPlayer(), hand, Sound.ITEM_BONE_MEAL_USE, block.getLocation());
			event.setCancelled(true);
		}
	}

	/**
	 * Handles the growing of height crops.
	 *
	 * @param block the block to handle
	 * @return true if the crop grew upwards
	 */
	private boolean handleHeightCrops(@Nonnull Block block) {
		Material type = block.getType();
		if (type != Material.SUGAR_CANE && type != Material.CACTUS) {
			return false;
		}

		Block top = block.getLocation().clone().add(0, 1, 0).getBlock();
		while (top.getType() == type) {
			top = top.getLocation().add(0, 1, 0).getBlock();
		}

		if (top.getY() >= top.getWorld().getMaxHeight() || !top.getType().isAir()) {
			return false;
		}

		top.setType(type);
		return true;
	}

	/**
	 * Handles the growing of nether wart.
	 *
	 * @param block the block to handle
	 * @return true if the nether wart grew
	 */
	private boolean handleNetherWart(@Nonnull Block block) {
		if (block.getType() != Material.NETHER_WART || !(block.getBlockData() instanceof Ageable crop) || crop.getAge() >= crop.getMaximumAge()) {
			return false;
		}

		Blocks.edit(block, Ageable.class, c -> c.setAge(c.getAge() + 1));
		return true;
	}
}
