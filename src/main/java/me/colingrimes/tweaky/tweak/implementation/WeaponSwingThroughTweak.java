package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Tag;
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
		return TweakItem
				.of(Material.DIAMOND_SWORD)
				.name("&aWeapon Swing Through")
				.lore("&7Attacks pass through passable blocks.")
				.lore()
				.lore("&8Requires:")
				.lore(" &7Sword &8(Any)")
				.lore(" &7Axe &8(Any)")
				.usage("&eUsage: &aWeapon attacks can swing through passable blocks to hit Mobs.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isLeftClick() || !event.getBlock().isPassable() || !event.isItem(Tag.ITEMS_SWORDS, Tag.ITEMS_AXES)) {
			return;
		}

		RayTraceResult result = Players.rayTrace(event.getPlayer(), 3);
		if (result != null && result.getHitEntity() != null) {
			event.getPlayer().attack(result.getHitEntity());
		}
	}
}
