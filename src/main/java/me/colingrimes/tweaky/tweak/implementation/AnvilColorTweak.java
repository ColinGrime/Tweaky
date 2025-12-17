package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import javax.annotation.Nonnull;

public class AnvilColorTweak extends Tweak {

	public AnvilColorTweak(@Nonnull Tweaky plugin) {
		super(plugin, "anvil_color");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_ANVIL_COLOR.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return TweakItem
				.of(Material.ANVIL)
				.name("&aColored Names")
				.lore("&7Items can be colored in the anvil.")
				.lore()
				.lore("&8Minecraft Colors:")
				.lore("&00&11&22&33&44&55&66&77&88&99&aa&bb&cc&dd&ee&ff")
				.lore("&fk &7→ &k12345  &fl &7→ &lBold")
				.lore("&fm &7→ &mStrike&r  &fn &7→ &nUline")
				.lore("&fo &7→ &oItalic&r   &fr &7→ Reset")
				.usage("&eUsage: &aPlace an item in an Anvil with the listed color codes.");
	}

	@EventHandler
	public void onPrepareAnvil(@Nonnull PrepareAnvilEvent event) {
		String renameText = event.getView().getRenameText();
		if (event.getResult() != null && renameText != null && renameText.contains("&")) {
			event.setResult(Items.rename(event.getResult(), renameText));
		}
	}
}
