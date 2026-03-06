package me.colingrimes.tweaky.tweak.properties;

/**
 * Represents a specific category that a tweak belongs to.
 */
public enum TweakCategory {

	/**
	 * Any tweak whose main purpose involves mobs.
	 */
	MOBS,

	/**
	 * Any tweak whose main purpose involves blocks.
	 */
	BLOCKS,

	/**
	 * Any tweak whose main purpose involves crops.
	 */
	CROPS,

	/**
	 * Any tweak whose main purpose is displaying text to the player.
	 */
	TEXT,

	/**
	 * Any tweak that saves the player time and effort.
	 * <p>
	 * Most tweaks will end up saving the player time, so this category is a last resort if none of the other ones fit better.
	 * <p>
	 * Additionally, if the tweak is more of a gimmick than anything, it should go in the {@link TweakCategory#MISCELLANEOUS} category instead.
	 */
	CONVENIENCE,

	/**
	 * Any tweak that adds a recipe to the game.
	 */
	RECIPES,

	/**
	 * Default category if none of the other categories fit better.
	 */
	MISCELLANEOUS,

	/**
	 * Tweak belongs to an unknown category.
	 */
	UNKNOWN;
}
