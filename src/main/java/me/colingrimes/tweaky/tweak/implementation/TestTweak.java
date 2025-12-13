package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.HappyGhast;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

// Used for testing. Should be disabled on prod.
public class TestTweak extends Tweak {

	public TestTweak(@Nonnull Tweaky plugin) {
		super(plugin, "test_tweak");
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

//	@EventHandler
//	public void onBed(PlayerBedEnterEvent event) {
//		if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW) {
//			event.getPlayer().sleep(event.getBed().getLocation(), true);
//		}
//	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isLeftClick() || !event.isItem(Material.AIR)) {
			return;
		}

		Block block = event.getBlock();
		if (Tag.WOODEN_DOORS.isTagged(block.getType())) {
			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1F, 1F);
			Bukkit.getScheduler().runTaskLater(plugin, (task) -> {
				block.getWorld().playSound(block.getLocation(), "custom.stranger_things", 1F, 1F);
			}, 20L);
			event.setCancelled(true);
		} else if (block.getType() == Material.IRON_DOOR) {
			block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 1F, 1F);
			block.getWorld().playSound(block.getLocation(), Sound.BLOCK_BELL_RESONATE, 1F, 1F);
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void die(EntityDeathEvent event) {
		if (event.getEntity() instanceof Chicken && event.getDamageSource().getDamageType() == DamageType.LAVA) {
			event.getEntity().getWorld().playSound(event.getEntity().getLocation(), Sound.MUSIC_DISC_LAVA_CHICKEN, 1F, 1F);
		}
	}

	@EventHandler
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isRightClick() || !Tag.ITEMS_SHOVELS.isTagged(event.getItemType()) || !event.isBlock(Material.DIRT_PATH)) {
			return;
		}

		Items.damage(event.getItem(), event.getPlayer());
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_HOE_TILL, 1F, 1F);
		event.getBlock().setType(Material.DIRT);
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getHand() != EquipmentSlot.HAND) {
			return;
		}

		Location eye = player.getEyeLocation();
		RayTraceResult result = player.getWorld().rayTrace(eye, eye.getDirection(), 50, FluidCollisionMode.NEVER, true, 0, e -> !e.equals(player));
		if (result == null || !(result.getHitEntity() instanceof HappyGhast ghast) || !ghast.isAdult()) {
			return;
		}

		EntityEquipment equipment = ghast.getEquipment();
		if (equipment != null && equipment.getItem(EquipmentSlot.BODY).getType().name().endsWith("_HARNESS")) {
			ghast.addPassenger(player);
			event.setCancelled(true);
		}
	}
}
