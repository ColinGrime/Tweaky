package me.colingrimes.tweaky.tweak.implementation.mob;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class EntityDyeTweak extends DefaultTweak {

	public EntityDyeTweak(@Nonnull Tweaky plugin) {
		super(plugin, "entity_dye");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.item(i -> i.getType().name().endsWith("DYE"))
				.entity(e -> e.getCustomName() != null);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
		entity.setCustomName(convertDye(item.getType()) + ChatColor.stripColor(entity.getCustomName()));
		Items.remove(item);
		event.setCancelled(true);
	}

	/**
	 * Converts the dye to chat color to be used in colored messages.
	 *
	 * @param dye the dye to convert
	 * @return the converted chat color
	 */
	@Nonnull
	private ChatColor convertDye(@Nonnull Material dye) {
		return switch (dye) {
			case WHITE_DYE      -> ChatColor.WHITE;
			case ORANGE_DYE     -> ChatColor.GOLD;
			case MAGENTA_DYE    -> ChatColor.LIGHT_PURPLE;
			case LIGHT_BLUE_DYE -> ChatColor.AQUA;
			case YELLOW_DYE     -> ChatColor.YELLOW;
			case LIME_DYE       -> ChatColor.GREEN;
			case PINK_DYE       -> ChatColor.LIGHT_PURPLE;
			case GRAY_DYE       -> ChatColor.DARK_GRAY;
			case LIGHT_GRAY_DYE -> ChatColor.GRAY;
			case CYAN_DYE       -> ChatColor.DARK_AQUA;
			case PURPLE_DYE     -> ChatColor.DARK_PURPLE;
			case BLUE_DYE       -> ChatColor.BLUE;
			case BROWN_DYE      -> ChatColor.DARK_RED;
			case GREEN_DYE      -> ChatColor.DARK_GREEN;
			case RED_DYE        -> ChatColor.RED;
			case BLACK_DYE      -> ChatColor.BLACK;
			default             -> ChatColor.WHITE;
		};
	}
}
