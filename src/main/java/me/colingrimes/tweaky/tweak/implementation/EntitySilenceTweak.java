package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
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
		return TweakItem
				.of(Material.NAME_TAG)
				.name("&aSilence Mobs")
				.lore("&7Name a Mob \"silence\" to make it silent.")
				.lore("&7Name a Mob \"unsilence\" to make it audible.")
				.usage("&eUsage: &aName a Mob \"silence\" or \"unsilence\" to toggle its sound.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Material itemType = event.getPlayer().getInventory().getItemInMainHand().getType();
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
