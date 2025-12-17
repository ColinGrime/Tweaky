package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class EntityIgniteTweak extends Tweak {

	public EntityIgniteTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_ignite");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ENTITY_IGNITE.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.FLINT_AND_STEEL)
				.name("&aIgnite Mobs &8(Right Click)")
				.lore("&7Set any Mob on fire.")
				.lore()
				.lore("&8Requires (1):")
				.lore(" &7Flint & Steel")
				.lore(" &7Fire Charge")
				.usage("&eUsage: &aRight Click a Mob with a Flint & Steel or Fire Charge to set it on fire.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEntity(@Nonnull PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Material type = item.getType();
		if (event.getHand() != EquipmentSlot.HAND || (type != Material.FLINT_AND_STEEL && type != Material.FIRE_CHARGE)) {
			return;
		}

		// Ignore creepers.
		if (event.getRightClicked().getType() == EntityType.CREEPER) {
			return;
		}

		event.getRightClicked().setFireTicks(20 * 8);
		event.setCancelled(true);
		player.swingMainHand();

		switch (type) {
			case Material.FLINT_AND_STEEL -> {
				Items.damage(item, player);
				Sounds.play(event.getRightClicked(), Sound.ITEM_FLINTANDSTEEL_USE);
			}
			case Material.FIRE_CHARGE -> {
				Items.remove(item);
				Sounds.play(event.getRightClicked(), Sound.ITEM_FIRECHARGE_USE);
			}
		}
	}
}
