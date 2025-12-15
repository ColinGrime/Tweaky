package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Snow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class SnowballTweak extends Tweak {

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

	@EventHandler
	public void onProjectileHit(@Nonnull ProjectileHitEvent event) {
		if (event.getEntity().getType() != EntityType.SNOWBALL) {
			return;
		}

		hitTweaks(event);
		blockTweaks(event);
	}

	private void hitTweaks(@Nonnull ProjectileHitEvent event) {
		if (!(event.getEntity().getShooter() instanceof Entity shooter) || !(event.getHitEntity() instanceof LivingEntity hit)) {
			return;
		}

		// TWEAK -- damage
		if (settings.TWEAK_SNOWBALLS_DAMAGE.get()) {
			hit.setHealth(Math.max(0, hit.getHealth() - settings.TWEAK_SNOWBALLS_DAMAGE_AMOUNT.get()));
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

		// Check for permission.
		if (event.getEntity().getShooter() instanceof Player player && !Players.canBuild(player, hit, target)) {
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
	@EventHandler
	public void onProjectileLaunch(@Nonnull ProjectileLaunchEvent event) {
		Entity snowball = event.getEntity();
		if (event.getEntity().getType() != EntityType.SNOWBALL) {
			return;
		}

		Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
			if (snowball.isDead()) {
				task.cancel();
				return;
			}

			Location location = snowball.getLocation();

			// TWEAK -- form ice (mid-water)
			if (settings.TWEAK_SNOWBALLS_FORM_ICE.get()) {
				for (Location loc : Util.around(location, 0.25)) {
					if (loc.getBlock().getType() == Material.WATER) {
						loc.getBlock().setType(Material.ICE);
						snowball.remove();
						return;
					}
				}
			}

			// TWEAK -- break plants (mid-air)
			if (settings.TWEAK_SNOWBALLS_BREAK_PLANTS.get()) {
				for (Location loc : Util.around(location, 0.5)) {
					if (Blocks.isPlant(loc.getBlock().getType())) {
						Blocks.destroy(loc.getBlock());
					}
				}
			}
		}, 1L, 1L);
	}
}
