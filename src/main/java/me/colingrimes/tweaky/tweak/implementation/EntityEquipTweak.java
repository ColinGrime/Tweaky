package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Events;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.EntityEquipment;

import javax.annotation.Nonnull;
import java.util.Set;

public class EntityEquipTweak extends Tweak {

	private final Set<EntityType> EQUIPPABLE_ENTITIES = Set.of(
			EntityType.ZOMBIE,
			EntityType.SKELETON,
			EntityType.HUSK,
			EntityType.STRAY,
			EntityType.DROWNED,
			EntityType.WITHER_SKELETON,
			EntityType.PILLAGER,
			EntityType.PIGLIN,
			EntityType.PIGLIN_BRUTE,
			EntityType.ZOMBIFIED_PIGLIN,
			EntityType.ARMOR_STAND
	);

	public EntityEquipTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_equip");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ENTITY_EQUIP.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_ENTITY_EQUIP.get().material(Material.STONE_SWORD);
	}

	@EventHandler
	public void onPlayerDropItem(@Nonnull PlayerDropItemEvent event) {
		if (!hasPermission(event.getPlayer())) {
			return;
		}

		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (event.getItemDrop().isDead()) {
				return;
			}

			for (Entity nearbyEntity : event.getItemDrop().getNearbyEntities(1, 1, 1)) {
				if (!(nearbyEntity instanceof LivingEntity entity) || !EQUIPPABLE_ENTITIES.contains(entity.getType())) {
					continue;
				}

				EntityEquipment equipment = entity.getEquipment();
				if (equipment == null || entity.getLocation().getWorld() == null) {
					continue;
				}

				if (!Events.canInteractEntity(event.getPlayer(), entity)) {
					return;
				}

				Location location = entity.getLocation();
				location.getWorld().dropItemNaturally(location, equipment.getItemInMainHand());
				equipment.setItemInMainHand(event.getItemDrop().getItemStack());
				event.getItemDrop().remove();
				return;
			}
		}, 10L);
	}
}
