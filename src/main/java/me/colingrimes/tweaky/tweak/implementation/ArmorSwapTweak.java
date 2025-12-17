package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Players;
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
		return TweakItem
				.of(Material.CHAINMAIL_CHESTPLATE)
				.name("&aArmor Swap &8(Sneak)")
				.lore("&7Armor Stands can quick swap Armor.")
				.usage("&eUsage: &aSneak over an Armor Stand to quick swap Armor.");
	}

	@EventHandler
	public void onPlayerSneak(@Nonnull PlayerToggleSneakEvent event) {
		if (!event.isSneaking()) {
			return;
		}

		Player player = event.getPlayer();
		for (ArmorStand stand : Util.nearby(ArmorStand.class, player.getLocation(), 0.5)) {
			if (!Players.canBuild(player, stand.getLocation().getBlock())) {
				return;
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
