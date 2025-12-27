package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nonnull;

public class EntitySilenceTweak extends Tweak {

	public EntitySilenceTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_silence");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ENTITY_SILENCE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_ENTITY_SILENCE.get().material(Material.NAME_TAG);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player)) {
			return;
		}

		Material itemType = player.getInventory().getItemInMainHand().getType();
		if (event.getHand() != EquipmentSlot.HAND || itemType != Material.NAME_TAG || !(event.getRightClicked() instanceof LivingEntity entity)) {
			return;
		}

		String prev = entity.getCustomName();
		Bukkit.getScheduler().runTask(plugin, () -> {
			String curr = entity.getCustomName();
			if (!entity.isValid() || curr == null || curr.equals(prev)) {
				return;
			}
			switch (curr.toLowerCase()) {
				case "silent", "silence" -> entity.setSilent(true);
				case "unsilent", "unsilence" -> entity.setSilent(false);
			}
		});
	}
}
