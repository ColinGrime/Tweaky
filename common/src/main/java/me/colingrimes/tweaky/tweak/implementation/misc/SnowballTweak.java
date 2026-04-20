package me.colingrimes.tweaky.tweak.implementation.misc;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.util.TweakItem;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Events;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SnowballTweak extends DefaultTweak {

	public SnowballTweak(@Nonnull Tweaky plugin) {
		super(plugin, "snowballs");
	}

	@Override
	public boolean isEnabled() {
		return getCount() > 0;
	}

	@Override
	public int getCount() {
		Boolean[] toggles = {
				settings.TWEAK_SNOWBALLS_ADD_SNOW_LAYER.get(),
				settings.TWEAK_SNOWBALLS_BREAK_PLANTS.get(),
				settings.TWEAK_SNOWBALLS_DAMAGE.get(),
				settings.TWEAK_SNOWBALLS_EXTINGUISH_ENTITIES.get(),
				settings.TWEAK_SNOWBALLS_EXTINGUISH_FIRE.get(),
				settings.TWEAK_SNOWBALLS_FORM_ICE.get(),
				settings.TWEAK_SNOWBALLS_FORM_SNOW.get(),
				settings.TWEAK_SNOWBALLS_KNOCKBACK.get()
		};
		return (int) Arrays.stream(toggles).filter(t -> t).count();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_SNOWBALLS.get()
				.placeholder("{count}", getCount())
				.placeholder("{damage}", settings.TWEAK_SNOWBALLS_DAMAGE_AMOUNT.get());
	}

	@TweakHandler
	public void onProjectileHit(@Nonnull ProjectileHitEvent event) {
		if (event.getEntity().getType() == EntityType.SNOWBALL) {
			hitTweaks(event);
			blockTweaks(event);
		}
	}

	private void hitTweaks(@Nonnull ProjectileHitEvent event) {
		if (!(event.getEntity().getShooter() instanceof Entity shooter) || !(event.getHitEntity() instanceof LivingEntity hit)) {
			return;
		}

		// Ignore Ghastlings and Happy Ghasts.
		// Ghastlings require snowballs, so this avoids accidental damage to it if you throw snowballs at it.
		if (hit.getType() == EntityType.HAPPY_GHAST) {
			return;
		}

		// TWEAK -- damage
		if (settings.TWEAK_SNOWBALLS_DAMAGE.get()) {
			hit.damage(settings.TWEAK_SNOWBALLS_DAMAGE_AMOUNT.get());
		}

		// TWEAK -- knockback
		if (settings.TWEAK_SNOWBALLS_KNOCKBACK.get()) {
			hit.setVelocity(Util.direction(shooter, hit).multiply(settings.TWEAK_SNOWBALLS_KNOCKBACK_AMOUNT.get()));
		}

		// TWEAK -- extinguish entity
		if (settings.TWEAK_SNOWBALLS_EXTINGUISH_ENTITIES.get() && hit.getFireTicks() > 0) {
			hit.setFireTicks(0);
			Sounds.play(hit, Sound.ENTITY_GENERIC_EXTINGUISH_FIRE);
		}
	}

	private void blockTweaks(@Nonnull ProjectileHitEvent event) {
		Block hit = event.getHitBlock();
		BlockFace face = event.getHitBlockFace();
		if (hit == null || face == null) {
			return;
		}

		Block target = hit.getRelative(face);
		Block below = target.getRelative(BlockFace.DOWN);
		Material targetType = target.getType();

		// Check for region protection.
		if (!checkRegionProtection(event.getEntity(), target)) {
			return;
		}

		// TWEAK -- add snow layer on snow layers
		Block existing = hit.getBlockData() instanceof Snow s && s.getLayers() < s.getMaximumLayers() ? hit : target;
		if (settings.TWEAK_SNOWBALLS_ADD_SNOW_LAYER.get() && existing.getBlockData() instanceof Snow snow && snow.getLayers() < snow.getMaximumLayers()) {
			Blocks.edit(existing, Snow.class, s -> s.setLayers(s.getLayers() + 1));
			return;
		}

		// TWEAK -- form snow
		boolean isPlaceableOn = (below.getType().isSolid() && below.getType().isOccluding()) || (below.getBlockData() instanceof Snow snow && snow.getLayers() == snow.getMaximumLayers());
		if (settings.TWEAK_SNOWBALLS_FORM_SNOW.get() && targetType.isAir() && isPlaceableOn) {
			target.setType(Material.SNOW);
			return;
		}

		// TWEAK -- form ice (only on solid block contact)
		if (settings.TWEAK_SNOWBALLS_FORM_ICE.get() && target.getType() == Material.WATER) {
			target.setType(Material.ICE);
			return;
		}

		// TWEAK -- break plants (only on solid block contact)
		if (settings.TWEAK_SNOWBALLS_BREAK_PLANTS.get() && Blocks.isPlant(targetType)) {
			Blocks.destroy(target);
			return;
		}

		// TWEAK -- extinguish fire
		if (settings.TWEAK_SNOWBALLS_EXTINGUISH_FIRE.get() && Tag.FIRE.isTagged(targetType)) {
			target.setType(Material.AIR);
			Sounds.play(target, Sound.BLOCK_FIRE_EXTINGUISH);
		}
	}

	// NOTE: Due to the ProjectileHitEvent not running on non-solid blocks,
	//       we have to run a timer on snowball throw to detect midair collisions.
	@TweakHandler
	public void onProjectileLaunch(@Nonnull ProjectileLaunchEvent event) {
		Projectile snowball = event.getEntity();
		if (event.getEntity().getType() != EntityType.SNOWBALL) {
			return;
		}

		Scheduler.sync().runRepeating((task) -> {
			if (snowball.isDead()) {
				task.stop();
				return;
			}

			Location location = snowball.getLocation();

			// TWEAK -- form ice (mid-water)
			if (settings.TWEAK_SNOWBALLS_FORM_ICE.get()) {
				for (Location loc : Util.around(location, 0.25)) {
					Block block = loc.getBlock();
					if (block.getType() != Material.WATER) {
						continue;
					}

					snowball.remove();
					if (checkRegionProtection(snowball, block)) {
						block.setType(Material.ICE);
					}
					return;
				}
			}

			// TWEAK -- break plants (mid-air)
			if (settings.TWEAK_SNOWBALLS_BREAK_PLANTS.get()) {
				for (Location loc : Util.around(location, 0.5)) {
					Block block = loc.getBlock();
					if (Blocks.isPlant(block.getType()) && checkRegionProtection(snowball, block)) {
						Blocks.destroy(block);
					}
				}
			}
		}, 1L, 1L);
	}

	private boolean checkRegionProtection(@Nonnull Projectile projectile, @Nonnull Block block) {
		return !(projectile.getShooter() instanceof Player player) || Events.canBuild(player, block);
	}
}
