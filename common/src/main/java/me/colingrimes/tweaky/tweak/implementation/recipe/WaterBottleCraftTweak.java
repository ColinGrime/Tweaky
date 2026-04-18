package me.colingrimes.tweaky.tweak.implementation.recipe;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.type.RecipeTweak;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;

public class WaterBottleCraftTweak extends RecipeTweak {

	public WaterBottleCraftTweak(@Nonnull Tweaky plugin) {
		super(plugin, "water_bottle_craft");
	}

	@Nonnull
	@Override
	protected Recipe recipe(@Nonnull NamespacedKey key) {
		ItemStack item = new ItemStack(Material.SPLASH_POTION, settings.TWEAK_WATER_BOTTLE_CRAFT_AMOUNT.get());
		if (item.getItemMeta() == null || !(item.getItemMeta() instanceof PotionMeta meta)) {
			throw new IllegalStateException("Item meta is not a potion.");
		}

		meta.setMaxStackSize(settings.TWEAK_WATER_BOTTLE_CRAFT_AMOUNT.get());
		meta.setBasePotionType(PotionType.WATER);
		item.setItemMeta(meta);

		ShapelessRecipe recipe = new ShapelessRecipe(key, item);
		recipe.addIngredient(Material.WATER_BUCKET);
		return recipe;
	}

	@EventHandler
	public void onCraftItem(@Nonnull CraftItemEvent event) {
		if (!(event.getRecipe() instanceof ShapelessRecipe recipe) || !recipe.getKey().equals(RECIPE_KEY)) {
			return;
		}

		// Remove the whole water bucket.
		CraftingInventory inventory = event.getInventory();
		for (int i=0; i<inventory.getMatrix().length; i++) {
			ItemStack item = inventory.getMatrix()[i];
			if (item != null && item.getType() == Material.WATER_BUCKET) {
				item.setAmount(item.getAmount() - 1);
				break;
			}
		}
	}
}
