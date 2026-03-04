package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.block.Beehive;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import javax.annotation.Nonnull;

public class BeeCaptureTweak extends DefaultTweak {

	public BeeCaptureTweak(@Nonnull Tweaky plugin) {
		super(plugin, "bee_capture");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(i -> i.getItemMeta() instanceof BlockStateMeta meta && meta.getBlockState() instanceof Beehive beehive && !beehive.isFull())
				.entity(EntityType.BEE);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
		BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();

		Beehive beehive = (Beehive) meta.getBlockState();
		beehive.addEntity((Bee) event.getRightClicked());
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
