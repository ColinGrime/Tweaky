package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

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
		return TweakItem
				.of(Material.ROTTEN_FLESH)
				.name("&aRotten Flesh to Leather &8(Smelt)")
				.lore("&7Smelt Rotten Flesh into Leather.")
				.usage("&eUsage: &aAllows you to smelt Rotten Flesh into Leather.");
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
}
