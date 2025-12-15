package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HappyGhast;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.inventory.EquipmentSlotGroup;

import javax.annotation.Nonnull;

public class HappyGhastSpeedTweak extends Tweak {

	private final NamespacedKey FLY_SPEED_KEY;

	public HappyGhastSpeedTweak(@Nonnull Tweaky plugin) {
		super(plugin, "happy_ghast_speed");
		this.FLY_SPEED_KEY = new NamespacedKey(plugin, "happy_ghast_speed");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_HAPPY_GHAST_SPEED.get();
	}

	@EventHandler
	public void onEntityMount(@Nonnull EntityMountEvent event) {
		if (!(event.getMount() instanceof HappyGhast ghast)) {
			return;
		}

		AttributeInstance fly = ghast.getAttribute(Attribute.FLYING_SPEED);
		if (fly != null && fly.getModifiers().stream().noneMatch(m -> m.getKey().equals(FLY_SPEED_KEY))) {
			fly.addModifier(new AttributeModifier(
					FLY_SPEED_KEY,
					settings.TWEAK_HAPPY_GHAST_SPEED_VALUE.get() - 1,
					AttributeModifier.Operation.ADD_SCALAR,
					EquipmentSlotGroup.ANY
			));
		}
	}

	@EventHandler
	public void onEntityUnmount(@Nonnull EntityDismountEvent event) {
		if (!(event.getDismounted() instanceof HappyGhast ghast) || ghast.getPassengers().size() > 1) {
			return;
		}

		AttributeInstance fly = ghast.getAttribute(Attribute.FLYING_SPEED);
		if (fly != null) {
			fly.getModifiers().stream().filter(m -> m.getKey().equals(FLY_SPEED_KEY)).findFirst().ifPresent(fly::removeModifier);
		}
	}
}
