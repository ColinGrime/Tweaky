package me.colingrimes.tweaky.util.bukkit;

import me.colingrimes.tweaky.Tweaky;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Responsible for NBT integration.
 */
public final class NBT {

	private static final Map<Class<?>, PersistentDataType<?, ?>> TYPE_MAP = new HashMap<>();

	static {
		TYPE_MAP.put(byte.class, PersistentDataType.BYTE);
		TYPE_MAP.put(Byte.class, PersistentDataType.BYTE);
		TYPE_MAP.put(short.class, PersistentDataType.SHORT);
		TYPE_MAP.put(Short.class, PersistentDataType.SHORT);
		TYPE_MAP.put(int.class, PersistentDataType.INTEGER);
		TYPE_MAP.put(Integer.class, PersistentDataType.INTEGER);
		TYPE_MAP.put(long.class, PersistentDataType.LONG);
		TYPE_MAP.put(Long.class, PersistentDataType.LONG);
		TYPE_MAP.put(float.class, PersistentDataType.FLOAT);
		TYPE_MAP.put(Float.class, PersistentDataType.FLOAT);
		TYPE_MAP.put(double.class, PersistentDataType.DOUBLE);
		TYPE_MAP.put(Double.class, PersistentDataType.DOUBLE);
		TYPE_MAP.put(boolean.class, PersistentDataType.BOOLEAN);
		TYPE_MAP.put(Boolean.class, PersistentDataType.BOOLEAN);
		TYPE_MAP.put(String.class, PersistentDataType.STRING);
	}

	/**
	 * Checks whether the holder has the given NBT tag.
	 *
	 * @param holder the holder to check the tag from
	 * @param key the key to check the tag from
	 * @param clazz the data type of the key
	 * @return true if the holder contains the key of the specified type
	 */
	public static boolean hasTag(@Nullable PersistentDataHolder holder, @Nullable String key, @Nonnull Class<?> clazz) {
		if (holder == null || key == null || key.isEmpty()) {
			return false;
		}

		NamespacedKey namespacedKey = new NamespacedKey(Tweaky.getInstance(), key);
		return holder.getPersistentDataContainer().has(namespacedKey, getDataType(clazz));
	}

	/**
	 * Gets the NBT tag corresponding to a key.
	 *
	 * @param holder the holder to get the tag from
	 * @param key the key to get the value from
	 * @return the holder's tag corresponding to the key
	 */
	@Nonnull
	public static Optional<String> getTag(@Nullable PersistentDataHolder holder, @Nullable String key) {
		return getTag(holder, key, String.class);
	}

	/**
	 * Gets the NBT tag corresponding to a key.
	 *
	 * @param holder the holder to get the tag from
	 * @param key the key to get the value from
	 * @param clazz the data type class to convert to
	 * @return the holder's tag corresponding to the key
	 */
	@Nonnull
	public static <T> Optional<T> getTag(@Nullable PersistentDataHolder holder, @Nullable String key, @Nonnull Class<T> clazz) {
		if (holder == null || key == null || key.isEmpty()) {
			return Optional.empty();
		}

		NamespacedKey namespacedKey = new NamespacedKey(Tweaky.getInstance(), key);
		return Optional.ofNullable(holder.getPersistentDataContainer().get(namespacedKey, getDataType(clazz)));
	}

	/**
	 * Sets the NBT tag (key-value pair).
	 *
	 * @param holder the holder to set the tag on
	 * @param key the key of the tag
	 * @param value the value corresponding to the key
	 */
	public static <T> void setTag(@Nonnull PersistentDataHolder holder, @Nonnull String key, @Nonnull T value) {
		Objects.requireNonNull(holder, "Holder is null.");
		Objects.requireNonNull(key, "Key is null.");
		Objects.requireNonNull(value, "Value is null.");

		NamespacedKey namespacedKey = new NamespacedKey(Tweaky.getInstance(), key);
		holder.getPersistentDataContainer().set(namespacedKey, getDataType(value), value);
	}

	/**
	 * Removes the NBT tag (key-value pair).
	 *
	 * @param holder the holder to remove the tag from
	 * @param key the key of the tag
	 */
	public static <T> void removeTag(@Nonnull PersistentDataHolder holder, @Nonnull String key) {
		Objects.requireNonNull(holder, "Holder is null.");
		Objects.requireNonNull(key, "Key is null.");

		NamespacedKey namespacedKey = new NamespacedKey(Tweaky.getInstance(), key);
		holder.getPersistentDataContainer().remove(namespacedKey);
	}

	/**
	 * Gets the {@link PersistentDataType} from the object.
	 *
	 * @param object the object to get the data type from
	 * @return the persistent data type
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	private static <T> PersistentDataType<?, T> getDataType(@Nonnull T object) {
		return getDataType((Class<T>) object.getClass());
	}

	/**
	 * Gets the {@link PersistentDataType} from the class.
	 *
	 * @param clazz the class to get the data type from
	 * @return the persistent data type
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	private static <T> PersistentDataType<?, T> getDataType(@Nonnull Class<T> clazz) {
		PersistentDataType<?, ?> type = TYPE_MAP.get(clazz);
		if (type == null) {
			throw new IllegalArgumentException("Unsupported data type: " + clazz.getSimpleName());
		} else {
			return (PersistentDataType<?, T>) type;
		}
	}

	private NBT() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}