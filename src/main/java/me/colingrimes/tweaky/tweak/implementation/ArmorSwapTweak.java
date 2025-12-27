package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Events;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ArmorSwapTweak extends Tweak {

	public ArmorSwapTweak(@Nonnull Tweaky plugin) {
		super(plugin, "armor_swap");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ARMOR_SWAP.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_ARMOR_SWAP.get().material(Material.CHAINMAIL_CHESTPLATE);
	}

	@EventHandler
	public void onPlayerSneak(@Nonnull PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || !event.isSneaking()) {
			return;
		}

		for (ArmorStand stand : Util.nearby(ArmorStand.class, player.getLocation(), 0.5)) {
			if (!Events.canInteractEntity(player, stand)) {
				return;
			}

			// Ignore custom Armor Stands (from other plugins like holograms, chairs, etc.)
			if (stand.isMarker() || !stand.isVisible()) {
				continue;
			}

			EntityEquipment playerEquipment = player.getEquipment();
			EntityEquipment standEquipment = stand.getEquipment();
			if (playerEquipment != null && standEquipment != null) {
				ItemStack[] armor = playerEquipment.getArmorContents();
				playerEquipment.setArmorContents(standEquipment.getArmorContents());
				standEquipment.setArmorContents(armor);
				return;
			}
		}
	}
}
