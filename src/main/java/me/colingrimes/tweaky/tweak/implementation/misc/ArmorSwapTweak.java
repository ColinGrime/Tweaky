package me.colingrimes.tweaky.tweak.implementation.misc;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Events;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class ArmorSwapTweak extends DefaultTweak {

	public ArmorSwapTweak(@Nonnull Tweaky plugin) {
		super(plugin, "armor_swap");
	}

	@TweakHandler
	public void onPlayerSneak(@Nonnull PlayerToggleSneakEvent event) {
		if (!event.isSneaking()) {
			return;
		}

		Player player = event.getPlayer();
		for (ArmorStand stand : Util.nearby(ArmorStand.class, player.getLocation(), 0.5)) {
			if (!Events.canInteractEntity(player, stand)) {
				return;
			}

			// Ignore custom Armor Stands (from other plugins like holograms, chairs, etc.)
			if (stand.isMarker() || !stand.isVisible()) {
				continue;
			}

			swapArmor(event.getPlayer(), stand);
			return;
		}
	}

	/**
	 * Swaps the player's armor with the armor stand's armor.
	 *
	 * @param player the player
	 * @param stand the armor stand
	 */
	private void swapArmor(@Nonnull Player player, @Nonnull ArmorStand stand) {
		ItemStack[] armor = player.getEquipment().getArmorContents();
		player.getEquipment().setArmorContents(stand.getEquipment().getArmorContents());
		stand.getEquipment().setArmorContents(armor);
	}
}
