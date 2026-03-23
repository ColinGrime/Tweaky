package me.colingrimes.tweaky.tweak.implementation.convenience;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.misc.Random;
import org.bukkit.Material;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class KeepInventoryTweak extends DefaultTweak {

	public KeepInventoryTweak(@Nonnull Tweaky plugin) {
		super(plugin, "keep_inventory");
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_KEEP_INVENTORY.get();
	}

	@TweakHandler
	public void onPlayerDeath(@Nonnull PlayerDeathEvent event) {
		if (settings.TWEAK_KEEP_INVENTORY_XP.get()) {
			event.setShouldDropExperience(false);
			event.setKeepLevel(true);
		}

		Iterator<ItemStack> iterator = event.getDrops().iterator();
		while (iterator.hasNext()) {
			ItemStack item = iterator.next();
			if (keepArmor(item) || keepTool(item) || keepUnstackable(item)) {
				iterator.remove();
				event.getItemsToKeep().add(item);
				continue;
			}

			if (keepStackable(item)) {
				double percentToKeep = settings.TWEAK_KEEP_INVENTORY_STACKABLES.get() / 100;
				int dropAmount = (int) Math.ceil(item.getAmount() * percentToKeep);
				int keepAmount = (int) Math.floor(item.getAmount() * percentToKeep);

				item.setAmount(dropAmount);

				ItemStack clone = item.clone();
				clone.setAmount(keepAmount);
				event.getItemsToKeep().add(clone);
			}
		}
	}

	/**
	 * Checks if the potential armor piece should be kept.
	 *
	 * @param item the item
	 * @return true if the item should be kept
	 */
	private boolean keepArmor(@Nonnull ItemStack item) {
		if (!settings.TWEAK_KEEP_INVENTORY_ARMOR.get()) {
			return false;
		}

		String name = item.getType().name();
		return item.getType() == Material.ELYTRA
			|| name.endsWith("_HELMET")
			|| name.endsWith("_CHESTPLATE")
			|| name.endsWith("_LEGGINGS")
			|| name.endsWith("_BOOTS");
	}

	/**
	 * Checks if the potential tool should be kept.
	 *
	 * @param item the item
	 * @return true if the item should be kept
	 */
	private boolean keepTool(@Nonnull ItemStack item) {
		if (!settings.TWEAK_KEEP_INVENTORY_TOOLS.get()) {
			return false;
		}

		boolean specific = switch(item.getType()) {
			case MACE, TRIDENT, BOW, CROSSBOW, FISHING_ROD, SHIELD -> true;
			default -> false;
		};

		String name = item.getType().name();
		return specific
			|| name.endsWith("_SWORD")
			|| name.endsWith("_PICKAXE")
			|| name.endsWith("_AXE")
			|| name.endsWith("_SHOVEL")
			|| name.endsWith("_HOE")
			|| name.endsWith("_SPEAR");
	}

	/**
	 * Checks if the potential unstackable item should be kept.
	 *
	 * @param item the item
	 * @return true if the item should be kept
	 */
	private boolean keepUnstackable(@Nonnull ItemStack item) {
		return isUnstackable(item) && Random.chance(settings.TWEAK_KEEP_INVENTORY_UNSTACKABLES.get());
	}

	/**
	 * Checks if the potential stackable item should be kept.
	 *
	 * @param item the item
	 * @return true if the item should be kept
	 */
	private boolean keepStackable(@Nonnull ItemStack item) {
		return !isUnstackable(item) && settings.TWEAK_KEEP_INVENTORY_STACKABLES.get() > 0;
	}

	/**
	 * Checks if the item is an unstackable item (stack size = 1).
	 *
	 * @param item the item
	 * @return true if the item is unstackable
	 */
	private boolean isUnstackable(@Nonnull ItemStack item) {
		return item.getMaxStackSize() == 1;
	}
}
