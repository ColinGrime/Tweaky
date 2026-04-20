package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.util.TweakItem;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.Util;
import me.colingrimes.tweaky.util.bukkit.Sounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LadderTeleportationTweak extends DefaultTweak {

	// Used to make sure player is positioned on the ladder correctly when teleporting.
	private static final Map<BlockFace, Consumer<Location>> UP_OFFSETS = new HashMap<>();
	private static final Map<BlockFace, Consumer<Location>> DOWN_OFFSETS = new HashMap<>();

	static {
		UP_OFFSETS.put(BlockFace.NORTH, l -> l.setZ(l.getBlockZ() + 0.7));
		UP_OFFSETS.put(BlockFace.EAST, l -> l.setX(l.getBlockX() + 0.3));
		UP_OFFSETS.put(BlockFace.SOUTH, l -> l.setZ(l.getBlockZ() + 0.3));
		UP_OFFSETS.put(BlockFace.WEST, l -> l.setX(l.getBlockX() + 0.7));
		DOWN_OFFSETS.put(BlockFace.NORTH, l -> l.setZ(l.getBlockZ() + 0.3));
		DOWN_OFFSETS.put(BlockFace.EAST, l -> l.setX(l.getBlockX() + 0.7));
		DOWN_OFFSETS.put(BlockFace.SOUTH, l -> l.setZ(l.getBlockZ() + 0.7));
		DOWN_OFFSETS.put(BlockFace.WEST, l -> l.setX(l.getBlockX() + 0.3));
	}

	enum Control {
		Automatic,
		Manual,
		Click
	}

	public LadderTeleportationTweak(@Nonnull Tweaky plugin) {
		super(plugin, "ladder_teleportation");
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		Control control = Util.parse(Control.class, settings.TWEAK_LADDER_TELEPORTATION_CONTROL.get());
		return switch (control) {
			case Automatic -> menus.TWEAK_LADDER_TELEPORTATION_AUTOMATIC.get();
			case Manual -> menus.TWEAK_LADDER_TELEPORTATION_MANUAL.get();
			case Click -> menus.TWEAK_LADDER_TELEPORTATION_CLICK.get();
			case null -> super.getGuiItem();
		};
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.standing()
				.rightClick()
				.block(Material.LADDER);
	}

	@EventHandler
	public void onPlayerMove(@Nonnull PlayerMoveEvent event) {
		Location to = event.getTo();
		if (to == null || to.getY() == event.getFrom().getY()) {
			return;
		}

		Player player = event.getPlayer();
		if (!hasPermission(player) || !player.isClimbing() || !(to.getBlock().getBlockData() instanceof Ladder ladder)) {
			return;
		}

		Control control = Util.parse(Control.class, settings.TWEAK_LADDER_TELEPORTATION_CONTROL.get());
		if (control == null || control == Control.Click) {
			return;
		}

		double pitch = to.getPitch();
		boolean up;
		if (control == Control.Automatic) {
			up = event.getFrom().getY() < to.getY();
		} else if (pitch == -90) {
			up = true;
		} else if (pitch == 90) {
			up = false;
		} else {
			return;
		}

		Block block = to.getBlock();
		Vector direction = up ? new Vector(0, 1, 0) : new Vector(0, -1, 0);
		while (block.getBlockData() instanceof Ladder l && l.getFacing() == ladder.getFacing()) {
			block = block.getLocation().add(direction).getBlock();
		}

		if (up && !block.getType().isSolid() && !block.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
			UP_OFFSETS.get(ladder.getFacing()).accept(to);
			to.setY(block.getY());
			Scheduler.sync().run(() -> Sounds.play(to, Sound.ENTITY_PLAYER_TELEPORT));
		} else if (!up && block.getType().isSolid() && !block.getLocation().add(0, 2, 0).getBlock().getType().isSolid()) {
			DOWN_OFFSETS.get(ladder.getFacing()).accept(to);
			to.setY(block.getY() + 1);
			Scheduler.sync().run(() -> Sounds.play(to, Sound.ENTITY_PLAYER_TELEPORT));
		}
	}

	@TweakHandler
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Block block = event.getBlock();
		if (!(block.getBlockData() instanceof Ladder ladder)) {
			return;
		}

		Control control = Util.parse(Control.class, settings.TWEAK_LADDER_TELEPORTATION_CONTROL.get());
		if (control != Control.Click) {
			return;
		}

		Player player = event.getPlayer();
		boolean up = player.getLocation().getPitch() <= 0;

		Vector direction = up ? new Vector(0, 1, 0) : new Vector(0, -1, 0);
		while (block.getBlockData() instanceof Ladder l && l.getFacing() == ladder.getFacing()) {
			block = block.getLocation().add(direction).getBlock();
		}

		Location location = block.getLocation().add(0.5, 0, 0.5);
		location.setYaw(player.getLocation().getYaw());
		location.setPitch(player.getLocation().getPitch());

		if (up && !block.getType().isSolid() && !block.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
			UP_OFFSETS.get(ladder.getFacing()).accept(location);
			player.teleport(location);
			Scheduler.sync().run(() -> Sounds.play(location, Sound.ENTITY_PLAYER_TELEPORT));
		} else if (!up && block.getType().isSolid() && !block.getLocation().add(0, 2, 0).getBlock().getType().isSolid()) {
			DOWN_OFFSETS.get(ladder.getFacing()).accept(location);
			player.teleport(location.add(0, 1, 0));
			Scheduler.sync().run(() -> Sounds.play(location, Sound.ENTITY_PLAYER_TELEPORT));
		}
	}
}
