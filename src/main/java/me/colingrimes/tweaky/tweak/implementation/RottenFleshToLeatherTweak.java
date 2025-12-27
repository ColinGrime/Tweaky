package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RottenFleshToLeatherTweak extends Tweak {

	private final NamespacedKey ROTTEN_FLESH_TO_LEATHER_KEY;

	public RottenFleshToLeatherTweak(@Nonnull Tweaky plugin) {
		super(plugin, "rotten_flesh_to_leather");
		this.ROTTEN_FLESH_TO_LEATHER_KEY = new NamespacedKey(plugin, "rotten_flesh_to_leather");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ROTTEN_FLESH_TO_LEATHER.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_ROTTEN_FLESH_TO_LEATHER.get().material(Material.ROTTEN_FLESH);
	}

	@Override
	public void init() {
		FurnaceRecipe recipe = new FurnaceRecipe(
				ROTTEN_FLESH_TO_LEATHER_KEY,
				new ItemStack(Material.LEATHER),
				Material.ROTTEN_FLESH,
				0.7f,
				200
		);
		Bukkit.addRecipe(recipe);
	}

	@Override
	public void shutdown() {
		Bukkit.removeRecipe(ROTTEN_FLESH_TO_LEATHER_KEY);
	}

	@Override
	public boolean hasPermission(@Nullable Entity entity) {
		return true;
	}
}
