package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;

public class AnvilRepairTweak extends DefaultTweak {

	public AnvilRepairTweak(@Nonnull Tweaky plugin) {
		super(plugin, "anvil_repair");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.rightClick()
				.item(Material.IRON_BLOCK)
				.anyBlock(Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		repairAnvil(event.getPlayer(), event.getHand(), event.getBlock(), ((Directional) event.getBlock().getBlockData()).getFacing());
		event.setCancelled(true);
	}

	/**
	 * Repairs the anvil by 1 stage.
	 *
	 * @param player the player repairing the anvil
	 * @param hand the hand of the player
	 * @param block the anvil block
	 * @param currFace the current face of the anvil
	 */
	private void repairAnvil(@Nonnull Player player, @Nonnull EquipmentSlot hand, @Nonnull Block block, @Nonnull BlockFace currFace) {
		block.setType(block.getType() == Material.CHIPPED_ANVIL ? Material.ANVIL : Material.CHIPPED_ANVIL);
		Blocks.edit(block, Directional.class, d -> d.setFacing(currFace));
		Players.use(player, hand, Sound.BLOCK_ANVIL_USE, block.getLocation());
	}
}
