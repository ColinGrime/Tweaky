package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterBottleCraftTweak extends Tweak {

	public WaterBottleCraftTweak(@Nonnull Tweaky plugin) {
		super(plugin, "water_bottle_craft");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_WATER_BOTTLE_CRAFT.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_WATER_BOTTLE_CRAFT.get().material(Material.SPLASH_POTION);
	}

	@Override
	public void init() {
		ItemStack item = new ItemStack(Material.SPLASH_POTION, settings.TWEAK_WATER_BOTTLE_CRAFT_AMOUNT.get());
		if (item.getItemMeta() == null || !(item.getItemMeta() instanceof PotionMeta meta)) {
			return;
		}

		meta.setMaxStackSize(settings.TWEAK_WATER_BOTTLE_CRAFT_AMOUNT.get());
		meta.setBasePotionType(PotionType.WATER);
		item.setItemMeta(meta);

		NamespacedKey key = new NamespacedKey(plugin, "water_bottles");
		ShapelessRecipe recipe = new ShapelessRecipe(key, item);
		recipe.addIngredient(Material.WATER_BUCKET);
		Bukkit.addRecipe(recipe);
	}

	@Override
	public void shutdown() {
		Bukkit.removeRecipe(new NamespacedKey(plugin, "water_bottles"));
	}

	@Override
	public boolean hasPermission(@Nullable Entity entity) {
		return true;
	}

	@EventHandler
	public void onCraftItem(@Nonnull CraftItemEvent event) {
		if (!(event.getRecipe() instanceof ShapelessRecipe recipe) || !recipe.getKey().getKey().equals("water_bottles")) {
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
