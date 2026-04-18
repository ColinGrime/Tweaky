package me.colingrimes.tweaky.util.bukkit;

import me.colingrimes.tweaky.event.PlayerInteractBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Events {

	/**
	 * Creates a new {@link Guard} object.
	 *
	 * @return the event guard
	 */
	@Nonnull
	public static Guard guard() {
		return new Guard();
	}

	/**
	 * Checks if the player can build at the block (place or break).
	 *
	 * @param player the player
	 * @param block the block
	 * @return true if the block can be modified
	 */
	public static boolean canBuild(@Nonnull Player player, @Nonnull Block block) {
		return canPlace(player, block, block.getRelative(BlockFace.DOWN)) || canBreak(player, block);
	}

	/**
	 * Checks if the player can place the block.
	 *
	 * @param player the player
	 * @param block the block to place
	 * @param blockAgainst the block that it is placed against
	 * @return true if the block can be placed
	 */
	public static boolean canPlace(@Nonnull Player player, @Nonnull Block block, @Nonnull Block blockAgainst) {
		BlockPlaceEvent event = new BlockPlaceEvent(
				block,
				block.getState(),
				blockAgainst,
				player.getInventory().getItemInMainHand(),
				player,
				true,
				EquipmentSlot.HAND
		);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Checks if the player can break the block.
	 *
	 * @param player the player
	 * @param block the block to break
	 * @return true if the block can be broken
	 */
	public static boolean canBreak(@Nonnull Player player, @Nonnull Block block) {
		BlockBreakEvent event = new BlockBreakEvent(block, player);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Checks if the block can be interacted with.
	 *
	 * @param player the player
	 * @param block the block to interact with
	 * @return true if the block can be interacted with
	 */
	public static boolean canInteract(@Nonnull Player player, @Nonnull Block block) {
		ItemStack item = player.getInventory().getItemInMainHand();
		PlayerInteractEvent event = new PlayerInteractEvent(player, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, item, block, BlockFace.UP);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Checks if the entity can be interacted with.
	 *
	 * @param player the player
	 * @param entity the entity to interact with
	 * @return true if the entity can be interacted with
	 */
	public static boolean canInteractEntity(@Nonnull Player player, @Nonnull Entity entity) {
		PlayerInteractEntityEvent event = new PlayerInteractEntityEvent(player, entity);
		Bukkit.getPluginManager().callEvent(event);
		return !event.isCancelled();
	}

	/**
	 * Attempts to get the player from the event.
	 *
	 * @param event the event
	 * @return the player if available
	 */
	@Nullable
	public static Player getPlayer(@Nonnull Event event) {
		return switch (event) {
			case PlayerEvent e -> e.getPlayer();
			case PlayerInteractBlockEvent e -> e.getPlayer();
			case BlockBreakEvent e -> e.getPlayer();
			case BlockPlaceEvent e -> e.getPlayer();
			case BlockDamageEvent e -> e.getPlayer();
			case BlockDropItemEvent e -> e.getPlayer();
			case InventoryEvent e -> e.getView().getPlayer() instanceof Player p ? p : null;
			case ProjectileLaunchEvent e -> e.getEntity().getShooter() instanceof Player p ? p : null;
			case ProjectileHitEvent e -> e.getEntity().getShooter() instanceof Player p ? p : null;
			case EntityBreedEvent e -> e.getBreeder() instanceof Player p ? p : null;
			case EntityEvent e -> e.getEntity() instanceof Player p ? p : null;
			default -> null;
		};
	}

	/**
	 * Attempts to get the entity from teh event.
	 *
	 * @param event the event
	 * @return the entity if available
	 */
	@Nullable
	public static Entity getEntity(@Nonnull Event event) {
		return switch (event) {
			case PlayerInteractEntityEvent e -> e.getRightClicked();
			case EntityEvent e -> e.getEntity();
			default -> null;
		};
	}

	/**
	 * Attempts to get the block from the event.
	 *
	 * @param event the event
	 * @return the block if available
	 */
	@Nullable
	public static Block getBlock(@Nonnull Event event) {
		return switch (event) {
			case BlockEvent e -> e.getBlock();
			case PlayerInteractEvent e -> e.getClickedBlock();
			case PlayerInteractBlockEvent e -> e.getBlock();
			case EntityInteractEvent e -> e.getBlock();
			default -> null;
		};
	}

	/**
	 * Attempts to get the player hand from the event.
	 *
	 * @param event the event
	 * @return the player hand if available
	 */
	@Nullable
	public static EquipmentSlot getHand(@Nonnull Event event) {
		return switch (event) {
			case PlayerInteractEvent e -> e.getHand();
			case PlayerInteractBlockEvent e -> e.getHand();
			case PlayerInteractEntityEvent e -> e.getHand();
			default -> null;
		};
	}

	/**
	 * Checks if the event is a left-click event.
	 *
	 * @param event the event
	 * @return true if the event is a left-click event
	 */
	public static boolean isLeftClick(@Nonnull Event event) {
		return switch (event) {
			case PlayerInteractEvent e -> e.getAction().isLeftClick();
			case PlayerInteractBlockEvent e -> e.isLeftClick();
			case InventoryClickEvent e -> e.isLeftClick();
			default -> false;
		};
	}

	/**
	 * Checks if the event is a right-click event.
	 *
	 * @param event the event
	 * @return true if the event is a right-click event
	 */
	public static boolean isRightClick(@Nonnull Event event) {
		return switch (event) {
			case PlayerInteractEvent e -> e.getAction().isRightClick();
			case PlayerInteractBlockEvent e -> e.isRightClick();
			case InventoryClickEvent e -> e.isRightClick();
			default -> false;
		};
	}

	public static class Guard {

		// Conditions
		private final Conditions<Player, EntityType> playerConditions = new Conditions<>(Player::getType);
		private final Conditions<ItemStack, Material> itemConditions = new Conditions<>(ItemStack::getType);
		private final Conditions<Block, Material> blockConditions = new Conditions<>(Block::getType);
		private final Conditions<Entity, EntityType> entityConditions = new Conditions<>(Entity::getType);

		// General
		private boolean standing = false;
		private boolean sneaking = false;
		private boolean mainHand = false;
		private boolean leftClick = false;
		private boolean rightClick = false;

		// Protection
		private boolean buildable = false;
		private boolean breakable = false;
		private boolean interactable = false;
		private boolean interactableEntity = false;

		/**
		 * Checks that the relevant location is buildable.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard buildable() {
			this.buildable = true;
			return this;
		}

		/**
		 * Checks that the relevant block is breakable.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard breakable() {
			this.breakable = true;
			return this;
		}

		/**
		 * Checks that the relevant block is interactable.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard interactable() {
			this.interactable = true;
			return this;
		}

		/**
		 * Checks that the relevant entity is interactable.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard interactableEntity() {
			this.interactableEntity = true;
			return this;
		}


		/**
		 * Checks that the player is standing.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard standing() {
			this.standing = true;
			return this;
		}

		/**
		 * Checks that the player is sneaking.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard sneaking() {
			this.sneaking = true;
			return this;
		}

		/**
		 * Checks that the player is using their main hand.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard mainHand() {
			this.mainHand = true;
			return this;
		}

		/**
		 * Checks that the player is left-clicking.
		 * <p>
		 * This could be left-clicking a block, entity, or inventory slot.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard leftClick() {
			this.leftClick = true;
			return this;
		}

		/**
		 * Checks that the player is right-clicking.
		 * <p>
		 * This could be right-clicking a block, entity, or inventory slot.
		 *
		 * @return the event guard
		 */
		@Nonnull
		public Guard rightClick() {
			this.rightClick = true;
			return this;
		}

		/**
		 * Checks if the player passes the predicate.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard player(@Nonnull Predicate<Player> predicate) {
			this.playerConditions.all(predicate);
			return this;
		}

		/**
		 * Checks if the player's held item matches the given material.
		 *
		 * @param material the material to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard item(@Nonnull Material material) {
			this.itemConditions.all(material);
			return this;
		}

		/**
		 * Checks if the player's held item matches the given tag.
		 *
		 * @param tag the tag to check
		 * @return the guard object
		 */
		@Nonnull
		public final Guard item(@Nonnull Tag<Material> tag) {
			this.itemConditions.all(tag);
			return this;
		}

		/**
		 * Checks if the player's held item passes the predicate.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard item(@Nonnull Predicate<ItemStack> predicate) {
			this.itemConditions.all(predicate);
			return this;
		}

		/**
		 * Checks if the player's held item matches one of the given materials.
		 *
		 * @param materials the materials to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard anyItem(@Nonnull Material ...materials) {
			this.itemConditions.any(materials);
			return this;
		}

		/**
		 * Checks if the player's held item matches one of the given tags.
		 *
		 * @param tags the tags to check
		 * @return the guard object
		 */
		@SafeVarargs
		@Nonnull
		public final Guard anyItem(@Nonnull Tag<Material> ...tags) {
			this.itemConditions.any(tags);
			return this;
		}

		/**
		 * Checks if the player's held item passes one the predicates.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard anyItem(@Nonnull Predicate<ItemStack> predicate) {
			this.itemConditions.any(predicate);
			return this;
		}

		/**
		 * Checks if the block matches the given material.
		 *
		 * @param material the material to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard block(@Nonnull Material material) {
			this.blockConditions.all(material);
			return this;
		}

		/**
		 * Checks if the block matches the given tag.
		 *
		 * @param tag the tag to check
		 * @return the guard object
		 */
		@Nonnull
		public final Guard block(@Nonnull Tag<Material> tag) {
			this.blockConditions.all(tag);
			return this;
		}

		/**
		 * Checks if the block passes the predicate.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard block(@Nonnull Predicate<Block> predicate) {
			this.blockConditions.all(predicate);
			return this;
		}

		/**
		 * Checks if the block matches one of the given materials.
		 *
		 * @param materials the materials to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard anyBlock(@Nonnull Material ...materials) {
			this.blockConditions.any(materials);
			return this;
		}

		/**
		 * Checks if the block matches one of the given tags.
		 *
		 * @param tags the tags to check
		 * @return the guard object
		 */
		@SafeVarargs
		@Nonnull
		public final Guard anyBlock(@Nonnull Tag<Material> ...tags) {
			this.blockConditions.any(tags);
			return this;
		}

		/**
		 * Checks if the block passes one of the predicates.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard anyBlock(@Nonnull Predicate<Block> predicate) {
			this.blockConditions.any(predicate);
			return this;
		}

		/**
		 * Checks if the entity matches the given type.
		 *
		 * @param type the type to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard entity(@Nonnull EntityType type) {
			this.entityConditions.all(type);
			return this;
		}

		/**
		 * Checks if the entity matches the given tag.
		 *
		 * @param tag the tag to check
		 * @return the guard object
		 */
		@Nonnull
		public final Guard entity(@Nonnull Tag<EntityType> tag) {
			this.entityConditions.all(tag);
			return this;
		}

		/**
		 * Checks if the entity passes the predicate.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard entity(@Nonnull Predicate<Entity> predicate) {
			this.entityConditions.all(predicate);
			return this;
		}

		/**
		 * Checks if the entity matches one of the given types.
		 *
		 * @param types the types to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard anyEntity(@Nonnull EntityType ...types) {
			this.entityConditions.any(types);
			return this;
		}

		/**
		 * Checks if the entity matches one of the given tags.
		 *
		 * @param tags the tags to check
		 * @return the guard object
		 */
		@SafeVarargs
		@Nonnull
		public final Guard anyEntity(@Nonnull Tag<EntityType> ...tags) {
			this.entityConditions.any(tags);
			return this;
		}

		/**
		 * Checks if the entity passes one of the predicates.
		 *
		 * @param predicate the predicate to check
		 * @return the guard object
		 */
		@Nonnull
		public Guard anyEntity(@Nonnull Predicate<Entity> predicate) {
			this.entityConditions.any(predicate);
			return this;
		}

		/**
		 * Tests all the filters to see if the event passes.
		 *
		 * @param event the event
		 * @return true if the event passes all the filters
		 */
		public boolean test(@Nonnull Event event) {
			return blockConditions.test(getBlock(event)) && entityConditions.test(getEntity(event)) && testClick(event) && testPlayer(event);
		}

		/**
		 * Tests all the filters to see if the event fails.
		 *
		 * @param event the event
		 * @return true if the event fails any of the filters
		 */
		public boolean reject(@Nonnull Event event) {
			return !test(event);
		}

		/**
		 * Tests all the player-related filters.
		 *
		 * @param event the event
		 * @return true if all the player filters pass
		 */
		private boolean testPlayer(@Nonnull Event event) {
			Player player = getPlayer(event);
			if (player == null) {
				return true;
			}

			EquipmentSlot hand = getHand(event);
			if (hand == null) {
				hand = EquipmentSlot.HAND;
			}

			if (!playerConditions.test(player) || !Players.shouldHandleHand(player, hand, itemConditions::test)) {
				return false;
			}

			if ((standing && player.isSneaking()) || (sneaking && !player.isSneaking()) || (mainHand && hand != EquipmentSlot.HAND)) {
				return false;
			}

			Entity entity = getEntity(event);
			if (interactableEntity && (entity == null || !Events.canInteractEntity(player, entity))) {
				return false;
			}

			Block block = getBlock(event);
			if (block == null) {
				return !buildable && !breakable && !interactable;
			}

			boolean buildPass = !buildable || Events.canBuild(player, block);
			boolean breakPass = !breakable || Events.canBreak(player, block);
			boolean interactablePass = !interactable || Events.canInteract(player, block);
			return buildPass && breakPass && interactablePass;
		}

		/**
		 * Tests all the click-type-related filters.
		 *
		 * @param event the event
		 * @return true if all the click filters pass
		 */
		private boolean testClick(@Nonnull Event event) {
			return (!rightClick || isRightClick(event)) && (!leftClick || isLeftClick(event));
		}
	}

	private static class Conditions<T, K extends Keyed> {

		private final Function<T, K> typeConverter;
		private final Set<Predicate<T>> allPredicates;
		private final Set<Predicate<T>> anyPredicates;

		private Conditions(@Nonnull Function<T, K> typeConverter) {
			this.typeConverter = typeConverter;
			this.allPredicates = new HashSet<>();
			this.anyPredicates = new HashSet<>();
		}

		/**
		 * Checks if the object's type is equal to the given type.
		 *
		 * @param type the type to check
		 */
		private void all(@Nonnull K type) {
			this.allPredicates.add(o -> typeConverter.apply(o).equals(type));
		}

		/**
		 * Checks if the object's type passes the given tag.
		 *
		 * @param tag the tag to check
		 */
		private void all(@Nonnull Tag<K> tag) {
			this.allPredicates.add(o -> tag.isTagged(typeConverter.apply(o)));
		}

		/**
		 * Checks if the object passes the given predicate.
		 *
		 * @param predicate the required predicate to check
		 */
		private void all(@Nonnull Predicate<T> predicate) {
			this.allPredicates.add(predicate);
		}

		/**
		 * Checks if the object's type is equal to any of the types.
		 * <p>
		 * Only a single predicate must be true to pass.
		 *
		 * @param types the types to check
		 */
		@SafeVarargs
		private void any(@Nonnull K ...types) {
			for (K type : types) {
				anyPredicates.add(o -> typeConverter.apply(o).equals(type));
			}
		}

		/**
		 * Checks if the object's type passes any of the tags.
		 * <p>
		 * Only a single predicate must be true to pass.
		 *
		 * @param tags the tags to check
		 */
		@SafeVarargs
		private void any(@Nonnull Tag<K> ...tags) {
			for (Tag<K> tag : tags) {
				anyPredicates.add(o -> tag.isTagged(typeConverter.apply(o)));
			}
		}

		/**
		 * Adds a predicate to the {@code anyPredicates} set.
		 * <p>
		 * Only a single predicate must be true to pass.
		 *
		 * @param predicate the predicate to check
		 */
		private void any(@Nonnull Predicate<T> predicate) {
			this.anyPredicates.add(predicate);
		}

		/**
		 * Checks if the conditions all pass for the object.
		 *
		 * @return true if the conditions are all met
		 */
		private boolean test(@Nullable T object) {
			if (object == null) {
				return allPredicates.isEmpty() && anyPredicates.isEmpty();
			}

			if (!allPredicates.stream().allMatch(p -> p.test(object))) {
				return false;
			}

			return anyPredicates.isEmpty() || anyPredicates.stream().anyMatch(p -> p.test(object));
		}
	}
}
