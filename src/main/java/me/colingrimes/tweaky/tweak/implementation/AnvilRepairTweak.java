package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class AnvilRepairTweak extends Tweak {

	public AnvilRepairTweak(@Nonnull Tweaky plugin) {
		super(plugin, "anvil_repair");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ANVIL_REPAIR.get();
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isRightClick() || !event.isItem(Material.IRON_BLOCK) || !event.isBlock(Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL)) {
			return;
		}

		Block block = event.getBlock();
		BlockFace blockFace = ((Directional) event.getBlock().getBlockData()).getFacing();
		block.setType(block.getType() == Material.CHIPPED_ANVIL ? Material.ANVIL : Material.CHIPPED_ANVIL);
		Blocks.edit(block, Directional.class, d -> d.setFacing(blockFace));

		Items.remove(event.getItem());
		Sounds.play(block, Sound.BLOCK_ANVIL_USE);
		event.setCancelled(true);
	}
}
