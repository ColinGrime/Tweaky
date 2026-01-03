package me.colingrimes.tweaky.tweak.type;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.NBT;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a {@link Tweak} that can be toggleable via a command.
 */
public abstract class ToggleTweak extends Tweak implements CommandExecutor {

	private final Set<UUID> toggleOn = new HashSet<>();
	private final String TOGGLE_KEY;
	private final String command;
	private final boolean defToggle;

	/**
	 * Constructs a toggle tweak.
	 *
	 * @param plugin the plugin
	 * @param id the id of the tweak
	 * @param command the command used to toggle the tweak
	 * @param defToggle the default value of the toggle
	 */
	public ToggleTweak(@Nonnull Tweaky plugin, @Nonnull String id, @Nonnull String command, boolean defToggle) {
		super(plugin, id);
		this.TOGGLE_KEY = id + "_toggle";
		this.command = command;
		this.defToggle = defToggle;
	}

	@Override
	public void init() {
		Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(this);
		Players.forEach(this::checkToggle);
	}

	@Override
	public void shutdown() {
		Objects.requireNonNull(Bukkit.getPluginCommand(command)).setExecutor(null);
		toggleOn.clear();
	}

	@Override
	public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
		if (!(sender instanceof Player player) || !hasPermission(player)) {
			return true;
		}

		UUID uuid = player.getUniqueId();
		if (toggleOn.contains(uuid)) {
			toggleOn.remove(uuid);
			NBT.setTag(player, TOGGLE_KEY, false);
			Players.sound(player, Sound.BLOCK_NOTE_BLOCK_BASS);
			deactivate(player);
		} else {
			toggleOn.add(uuid);
			NBT.setTag(player, TOGGLE_KEY, true);
			Players.sound(player, Sound.BLOCK_NOTE_BLOCK_PLING);
			activate(player);
		}

		return true;
	}

	/**
	 * Checks the toggle value of the player and adds it to the set.
	 * <p>
	 * If no toggle value is present, it will use the default toggle value given.
	 *
	 * @param player the player
	 */
	public void checkToggle(@Nonnull Player player) {
		if (!NBT.hasTag(player, TOGGLE_KEY, Boolean.class)) {
			NBT.setTag(player, TOGGLE_KEY, defToggle);
		}

		boolean toggle = NBT.getTag(player, TOGGLE_KEY, Boolean.class).orElse(defToggle);
		if (toggle) {
			toggleOn.add(player.getUniqueId());
		}
	}

	/**
	 * Removes the toggle data from the player.
	 *
	 * @param player the player
	 */
	public void removeToggle(@Nonnull Player player) {
		toggleOn.remove(player.getUniqueId());
	}

	/**
	 * Checks if the player has this tweak toggled on.
	 *
	 * @param entity the player
	 * @return true if the tweak is toggled on for the player
	 */
	public boolean isToggled(@Nonnull Entity entity) {
		return entity instanceof Player player && toggleOn.contains(player.getUniqueId());
	}

	/**
	 * Activates the tweak if the player has it toggled on and has permission to use it.
	 *
	 * @param player the player
	 */
	public void activate(@Nonnull Player player) {
		if (isToggled(player) && hasPermission(player)) {
			activateTweak(player);
		}
	}

	/**
	 * Deactivates the tweak if the player has it toggled off and has permission to use it.
	 *
	 * @param player the player
	 */
	public void deactivate(@Nonnull Player player) {
		if (!isToggled(player) && hasPermission(player)) {
			deactivateTweak(player);
		}
	}

	/**
	 * Method that should be called when the toggle is activated.
	 *
	 * @param player the player
	 */
	protected abstract void activateTweak(@Nonnull Player player);

	/**
	 * Method that should be called when the toggle is deactivated.
	 *
	 * @param player the player
	 */
	protected abstract void deactivateTweak(@Nonnull Player player);
}
