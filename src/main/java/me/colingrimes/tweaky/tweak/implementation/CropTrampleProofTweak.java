package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;

public class CropTrampleProofTweak extends Tweak {

	public CropTrampleProofTweak(@Nonnull Tweaky plugin) {
		super(plugin, "crops_trample_proof");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_CROPS_TRAMPLE_PROOF.get();
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		if (event.getAction() != Action.PHYSICAL) {
			return;
		}

		Block block = event.getClickedBlock();
		if (block != null && block.getType() == Material.FARMLAND) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityInteract(@Nonnull EntityInteractEvent event) {
		if (event.getBlock().getType() == Material.FARMLAND) {
			event.setCancelled(true);
		}
	}
}
