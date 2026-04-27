package me.colingrimes.tweaky.menu.tweak.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.Slot;
import me.colingrimes.tweaky.menu.tweak.TweakMenuCategory;
import me.colingrimes.tweaky.scheduler.Scheduler;
import me.colingrimes.tweaky.scheduler.task.Task;
import me.colingrimes.tweaky.tweak.properties.TweakCategory;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Responsible for showing a preview of a {@link RecipeTweak} to a player.
 */
public class TweakRecipePreview extends Gui {

	private static final List<Material> FURNACE_MATERIALS = List.of(
			Material.CHARCOAL,
			Material.COAL,
			Material.COAL_BLOCK,
			Material.LAVA_BUCKET
	);

	private final Recipe recipe;
	private final List<PreviewRotation> rotations = new ArrayList<>();
	private Task task;

	/**
	 * Gets the recipe preview of the tweak if a recipe exists.
	 *
	 * @param plugin the plugin
	 * @param player the player
	 * @param tweak the recipe tweak
	 * @return the recipe preview if available
	 */
	@Nonnull
	public static Optional<TweakRecipePreview> of(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull RecipeTweak tweak) {
		if (tweak.getRecipe().isPresent()) {
			return Optional.of(new TweakRecipePreview(plugin, player, tweak.getRecipe().get()));
		} else {
			return Optional.empty();
		}
	}

	private TweakRecipePreview(@Nonnull Tweaky plugin, @Nonnull Player player, @Nonnull Recipe recipe) {
		super(plugin, player, RecipeUtil.getInventoryTypeFromRecipe(recipe), "Preview");
		this.recipe = recipe;
	}

	@Override
	protected void onOpen() {
		task = Scheduler.sync().runRepeating(this::showNextPreview, 10L, 10L);
	}

	@Override
	protected void onClose() {
		task.stop();
		TweakMenuCategory.of(plugin, player, TweakCategory.RECIPES).open();
	}

	@Override
	public void draw() {
		switch (inventory.getType()) {
			case WORKBENCH -> drawWorkbench();
			case FURNACE -> drawFurance();
		}
	}

	/**
	 * Shows the next materials in the recipe preview.
	 */
	public void showNextPreview() {
		rotations.forEach(PreviewRotation::showNextPreview);
	}

	/**
	 * Draws the recipe preview for crafting recipes.
	 */
	private void drawWorkbench() {
		if (recipe instanceof ShapedRecipe shaped) {
			String[] shape = shaped.getShape();
			for (int i=0; i<shape.length; i++) {
				for (int j=0; j<shape[i].length(); j++) {
					char ch = shape[i].charAt(j);
					if (shaped.getChoiceMap().get(ch) != null) {
						rotations.add(new PreviewRotation(getSlot(i*3+j+1), shaped.getChoiceMap().get(ch)));
					}
				}
			}
		}

		if (recipe instanceof ShapelessRecipe shapeless) {
			List<ItemStack> ingredients = shapeless.getIngredientList();
			for (int i=0; i<ingredients.size(); i++) {
				rotations.add(new PreviewRotation(getSlot(i+1), shapeless.getChoiceList().get(i)));
			}
		}

		getSlot(0).setItem(recipe.getResult());
	}

	/**
	 * Draws the recipe preview for furance recipes.
	 */
	private void drawFurance() {
		if (!(recipe instanceof FurnaceRecipe furnance)) {
			return;
		}

		rotations.add(new PreviewRotation(getSlot(0), furnance.getInputChoice()));
		rotations.add(new PreviewRotation(getSlot(1), FURNACE_MATERIALS));
		getSlot(2).setItem(recipe.getResult());
	}

	private static class PreviewRotation {

		private final Slot slot;
		private final List<Material> choices;
		private int currentChoice = 0;

		public PreviewRotation(@Nonnull Slot slot, @Nonnull RecipeChoice recipeChoice) {
			this(slot, recipeChoice instanceof RecipeChoice.MaterialChoice materialChoice ? materialChoice.getChoices() : new ArrayList<>());
		}

		public PreviewRotation(@Nonnull Slot slot, @Nonnull List<Material> choices) {
			this.slot = slot;
			this.choices = choices;
			this.showNextPreview();
		}

		/**
		 * Shows the next material in the recipe preview.
		 */
		public void showNextPreview() {
			// Reset to the first material.
			if (currentChoice == choices.size()) {
				currentChoice = 0;
			}

			slot.setItem(choices.get(currentChoice));
			currentChoice += 1;
		}
	}
}
