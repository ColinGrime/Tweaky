package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class TorchThrowTweak extends Tweak {

	public TorchThrowTweak(@Nonnull Tweaky plugin) {
		super(plugin, "torch_throw");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_TORCH_THROW.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.TORCH)
				.name("&aTorch Placement &8(Throw)")
				.lore("&7Places a Torch on the ground.")
				.usage("&eUsage: &aThrow Torches on the ground to place them.");
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (event.getItemDrop().getItemStack().getType() != Material.TORCH) {
			return;
		}

		Item item = event.getItemDrop();
		Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
			if (item.isDead()) {
				task.cancel();
				return;
			}

			Location location = item.getLocation();
			boolean readyToPlace = Math.abs(item.getVelocity().getY()) < 0.01 && !location.clone().add(0, -1, 0).getBlock().getType().isAir();
			if (!readyToPlace) {
				return;
			}

			task.cancel();

			Block block = location.getBlock();
			if (!Players.canBuild(event.getPlayer(), block)) {
				return;
			}

			if (block.getType().isAir()) {
				block.setType(Material.TORCH);
				Blocks.placeSound(block);

				if (item.getItemStack().getAmount() > 1) {
					ItemStack itemStack = item.getItemStack();
					itemStack.setAmount(itemStack.getAmount() - 1);
					item.setItemStack(itemStack);
				} else {
					item.remove();
				}
			}
		}, 1L, 1L);
	}
}
