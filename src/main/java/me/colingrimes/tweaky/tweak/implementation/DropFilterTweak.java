package me.colingrimes.tweaky.tweak.implementation;

import me.colingrimes.tweaky.Tweaky;
import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import me.colingrimes.tweaky.menu.Gui;
import me.colingrimes.tweaky.menu.tweak.TweakItem;
import me.colingrimes.tweaky.tweak.type.ToggleTweak;
import me.colingrimes.tweaky.util.bukkit.Items;
import me.colingrimes.tweaky.util.bukkit.NBT;
import me.colingrimes.tweaky.util.bukkit.Players;
import me.colingrimes.tweaky.util.text.Text;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class DropFilterTweak extends ToggleTweak {

	private static final String FILTER_MATERIALS_KEY = "drops_filter_materials";
	private final Map<UUID, List<Material>> filter = new HashMap<>();
	private final Map<UUID, Set<Material>> filterSet = new HashMap<>();

	public DropFilterTweak(@Nonnull Tweaky plugin) {
		super(plugin, "drops_filter", "drops", true);
	}

	@Override
	public boolean isEnabled() {
		return settings.TWEAK_DROPS_FILTER.get();
	}

	@Nonnull
	@Override
	public TweakItem getGuiItem() {
		return menus.TWEAK_DROPS_FILTER.get().material(Material.CAULDRON);
	}

	@Override
	public void init() {
		super.init();
		Players.forEach(this::loadFilter);
	}

	@Override
	public void shutdown() {
		super.shutdown();
		filter.clear();
		filterSet.clear();
	}

	@Override
	protected void activateTweak(@Nonnull Player player) {
		msg.TWEAK_FILTER_ON.send(player);
	}

	@Override
	protected void deactivateTweak(@Nonnull Player player) {
		msg.TWEAK_FILTER_OFF.send(player);
	}

	@EventHandler
	public void onPlayerJoin(@Nonnull PlayerJoinEvent event) {
		loadFilter(event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(@Nonnull PlayerInteractBlockEvent event) {
		Player player = event.getPlayer();
		if (!hasPermission(player) || !event.isRightClick() || !event.isBlock(Tag.CAULDRONS) || player.isSneaking()) {
			return;
		}

		ItemStack item = player.getInventory().getItemInMainHand();
		Material type = item.getType();
		if (type == Material.BUCKET ||
				type == Material.WATER_BUCKET ||
				type == Material.LAVA_BUCKET ||
				type == Material.POWDER_SNOW_BUCKET ||
				type == Material.GLASS_BOTTLE ||
				(item.getItemMeta() instanceof PotionMeta potion && potion.getBasePotionType() == PotionType.WATER)) {
			return;
		}

		player.swingMainHand();
		new FilterMenu(player).open();
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityPickupItem(@Nonnull EntityPickupItemEvent event) {
		if (!hasPermission(event.getEntity()) || !isToggled(event.getEntity())) {
			return;
		}

		UUID uuid = event.getEntity().getUniqueId();
		if (filterSet.containsKey(uuid) && filterSet.get(uuid).contains(event.getItem().getItemStack().getType())) {
			event.setCancelled(true);
		}
	}

	private class FilterMenu extends Gui {
		public FilterMenu(@Nonnull Player player) {
			super(player, menus.FILTER_MENU_TITLE.toText(), 6);
		}

		@Override
		public void draw() {
			List<Material> materials = filter.get(player.getUniqueId());
			for (int i=0; i<Math.min(54, materials.size()); i++) {
				Material type = materials.get(i);
				String name = menus.FILTER_MENU_ITEM_NAME.replace("{item}", Text.format(type.name())).toText();
				List<String> lore = menus.FILTER_MENU_ITEM_LORE.toTextList();
				ItemStack preview = Items.of(type).name(name).lore(lore).build();
				getSlot(i).setItem(preview).bind(this::remove, ClickType.LEFT, ClickType.RIGHT);
			}

			for (int i=0; i<player.getInventory().getSize(); i++) {
				int playerSlot = inventory.getSize() + i;
				getSlot(playerSlot).bind(e -> add(e.getCurrentItem()), ClickType.LEFT, ClickType.RIGHT);
			}
		}

		/**
		 * Adds the player's item to the filter.
		 *
		 * @param item the item to add to the filter
		 */
		private void add(@Nullable ItemStack item) {
			if (item == null || item.getType().isAir()) {
				return;
			}

			if (filterSet.get(player.getUniqueId()).contains(item.getType())) {
				msg.TWEAK_FILTER_EXISTS.send(player);
				return;
			}

			for (int i=0; i<inventory.getSize(); i++) {
				if (getSlot(i).getItem() != null) {
					continue;
				}

				Material type = item.getType();
				String name = menus.FILTER_MENU_ITEM_NAME.replace("{item}", Text.format(type.name())).toText();
				List<String> lore = menus.FILTER_MENU_ITEM_LORE.toTextList();
				ItemStack preview = Items.of(type).name(name).lore(lore).build();
				getSlot(i).setItem(preview).bind(this::remove, ClickType.LEFT, ClickType.RIGHT);

				msg.TWEAK_FILTER_ADD.replace("{item}", Text.format(type.name())).send(player);
				filter.get(player.getUniqueId()).add(type);
				filterSet.get(player.getUniqueId()).add(type);
				saveFilter(player);
				return;
			}

			msg.TWEAK_FILTER_FULL.send(player);
		}

		/**
		 * Removes an item from the filter, shifting all items to the left.
		 *
		 * @param event the click event
		 */
		private void remove(@Nonnull InventoryClickEvent event) {
			Material type = event.getCurrentItem() != null ? event.getCurrentItem().getType() : null;
			if (type == null) {
				return;
			}

			for (int i=event.getRawSlot(); i<inventory.getSize(); i++) {
				ItemStack next = i+1 < inventory.getSize() ? getSlot(i+1).getItem() : null;
				if (next != null) {
					getSlot(i).setItem(next);
				} else {
					getSlot(i).setItem((ItemStack) null);
				}
			}

			msg.TWEAK_FILTER_REMOVE.replace("{item}", Text.format(type.name())).send(player);
			filter.get(player.getUniqueId()).remove(type);
			filterSet.get(player.getUniqueId()).remove(type);
			saveFilter(player);

		}
	}

	// The Gui class doesn't natively support actions involving your own inventory.
	// This adds support so that you can click on your own inventory to add items to the filter.
	@EventHandler
	public void onInventoryClick(@Nonnull InventoryClickEvent event) {
		Inventory clicked = event.getClickedInventory();
		if (clicked == null || clicked.getType() != InventoryType.PLAYER) {
			return;
		}

		Player player = (Player) event.getWhoClicked();
		Gui gui = Gui.players.get(player);
		if (!(gui instanceof FilterMenu)) {
			return;
		}

		event.setCancelled(true);

		if (gui.isValid()) {
			gui.getSlot(event.getRawSlot()).handle(event);
		} else {
			gui.close();
		}
	}

	/**
	 * Loads the drop filter from the player's NBT data.
	 *
	 * @param player the player
	 */
	private void loadFilter(@Nonnull Player player) {
		if (filter.containsKey(player.getUniqueId())) {
			return;
		}

		String materialStr = NBT.getTag(player, FILTER_MATERIALS_KEY).orElse(null);
		if (materialStr == null || materialStr.isEmpty()) {
			filter.put(player.getUniqueId(), new ArrayList<>());
			filterSet.put(player.getUniqueId(), new HashSet<>());
			return;
		}

		List<Material> materials = Arrays.stream(materialStr.split(";")).map(Material::valueOf).collect(Collectors.toList());
		filter.put(player.getUniqueId(), materials);
		filterSet.put(player.getUniqueId(), new HashSet<>(materials));
	}

	/**
	 * Saves the drop filter to the player's NBT data.
	 *
	 * @param player the player
	 */
	private void saveFilter(@Nonnull Player player) {
		List<String> materialNames = filter.get(player.getUniqueId()).stream().map(Enum::name).toList();
		NBT.setTag(player, FILTER_MATERIALS_KEY, String.join(";", materialNames));
	}
}
