//package me.colingrimes.tweaks.tweak.implementation;
//
//import me.colingrimes.midnight.util.Common;
//import me.colingrimes.midnight.util.bukkit.Items;
//import me.colingrimes.midnight.util.bukkit.Locations;
//import me.colingrimes.midnight.util.bukkit.Vectors;
//import me.colingrimes.tweaks.Tweaks;
//import me.colingrimes.tweaks.config.Settings;
//import me.colingrimes.tweaks.tweak.Tweak;
//import org.bukkit.Location;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Boat;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.inventory.PrepareAnvilEvent;
//import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
//import org.bukkit.event.vehicle.VehicleMoveEvent;
//import org.bukkit.util.Vector;
//
//import javax.annotation.Nonnull;
//
//public class BoatClimbTweak extends Tweak {
//
//	public BoatClimbTweak(@Nonnull Tweaks plugin) {
//		super(plugin, "boat_climb");
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return false;
//	}
//
//	@EventHandler
//	public void onVehicleMove(@Nonnull VehicleMoveEvent event) {
//		if (!(event.getVehicle() instanceof Boat boat)) {
//			return;
//		}
//
//		Vector toVector = Vectors.direction(event.getFrom(), event.getTo());
//		if (Double.isNaN(toVector.getX()) || Double.isNaN(toVector.getY()) || Double.isNaN(toVector.getZ())) {
//			return;
//		}
//
//		float yaw = event.getVehicle().getLocation().getYaw();
//		Vector direction = new Vector(-Math.sin(Math.toRadians(yaw)), 0, Math.cos(Math.toRadians(yaw))).normalize();
//
//		Location front = event.getTo().clone().add(direction.multiply(1.2));
//		Block frontBlock = front.getBlock();
//		Common.broadcast("Block From Yaw: " + frontBlock.getType());
//		Common.broadcast("Block From toVector: " + toVector);
////		if (frontBlock.getType().name().endsWith("SLAB")) {
////			boat.setVelocity(boat.getVelocity().add(direction.multiply(0.1).add(new Vector(0, 0.1, 0))));
////		}
//	}
//}
