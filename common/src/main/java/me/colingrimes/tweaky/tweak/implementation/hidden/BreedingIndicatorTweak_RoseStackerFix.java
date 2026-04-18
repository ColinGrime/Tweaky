package me.colingrimes.tweaky.tweak.implementation.hidden;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedEntity;
import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.io.Logger;
import me.colingrimes.tweaky.util.text.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BreedingIndicatorTweak_RoseStackerFix extends DefaultTweak {

	private final Map<UUID, Instant> breedingEntities = new HashMap<>();
	private final Map<Integer, String> ignoredEntities = new HashMap<>();
	private final PacketListener ignoreTags;
	private Task task;

	public BreedingIndicatorTweak_RoseStackerFix(@Nonnull Tweaky plugin) {
		super(plugin, "breeding_indicator");
		this.ignoreTags = new PlayServerEntityMetadataListener();
		Logger.log("BreedingIndicator has been modified to support RoseStacker.");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.setCategory(TweakCategory.TEXT);
	}

	@Override
	public void init() {
		ProtocolLibrary.getProtocolManager().addPacketListener(ignoreTags);
		task = Scheduler.sync().runRepeating(() -> {
			Instant now = Instant.now();
			var iterator = breedingEntities.entrySet().iterator();
			while (iterator.hasNext()) {
				var entry = iterator.next();
				Entity entity = Bukkit.getEntity(entry.getKey());
				Duration time = Duration.between(now, entry.getValue());
				if (tick(entity, time)) {
					iterator.remove();
				}
			}
		}, 20L, 20L);
	}

	@Override
	public void shutdown() {
		ProtocolLibrary.getProtocolManager().removePacketListener(ignoreTags);
		task.stop();
		breedingEntities.keySet().forEach(uuid -> updateEntityName(Bukkit.getEntity(uuid), null));
		breedingEntities.clear();
		ignoredEntities.clear();
	}

	@EventHandler
	public void onEntityBreed(@Nonnull EntityBreedEvent event) {
		breedingEntities.put(event.getMother().getUniqueId(), Instant.now().plusSeconds(300));
		breedingEntities.put(event.getFather().getUniqueId(), Instant.now().plusSeconds(300));
		updateEntityName(event.getMother(), "5:00");
		updateEntityName(event.getFather(), "5:00");
	}

	/**
	 * Sets the name to the breeding entity accordingly.
	 *
	 * @param entity the breeding entity
	 * @param time the current time it has left until it can breed again
	 * @return true if the breeding entity is done being updated and can be deleted from the map
	 */
	private boolean tick(@Nullable Entity entity, @Nonnull Duration time) {
		// Finished cooldown, we can reset and remove from map.
		if (time.isNegative() || time.isZero()) {
			updateEntityName(entity, null);
			return true;
		}

		// Entity might have been unloaded, so we need to keep it in the map and wait.
		if (entity == null || entity.isDead()) {
			return false;
		}

		// If it is further than 20 blocks away from a player, we can reset name.
		if (Util.nearby(Player.class, entity.getLocation(), 20).isEmpty()) {
			updateEntityName(entity, null);
			return false;
		}

		// Default case -- count down the time.
		updateEntityName(entity, Text.format(time));
		return false;
	}

	/**
	 * Updates the entity's name and sets their name to be visible.
	 *
	 * @param entity the entity to update
	 * @param name the new name
	 */
	private void updateEntityName(@Nullable Entity entity, @Nullable String name) {
		if (!(entity instanceof LivingEntity living) || living.isDead()) {
			return;
		}

		entity.setCustomName(name != null ? Text.color("&a" + name) : null);
		entity.setCustomNameVisible(name != null);

		// -------------------------
		// -- RoseStacker Support --
		// -------------------------
		StackedEntity stacked = RoseStackerAPI.getInstance().getStackedEntity(living);
		if (stacked != null) {
			stacked.updateDisplay();
			if (name != null) {
				ignoredEntities.put(entity.getEntityId(), name);
			}
		}
	}

	// In order to prioritize RoseStacker's tagging system, we need to ignore ours.
	// This is incredibly hacky, but unfortunately the SetEntityMetadata packet from the entity#setCustomName()
	// method gets sent after RoseStacker's tagging packet gets sent (even after calling it first), so it results
	// in some major flickering. Couldn't think of any other solution, so here we are. :(
	private class PlayServerEntityMetadataListener extends PacketAdapter {

		public PlayServerEntityMetadataListener() {
			super(BreedingIndicatorTweak_RoseStackerFix.this.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA);
		}

		@Override
		public void onPacketSending(@Nonnull PacketEvent event) {
			PacketContainer packet = event.getPacket();
			int entityID = packet.getIntegers().read(0);
			String ignoredName = ignoredEntities.get(entityID);
			if (ignoredName == null) {
				return;
			}

			for (WrappedDataValue wrapped : packet.getDataValueCollectionModifier().read(0)) {
				if (wrapped.getIndex() != 2 || !(wrapped.getValue() instanceof Optional<?> opt) || opt.isEmpty() || !(opt.get() instanceof WrappedChatComponent wrappedComponent)) {
					return;
				}

				String json = wrappedComponent.getJson();
				Component component = GsonComponentSerializer.gson().deserialize(json);
				String plain = PlainTextComponentSerializer.plainText().serialize(component);
				if (ignoredName.equals(plain)) {
					ignoredEntities.remove(entityID);
					event.setCancelled(true);
				}
			}
		}
	}
}
