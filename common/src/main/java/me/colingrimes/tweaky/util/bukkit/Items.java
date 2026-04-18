package me.colingrimes.tweaky.util.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import me.colingrimes.tweaky.message.Message;
import me.colingrimes.tweaky.message.Placeholders;
import me.colingrimes.tweaky.util.misc.Random;
import me.colingrimes.tweaky.util.text.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public final class Items {

	/**
	 * Creates a new {@link Builder} object.
	 * <p>
	 * Defaults to {@link Material#STONE}.
	 *
	 * @return the item builder object
	 */
	@Nonnull
	public static Builder create() {
		return new Builder(Material.STONE);
	}

	/**
	 * Creates a new {@link Builder} object.
	 *
	 * @param material the default material of the item
	 * @return the item builder object
	 */
	@Nonnull
	public static Builder of(@Nonnull Material material) {
		return new Builder(material);
	}

	/**
	 * Creates a new {@link Builder} object.
	 * <p>
	 * This will use the provided item as a base for the item builder.
	 *
	 * @param item the item stack
	 * @return the item builder object
	 */
	@Nonnull
	public static Builder of(@Nonnull ItemStack item) {
		return new Builder(item);
	}

	/**
	 * Renames the item with the given name. Supports color codes.
	 *
	 * @param item the item to rename
	 * @param name the name
	 * @return the renamed item
	 */
	@Nonnull
	public static ItemStack rename(@Nonnull ItemStack item, @Nonnull String name) {
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(Text.color(name));
			item.setItemMeta(meta);
		}
		return item;
	}

	/**
	 * Attempts to give the player the item.
	 * If there is no room in the player's inventory, the item will be dropped on the ground.
	 *
	 * @param player the player
	 * @param item the item to give to the player
	 */
	public static void give(@Nonnull Player player, @Nonnull ItemStack item) {
		Collection<ItemStack> remaining = player.getInventory().addItem(item).values();
		for (ItemStack itemDrop : remaining) {
			drop(itemDrop, player.getLocation());
		}
	}

	/**
	 * Removes a single item from the stack.
	 *
	 * @param item the item stack
	 */
	public static void remove(@Nonnull ItemStack item) {
		item.setAmount(item.getAmount() - 1);
	}

	/**
	 * Damages the specified item by 1 durability.
	 *
	 * @param item the item to damage
	 * @param player the player to play the damage sound if applicable
	 */
	public static void damage(@Nullable ItemStack item, @Nonnull Player player) {
		if (item == null || !(item.getItemMeta() instanceof Damageable damageable)) {
			return;
		}

		int unbreaking = item.getEnchantmentLevel(Enchantment.UNBREAKING);
		if (!Random.chance(100.0 / (unbreaking + 1))) {
			return;
		}

		int damage = damageable.getDamage() + 1;
		int maxDamage = getMaxDurability(item);

		if (damage < maxDamage) {
			damageable.setDamage(damage);
			item.setItemMeta(damageable);
		} else {
			remove(item);
			Sounds.play(player, Sound.ENTITY_ITEM_BREAK);
		}
	}

	/**
	 * Gets the maximum durability of the specified item.
	 *
	 * @param item the item
	 * @return the max durability of the item
	 */
	public static int getMaxDurability(@Nonnull ItemStack item) {
		if (!(item.getItemMeta() instanceof Damageable damageable)) {
			return 0;
		}

		if (damageable.hasMaxDamage()) {
			return damageable.getMaxDamage();
		} else {
			return item.getType().getMaxDurability();
		}
	}

	/**
	 * Uses the item. This damages (or removes) the item and plays the given use sound.
	 *
	 * @param item the item to use
	 * @param player the player using the item
	 * @param useSound the sound to play when using the item
	 * @param location the location to play the sound
	 */
	public static void use(@Nonnull ItemStack item, @Nonnull Player player, @Nonnull Sound useSound, @Nonnull Location location) {
		if (item.getType().getMaxDurability() > 0) {
			damage(item, player);
		} else {
			remove(item);
		}
		Sounds.play(location, useSound);
	}

	/**
	 * Drops the item at the given location.
	 *
	 * @param item the item to drop
	 * @param location the location
	 */
	public static void drop(@Nonnull ItemStack item, @Nonnull Location location) {
		if (location.getWorld() != null) {
			location.getWorld().dropItemNaturally(location, item);
		}
	}

	/**
	 * Provides a simple way to build {@link ItemStack} objects:
	 * <ul>
	 *   <li>Supports parsing of {@link Material} enums from string formats.</li>
	 *   <li>Supports setting the colored name and lore of the item.</li>
	 *   <li>Supports hiding item attributes, glowing the item, and making it unbreakable. </li>
	 *   <li>Supports replacing placeholders from the name/lore.</li>
	 * </ul>
	 */
	public static class Builder {

		protected final Placeholders placeholders = Placeholders.create();
		private final Material defMaterial;
		private ItemStack baseItem;

		private Material material;
		private Component name;
		private List<Component> lore = new ArrayList<>();
		private boolean hide = false;
		private boolean glow = false;
		private boolean unbreakable = false;

		public Builder(@Nonnull Material def) {
			this.defMaterial = Objects.requireNonNull(def, "material");
			this.baseItem = null;
		}

		public Builder(@Nonnull ItemStack base) {
			Preconditions.checkNotNull(base.getItemMeta(), "Item meta is null.");
			this.defMaterial = null;
			this.baseItem = base;
			this.name = base.getItemMeta().hasDisplayName() ? base.getItemMeta().displayName() : null;
			this.lore = base.getItemMeta().hasLore() ? base.getItemMeta().lore() : new ArrayList<>();
		}

		/**
		 * Sets the {@link ItemStack} of the item.
		 *
		 * @param base the base item stack to build on
		 * @return the item builder object
		 */
		@Nonnull
		public Builder item(@Nullable ItemStack base) {
			this.baseItem = base;
			return this;
		}

		/**
		 * Sets the {@link Material} of the item.
		 *
		 * @param material the material you want the item to be
		 * @return the item builder object
		 */
		@Nonnull
		public Builder material(@Nullable Material material) {
			this.material = material;
			return this;
		}

		/**
		 * Sets the {@link Material} of the item.
		 *
		 * @param str the string containing the material name
		 * @return the item builder object
		 */
		@Nonnull
		public Builder material(@Nullable String str) {
			if (str == null) {
				return this;
			}

			this.material = Material.matchMaterial(str);
			return this;
		}

		/**
		 * Sets the name of the item.
		 *
		 * @param name the name you want the item to be
		 * @return the item builder object
		 */
		@Nonnull
		public Builder name(@Nullable Component name) {
			if (name == null) {
				return this;
			}

			this.name = name.decoration(TextDecoration.ITALIC, false);
			return this;
		}

		/**
		 * Sets the name of the item.
		 *
		 * @param name the name you want the item to be
		 * @return the item builder object
		 */
		@Nonnull
		public Builder name(@Nullable String name) {
			if (name == null) {
				return this;
			} else {
				return name(Message.LEGACY.deserialize(name));
			}
		}

		/**
		 * Adds a line to the lore.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder lore() {
			lore.add(Component.empty());
			return this;
		}

		/**
		 * Adds a line to the lore.
		 *
		 * @param line the line you want to add to the lore
		 * @return the item builder object
		 */
		@Nonnull
		public Builder lore(@Nullable String line) {
			if (line == null) {
				return this;
			}

			lore.add(Message.LEGACY.deserialize(line).decoration(TextDecoration.ITALIC, false));
			return this;
		}

		/**
		 * Sets the lore of the item.
		 *
		 * @param lore the lore you want the item to have
		 * @return the item builder object
		 */
		@Nonnull
		public Builder lore(@Nullable String[] lore) {
			if (lore == null) {
				return this;
			} else {
				return lore(List.of(lore));
			}
		}

		/**
		 * Sets the lore of the item.
		 *
		 * @param lore the lore you want the item to have
		 * @return the item builder object
		 */
		@Nonnull
		public Builder lore(@Nullable List<String> lore) {
			if (lore == null) {
				return this;
			}

			this.lore = lore.stream().map(l -> (Component) Message.LEGACY.deserialize(l).decoration(TextDecoration.ITALIC, false)).toList();
			return this;
		}

		/**
		 * Hides the attributes of the item.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder hide() {
			hide(true);
			return this;
		}

		/**
		 * Hides the attributes of the item.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder hide(boolean hide) {
			this.hide = hide;
			return this;
		}

		/**
		 * Makes the item glow.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder glow() {
			glow(true);
			return this;
		}

		/**
		 * Makes the item glow.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder glow(boolean glow) {
			this.glow = glow;
			return this;
		}

		/**
		 * Makes the item unbreakable.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder unbreakable() {
			unbreakable(true);
			return this;
		}

		/**
		 * Makes the item unbreakable.
		 *
		 * @return the item builder object
		 */
		@Nonnull
		public Builder unbreakable(boolean unbreakable) {
			this.unbreakable = unbreakable;
			return this;
		}

		/**
		 * Parses a {@link ConfigurationSection} and checks for the following:
		 * - A "type" or "material" key for materials.
		 * - A "name" key for the name of the item.
		 * - A "lore" key for the lore of the item.
		 * - A "glowing" key for whether the item should be glowing or not.
		 *
		 * @param sec the section of the configuration file
		 * @return the item builder object
		 */
		@Nonnull
		public Builder config(@Nullable ConfigurationSection sec) {
			if (sec == null) {
				return this;
			}

			parse(sec, "type").ifPresent(this::material);
			parse(sec, "material").ifPresent(this::material);
			parse(sec, "name").ifPresent(this::name);
			parse(sec, "lore").ifPresent(this::lore);
			parse(sec, "lore", List.class).ifPresent(this::lore);
			parse(sec, "hide", Boolean.class).ifPresent(this::hide);
			parse(sec, "glow", Boolean.class).ifPresent(this::glow);
			parse(sec, "unbreakable", Boolean.class).ifPresent(this::glow);
			return this;
		}

		/**
		 * Adds a placeholder to the list of placeholders.
		 *
		 * @param placeholder the placeholder you want to add
		 * @param replacement the value you want to replace the placeholder with
		 * @return the item builder object
		 */
		@Nonnull
		public <T> Builder placeholder(@Nonnull String placeholder, @Nonnull T replacement) {
			placeholders.add(placeholder, replacement);
			return this;
		}

		/**
		 * Builds the {@link ItemStack} item.
		 *
		 * @return the item
		 */
		@Nonnull
		public ItemStack build() {
			Material type = material != null ? material : defMaterial;
			ItemStack item = baseItem != null ? baseItem : new ItemStack(Objects.requireNonNull(type, "Material is null."));
			if (name != null) {
				placeholders.apply(name).setName(item);
			}
			if (lore != null && !lore.isEmpty()) {
				placeholders.applyComponents(lore).setLore(item);
			}

			ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Item meta is null.");
			if (hide) {
				meta.setAttributeModifiers(HashMultimap.create());
				meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
			}
			if (glow) {
				meta.addEnchant(Enchantment.INFINITY, 1, true);
				meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			if (unbreakable) {
				meta.setUnbreakable(true);
				meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			}

			item.setItemMeta(meta);
			return item;
		}

		/**
		 * Gets the string that corresponds to the inputted key.
		 *
		 * @param sec the section of the configuration file
		 * @param key the key of the string
		 * @return the value corresponding to the key
		 */
		@Nonnull
		private Optional<String> parse(@Nonnull ConfigurationSection sec, @Nonnull String key) {
			return Optional.ofNullable(sec.getString(key));
		}

		/**
		 * Gets the object that corresponds to the inputted key.
		 *
		 * @param sec the section of the configuration file
		 * @param key the key of the string
		 * @return the value corresponding to the key
		 */
		@Nonnull
		private <T> Optional<T> parse(@Nonnull ConfigurationSection sec, @Nonnull String key, @Nonnull Class<T> clazz) {
			return Optional.ofNullable(sec.getObject(key, clazz));
		}
	}
}
