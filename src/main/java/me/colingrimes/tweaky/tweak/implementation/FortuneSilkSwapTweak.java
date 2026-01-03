package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.config.implementation.Messages;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.Tweak;
import me.colingrimes.tweaky.util.bukkit.Blocks;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.NBT;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.view.AnvilView;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;

public class FortuneSilkSwapTweak extends Tweak {

	enum Type {
		Fortune(msg -> Text.color(" " + msg.TWEAK_FORTUNE_SUFFIX.toText())),
		SilkTouch(msg -> Text.color(" " + msg.TWEAK_SILK_TOUCH_SUFFIX.toText())),
		Reset(__ -> "");

		private final Function<Messages, String> name;

		Type(@Nonnull Function<Messages, String> name) {
			this.name = name;
		}

		@Nonnull
		public String name(@Nonnull Messages msg) {
			return name.apply(msg);
		}
	}

	public FortuneSilkSwapTweak(@Nonnull Tweaky plugin) {
		super(plugin, "fortune_silk_swap");
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_FORTUNE_SILK_SWAP.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_FORTUNE_SILK_SWAP.get().material(Material.NETHERITE_PICKAXE);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPrepareAnvil(@Nonnull PrepareAnvilEvent event) {
		// Resets any custom names from the tweak.
		ItemStack first = event.getInventory().getItem(0);
		if (first != null) {
			resetName(first);
		}

		// Renames the result item accordingly.
		ItemStack result = event.getResult();
		if (result != null && NBT.hasTag(result.getItemMeta(), id, Boolean.class)) {
			Boolean fortune = NBT.getTag(result.getItemMeta(), id, Boolean.class).orElseThrow();
			setType(result, fortune ? Type.Fortune : Type.SilkTouch);
		}

		AnvilView view = event.getView();
		ItemStack target = result != null ? result : first;
		ItemStack second = event.getInventory().getItem(1);
		if (!hasPermission(view.getPlayer()) || target == null || second == null) {
			return;
		}

		// Pick, Axe, Shovel, Hoe
		Material type = target.getType();
		if (!Tag.ITEMS_PICKAXES.isTagged(type) &&
				!Tag.ITEMS_AXES.isTagged(type) &&
				!Tag.ITEMS_SHOVELS.isTagged(type) &&
				!Tag.ITEMS_HOES.isTagged(type)) {
			return;
		}

		// Fortune or Silk required.
		Map<Enchantment, Integer> enchs = getEnchantments(second);
		if (!enchs.containsKey(Enchantment.FORTUNE) && !enchs.containsKey(Enchantment.SILK_TOUCH)) {
			return;
		}

		ItemStack copy = target.clone();
		for (var ench : enchs.entrySet()) {
			// Adding Fortune onto a Silk Touch
			if (copy.containsEnchantment(Enchantment.SILK_TOUCH) && ench.getKey() == Enchantment.FORTUNE) {
				copy.addEnchantment(ench.getKey(), Math.max(copy.getEnchantmentLevel(ench.getKey()), ench.getValue()));
			}
			// Adding Silk Touch onto a Fortune
			if (copy.containsEnchantment(Enchantment.FORTUNE) && ench.getKey() == Enchantment.SILK_TOUCH) {
				copy.addEnchantment(ench.getKey(), Math.max(copy.getEnchantmentLevel(ench.getKey()), ench.getValue()));
			}
		}

		// Make sure it has both enchants.
		if (!copy.containsEnchantment(Enchantment.FORTUNE) || !copy.containsEnchantment(Enchantment.SILK_TOUCH)) {
			return;
		}

		// Has to have a Display Name set so we can later add a suffix indicator.
		String renameText = view.getRenameText().isEmpty() ? "&b" + Text.format(copy.getType().name()) : view.getRenameText();
		Items.rename(copy, renameText);

		Boolean fortune = NBT.getTag(copy.getItemMeta(), id, Boolean.class).orElse(null);
		if (fortune != null) {
			setType(copy, fortune ? Type.Fortune : Type.SilkTouch);
		} else {
			setType(copy, Type.Fortune);
		}

		event.setResult(copy);
		Bukkit.getScheduler().runTask(plugin, () -> view.setRepairCost(settings.TWEAK_FORTUNE_SILK_SWAP_COST.get()));
	}

	@EventHandler
	public void onPrepareGrindstone(@Nonnull PrepareGrindstoneEvent event) {
		ItemStack result = event.getResult();
		if (result != null && NBT.hasTag(result.getItemMeta(), id, Boolean.class)) {
			setType(result, Type.Reset);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(@Nonnull BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Boolean fortune = NBT.getTag(item.getItemMeta(), id, Boolean.class).orElse(null);
		if (!hasPermission(player) || fortune == null) {
			return;
		}

		ItemStack copy = item.clone();
		if (fortune.equals(true)) {
			copy.removeEnchantment(Enchantment.SILK_TOUCH);
		} else {
			copy.removeEnchantment(Enchantment.FORTUNE);
		}

		Items.damage(item, player);
		event.getBlock().breakNaturally(copy, false, true);
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerInteract(@Nonnull PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || event.getHand() != EquipmentSlot.HAND || !event.getAction().name().startsWith("RIGHT_CLICK")) {
			return;
		}

		if (Blocks.isInteractable(event.getClickedBlock())) {
			return;
		}

		// Sets the next type if it is a Fortune Silk item.
		ItemStack item = player.getInventory().getItemInMainHand();
		NBT.getTag(item.getItemMeta(), id, Boolean.class).ifPresent(fortune -> setType(item, fortune ? Type.SilkTouch : Type.Fortune));
	}

	/**
	 * Gets the map of enchantments from the item.
	 *
	 * @param item the item
	 * @return the map of enchantments
	 */
	@Nonnull
	private Map<Enchantment, Integer> getEnchantments(@Nonnull ItemStack item) {
		if (item.getItemMeta() instanceof EnchantmentStorageMeta meta) {
			return meta.getStoredEnchants();
		}
		return item.getEnchantments();
	}

	/**
	 * Sets the Silk or Fortune type of the item.
	 *
	 * @param item the item
	 * @param type the type to set
	 */
	private void setType(@Nonnull ItemStack item, @Nonnull Type type) {
		ItemMeta meta = resetName(item);
		meta.setDisplayName(Text.color(meta.getDisplayName() + type.name(msg)));
		switch (type) {
			case Fortune -> NBT.setTag(meta, id, true);
			case SilkTouch -> NBT.setTag(meta, id, false);
			case Reset -> NBT.removeTag(meta, id);
		}
		item.setItemMeta(meta);
	}

	/**
	 * Resets the custom names used in this tweak.
	 *
	 * @param item the item to reset
	 * @return the item meta used
	 */
	@Nonnull
	private ItemMeta resetName(@Nonnull ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasDisplayName()) {
			return meta;
		}

		String name = meta.getDisplayName()
				.replace(Type.Fortune.name(msg), "")
				.replace(Type.SilkTouch.name(msg), "")
				.replace(ChatColor.stripColor(Type.Fortune.name(msg)), "")
				.replace(ChatColor.stripColor(Type.SilkTouch.name(msg)), "");
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return meta;
	}
}
