package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
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

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_CROPS_HARVEST.get().material(Material.NETHERITE_HOE);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || !event.isRightClick() || !event.isItem(Tag.ITEMS_HOES)) {
			return;
		}

		Block block = event.getBlock();
		if (block.getBlockData() instanceof Ageable crop && crop.getAge() >= crop.getMaximumAge() && player.breakBlock(block)) {
			crop.setAge(0);
			block.setBlockData(crop);
			Items.damage(event.getItem(), event.getPlayer());
			Blocks.breakSound(block);
		}
	}
}
