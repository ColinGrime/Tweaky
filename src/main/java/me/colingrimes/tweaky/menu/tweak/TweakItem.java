package me.colingrimes.tweaky.menu.tweak;

import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TweakItem extends Items.Builder {

	private final List<String> usage = new ArrayList<>();

	@Nonnull
	public static TweakItem of(@Nonnull Material def) {
		return new TweakItem(def);
	}

	public TweakItem(@Nonnull Material def) {
		super(def);
	}

	@Nonnull
	@Override
	public TweakItem name(@Nullable String name) {
		super.name(name);
		return this;
	}

	@Nonnull
	@Override
	public TweakItem lore() {
		super.lore();
		return this;
	}

	@Nonnull
	@Override
	public TweakItem lore(@Nullable String line) {
		super.lore(line);
		return this;
	}

	/**
	 * Adds a line to the usage message.
	 *
	 * @param line the line you want to add to the usage
	 * @return the tweak item object
	 */
	@Nonnull
	public TweakItem usage(@Nonnull String line) {
		usage.add(line);
		return this;
	}

	/**
	 * Sends the usage messages to the player.
	 *
	 * @param player the player
	 */
	public void sendUsage(@Nonnull HumanEntity player) {
		if (usage.isEmpty()) {
			return;
		}

		player.sendMessage("");
		Text.color(usage).forEach(player::sendMessage);
		player.sendMessage("");
	}
}