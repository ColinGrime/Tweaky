package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class WeaponSwingThroughTweak extends Tweak {

	public WeaponSwingThroughTweak(@Nonnull Tweaky plugin) {
		super(plugin, "weapon_swing_through");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_WEAPON_SWING_THROUGH.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_WEAPON_SWING_THROUGH.get().material(Material.DIAMOND_SWORD);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || !event.isLeftClick() || !event.getBlock().isPassable() || !event.isItem(Tag.ITEMS_SWORDS, Tag.ITEMS_AXES)) {
			return;
		}

		RayTraceResult result = Players.rayTrace(player, 3);
		if (result != null && result.getHitEntity() != null) {
			player.attack(result.getHitEntity());
		}
	}
}
