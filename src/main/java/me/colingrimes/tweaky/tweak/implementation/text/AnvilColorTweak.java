package me.colingrimes.tweaky.tweak.implementation.text;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.tweak.event.TweakHandler;
import me.colingrimes.tweaky.tweak.type.DefaultTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import javax.annotation.Nonnull;

public class AnvilColorTweak extends DefaultTweak {

	public AnvilColorTweak(@Nonnull Tweaky plugin) {
		super(plugin, "anvil_color");
	}

	@TweakHandler
	public void onPrepareAnvil(@Nonnull PrepareAnvilEvent event) {
		String renameText = event.getView().getRenameText();
		if (event.getResult() != null && renameText != null && renameText.contains("&")) {
			event.setResult(Items.rename(event.getResult(), renameText));
		}
	}
}
