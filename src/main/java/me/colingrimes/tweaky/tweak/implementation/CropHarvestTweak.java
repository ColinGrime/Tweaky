package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class CropHarvestTweak extends Tweak {

	public CropHarvestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_harvest");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CROPS_HARVEST.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Block block = event.getBlock();
		if (event.isRightClick() && event.isItem(Tag.ITEMS_HOES) && block.getBlockData() instanceof Ageable crop && crop.getAge() >= crop.getMaximumAge()) {
			Items.damage(event.getItem(), event.getPlayer());
			Blocks.breakSound(block);
			event.getPlayer().breakBlock(block);
			crop.setAge(0);
			block.setBlockData(crop);
		}
	}
}
