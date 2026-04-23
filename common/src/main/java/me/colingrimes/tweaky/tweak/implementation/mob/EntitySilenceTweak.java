package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntitySilenceTweak extends DefaultTweak {

	public EntitySilenceTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_silence");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(Material.NAME_TAG)
				.entity(e -> e instanceof LivingEntity);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		LivingEntity entity = (LivingEntity) event.getRightClicked();
		String prevName = entity.getCustomName();
		Scheduler.sync().run(() -> checkSilence(entity, prevName));
	}

	/**
	 * Checks for the silence or audible attribute of the entity:
	 * <ul>
	 *     <li>If the entity is named {@code silent} or {@code silence}, then it will be set to silence mode.</li>
	 *     <li>If the entity is named {@code unsilent} or {@code unsilence}, then it will be set to audible mode.</li>
	 * </ul>
	 *
	 * @param entity the entity
	 * @param prevName the previous name of the entity (before it was name tagged)
	 */
	private void checkSilence(@Nonnull LivingEntity entity, @Nullable String prevName) {
		String currName = entity.getCustomName();
		if (!entity.isValid() || currName == null || currName.equals(prevName)) {
			return;
		}

		switch (currName.toLowerCase()) {
			case "silent", "silence" -> entity.setSilent(true);
			case "unsilent", "unsilence" -> entity.setSilent(false);
		}
	}
}
