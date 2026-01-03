package me.colingrimes.tweaky.menu.tweak;

import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TweakItem extends Items.Builder {

	private List<String> usage = new ArrayList<>();

	@Nonnull
	public static TweakItem create() {
		return of(Material.STONE);
	}

	@Nonnull
	public static TweakItem create(@Nonnull String message) {
		return create().name("&cUnknown Tweak").lore("&7" + message);
	}

	@Nonnull
	public static TweakItem of(@Nonnull Material material) {
		return new TweakItem(material);
	}

	public TweakItem(@Nonnull Material def) {
		super(def);
	}

	@Nonnull
	@Override
	public TweakItem item(@Nullable ItemStack base) {
		super.item(base);
		return this;
	}

	@Nonnull
	@Override
	public TweakItem material(@Nullable Material material) {
		super.material(material);
		return this;
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

	@Nonnull
	@Override
	public <T> TweakItem placeholder(@Nonnull String placeholder, @Nonnull T replacement) {
		super.placeholder(placeholder, replacement);
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
	 * Sets the usage message of the item.
	 *
	 * @param usage the usage message you want the item to have
	 * @return the tweak item object
	 */
	@Nonnull
	public TweakItem usage(@Nullable List<String> usage) {
		this.usage = usage;
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

		for (var entry : placeholders.entrySet()) {
			usage = usage.stream().map(l -> l.replace(entry.getKey(), entry.getValue())).toList();
		}

		player.sendMessage("");
		Text.color(usage).forEach(player::sendMessage);
		player.sendMessage("");
	}
}