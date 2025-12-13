package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Items;
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

	@EventHandler
	public void onPrepareAnvil(@Nonnull PrepareAnvilEvent event) {
		String renameText = event.getView().getRenameText();
		if (event.getResult() != null && renameText != null && renameText.contains("&")) {
			event.setResult(Items.rename(event.getResult(), renameText));
		}
	}
}
