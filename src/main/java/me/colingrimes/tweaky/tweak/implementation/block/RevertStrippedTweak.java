package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.tweak.properties.TweakProperties;
import me.colingrimes.tweaky.util.bukkit.Players;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class RevertStrippedTweak extends DefaultTweak {

	public RevertStrippedTweak(@Nonnull Tweaky plugin) {
		super(plugin, "revert_stripped");
	}

	@Override
	protected void configureProperties(@Nonnull TweakProperties properties) {
		properties.getGuard()
				.sneaking()
				.rightClick()
				.item(Tag.ITEMS_AXES)
				.block(b -> getUnstripped(b.getType()) != null);
	}

	@TweakHandler(ignoreCancelled = true)
	public void onPlayerIntearact(@Nonnull PlayerInteractBlockEvent event) {
		Players.use(event.getPlayer(), event.getHand(), Sound.ITEM_AXE_STRIP, event.getBlock().getLocation());
		event.getBlock().setType(Objects.requireNonNull(getUnstripped(event.getBlockType())));
		event.setCancelled(true);
	}

	/**
	 * Gets the unstripped version of the stripped log or stem.
	 *
	 * @param stripped the stripped log or stem
	 * @return the unstripped log or stem
	 */
	@Nullable
	private Material getUnstripped(@Nonnull Material stripped) {
		return switch (stripped) {
			// Logs
			case STRIPPED_OAK_LOG        -> Material.OAK_LOG;
			case STRIPPED_SPRUCE_LOG     -> Material.SPRUCE_LOG;
			case STRIPPED_BIRCH_LOG      -> Material.BIRCH_LOG;
			case STRIPPED_JUNGLE_LOG     -> Material.JUNGLE_LOG;
			case STRIPPED_ACACIA_LOG     -> Material.ACACIA_LOG;
			case STRIPPED_DARK_OAK_LOG   -> Material.DARK_OAK_LOG;
			case STRIPPED_MANGROVE_LOG   -> Material.MANGROVE_LOG;
			case STRIPPED_CHERRY_LOG     -> Material.CHERRY_LOG;
			case STRIPPED_PALE_OAK_LOG   -> Material.PALE_OAK_LOG;
			// Wood
			case STRIPPED_OAK_WOOD       -> Material.OAK_WOOD;
			case STRIPPED_SPRUCE_WOOD    -> Material.SPRUCE_WOOD;
			case STRIPPED_BIRCH_WOOD     -> Material.BIRCH_WOOD;
			case STRIPPED_JUNGLE_WOOD    -> Material.JUNGLE_WOOD;
			case STRIPPED_ACACIA_WOOD    -> Material.ACACIA_WOOD;
			case STRIPPED_DARK_OAK_WOOD  -> Material.DARK_OAK_WOOD;
			case STRIPPED_MANGROVE_WOOD  -> Material.MANGROVE_WOOD;
			case STRIPPED_CHERRY_WOOD    -> Material.CHERRY_WOOD;
			case STRIPPED_PALE_OAK_WOOD  -> Material.PALE_OAK_WOOD;
			// Stem
			case STRIPPED_WARPED_STEM    -> Material.WARPED_STEM;
			case STRIPPED_CRIMSON_STEM   -> Material.CRIMSON_STEM;
			// Hyphae
			case STRIPPED_WARPED_HYPHAE  -> Material.WARPED_HYPHAE;
			case STRIPPED_CRIMSON_HYPHAE -> Material.CRIMSON_HYPHAE;
			// Bamboo
			case STRIPPED_BAMBOO_BLOCK   -> Material.BAMBOO_BLOCK;
			// Default
			default -> null;
		};
	}
}
