package me.colingrimes.tweaky.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Random {

	private final static java.util.Random random = new java.util.Random();

	/**
	 * Gets a random number from 0 to a number (exclusive).
	 *
	 * @param num any integer
	 * @return random number
	 */
	public static int number(int num) {
		return random.nextInt(num);
	}

	/**
	 * Gets a random number between two integers.
	 *
	 * @param lowNum  low integer
	 * @param highNum high integer
	 * @return random number
	 */
	public static int number(int lowNum, int highNum) {
		return random.nextInt((highNum - lowNum) + 1) + lowNum;
	}

	/**
	 * Gets a random number between two doubles.
	 *
	 * @param lowNum  low double
	 * @param highNum high double
	 * @return random number
	 */
	public static double decimal(double lowNum, double highNum) {
		return (random.nextDouble() * (highNum - lowNum)) + lowNum;
	}

	/**
	 * Returns true with the given percentage chance.
	 *
	 * @param percent any double
	 * @return true with the percent chance
	 */
	public static boolean chance(double percent) {
		percent = Math.max(0, Math.min(100, percent));
		return random.nextDouble() < percent / 100.0;
	}

	/**
	 * Gets a random item from the provided list.
	 *
	 * @param list the list
	 * @return a random item
	 */
	@Nonnull
	public static <T> T item(@Nonnull List<T> list) {
		return list.get(Random.number(list.size()));
	}

	/**
	 * Gets a random item from the provided array.
	 *
	 * @param array the array
	 * @return a random item
	 */
	@Nonnull
	public static <T> T item(@Nonnull T[] array) {
		return array[Random.number(array.length)];
	}

	/**
	 * Gets a list of random items from the provided list (repeated elements are not allowed).
	 *
	 * @param list the list
	 * @param count the number of random items
	 * @return the random items
	 */
	@Nonnull
	public static <T> List<T> items(@Nonnull List<T> list, int count) {
		return items(list, count, false);
	}

	/**
	 * Gets a list of random items from the provided list.
	 *
	 * @param list the list
	 * @param count the number of random items
	 * @param repeat whether repeating elements are allowed
	 * @return the random items
	 */
	@Nonnull
	public static <T> List<T> items(@Nonnull List<T> list, int count, boolean repeat) {
		if (!repeat) {
			List<T> copy = new ArrayList<>(list);
			Collections.shuffle(copy);
			return copy.subList(0, count);
		}

		List<T> random = new ArrayList<>();
		for (int i=0; i<count; i++) {
			random.add(item(list));
		}
		return random;
	}

	/**
	 * Gets an array of random items from the provided array (repeated elements are not allowed).
	 *
	 * @param array the array
	 * @param count the number of random items
	 * @return the random items
	 */
	@Nonnull
	public static <T> T[] items(@Nonnull T[] array, int count) {
		return items(array, count, false);
	}

	/**
	 * Gets an array of random items from the provided array.
	 *
	 * @param array the array
	 * @param count the number of random items
	 * @param repeat whether repeating elements are allowed
	 * @return the random items
	 */
	@Nonnull
	public static <T> T[] items(@Nonnull T[] array, int count, boolean repeat) {
		return items(Arrays.asList(array), count, repeat).toArray(Arrays.copyOf(array, count));
	}

	private Random() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}