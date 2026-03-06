package me.colingrimes.tweaky.tweak.implementation.block;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.CauldronTweak;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CauldronConcreteTweak extends CauldronTweak {

	public CauldronConcreteTweak(@Nonnull Tweaky plugin) {
		super(plugin, "cauldron_concrete", CauldronConcreteTweak::getConcrete, plugin.getSettings().TWEAK_CAULDRON_CONCRETE_USE_WATER);
	}

	/**
	 * Converts concrete powder into its corresponding concrete form.
	 *
	 * @param powder the concrete powder
	 * @return the concrete
	 */
	@Nullable
	private static Material getConcrete(@Nonnull Material powder) {
		return switch (powder) {
			case WHITE_CONCRETE_POWDER      -> Material.WHITE_CONCRETE;
			case LIGHT_GRAY_CONCRETE_POWDER -> Material.LIGHT_GRAY_CONCRETE;
			case GRAY_CONCRETE_POWDER       -> Material.GRAY_CONCRETE;
			case BLACK_CONCRETE_POWDER      -> Material.BLACK_CONCRETE;
			case BROWN_CONCRETE_POWDER      -> Material.BROWN_CONCRETE;
			case RED_CONCRETE_POWDER        -> Material.RED_CONCRETE;
			case ORANGE_CONCRETE_POWDER     -> Material.ORANGE_CONCRETE;
			case YELLOW_CONCRETE_POWDER     -> Material.YELLOW_CONCRETE;
			case LIME_CONCRETE_POWDER       -> Material.LIME_CONCRETE;
			case GREEN_CONCRETE_POWDER      -> Material.GREEN_CONCRETE;
			case CYAN_CONCRETE_POWDER       -> Material.CYAN_CONCRETE;
			case LIGHT_BLUE_CONCRETE_POWDER -> Material.LIGHT_BLUE_CONCRETE;
			case BLUE_CONCRETE_POWDER       -> Material.BLUE_CONCRETE;
			case PURPLE_CONCRETE_POWDER     -> Material.PURPLE_CONCRETE;
			case MAGENTA_CONCRETE_POWDER    -> Material.MAGENTA_CONCRETE;
			case PINK_CONCRETE_POWDER       -> Material.PINK_CONCRETE;
			default -> null;
		};
	}
}
