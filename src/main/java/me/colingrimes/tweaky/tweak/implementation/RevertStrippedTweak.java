package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.event.EventHandler;

import javax.annotation.Nonnull;

public class RevertStrippedTweak extends Tweak {

	public RevertStrippedTweak(@Nonnull Tweaky plugin) {
		super(plugin, "revert_stripped");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_REVERT_STRIPPED.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.STRIPPED_OAK_LOG)
				.name("&aRevert Stripped &8(Sneak Right Click)")
				.lore("&7Goes back to previous Log or Stem variant.")
				.lore()
				.lore("&8Requires:")
				.lore(" &7Axe &8(Any)")
				.usage("&eUsage: &aSneak Right Click on a Stripped Log or Stem with an Axe to revert it back to its previous variant.");
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		if (!event.isShiftRightClick() || !Tag.ITEMS_AXES.isTagged(event.getItemType())) {
			return;
		}

		Material unstripped = getUnstripped(event.getBlockType());
		if (unstripped == null) {
			return;
		}

		Items.damage(event.getItem(), event.getPlayer());
		event.getPlayer().swingMainHand();
		event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_AXE_STRIP, 1F, 1F);
		event.getBlock().setType(unstripped);
		event.setCancelled(true);
	}

	/**
	 * Gets the unstripped version of the stripped log or stem.
	 *
	 * @param stripped the stripped log or stem
	 * @return the unstripped log or stem
	 */
	public static Material getUnstripped(Material stripped) {
		return switch (stripped) {
			// Logs
			case STRIPPED_OAK_LOG -> Material.OAK_LOG;
			case STRIPPED_SPRUCE_LOG -> Material.SPRUCE_LOG;
			case STRIPPED_BIRCH_LOG -> Material.BIRCH_LOG;
			case STRIPPED_JUNGLE_LOG -> Material.JUNGLE_LOG;
			case STRIPPED_ACACIA_LOG -> Material.ACACIA_LOG;
			case STRIPPED_DARK_OAK_LOG -> Material.DARK_OAK_LOG;
			case STRIPPED_MANGROVE_LOG -> Material.MANGROVE_LOG;
			case STRIPPED_CHERRY_LOG -> Material.CHERRY_LOG;
			case STRIPPED_PALE_OAK_LOG -> Material.PALE_OAK_LOG;
			// Wood
			case STRIPPED_OAK_WOOD -> Material.OAK_WOOD;
			case STRIPPED_SPRUCE_WOOD -> Material.SPRUCE_WOOD;
			case STRIPPED_BIRCH_WOOD -> Material.BIRCH_WOOD;
			case STRIPPED_JUNGLE_WOOD -> Material.JUNGLE_WOOD;
			case STRIPPED_ACACIA_WOOD -> Material.ACACIA_WOOD;
			case STRIPPED_DARK_OAK_WOOD -> Material.DARK_OAK_WOOD;
			case STRIPPED_MANGROVE_WOOD -> Material.MANGROVE_WOOD;
			case STRIPPED_CHERRY_WOOD -> Material.CHERRY_WOOD;
			case STRIPPED_PALE_OAK_WOOD -> Material.PALE_OAK_WOOD;
			// Stem
			case STRIPPED_WARPED_STEM -> Material.WARPED_STEM;
			case STRIPPED_CRIMSON_STEM -> Material.CRIMSON_STEM;
			// Hyphae
			case STRIPPED_WARPED_HYPHAE -> Material.WARPED_HYPHAE;
			case STRIPPED_CRIMSON_HYPHAE -> Material.CRIMSON_HYPHAE;
			// Bamboo
			case STRIPPED_BAMBOO_BLOCK -> Material.BAMBOO_BLOCK;
			// Default
			default -> null;
		};
	}

}
