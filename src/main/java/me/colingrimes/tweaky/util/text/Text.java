package me.colingrimes.tweaky.util.text;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Provides various string operations.
 */
public final class Text {

	private static final Pattern SPECIAL_CHARACTERS = Pattern.compile("\\s+|:+|-+|_+");
	private static final DecimalFormat INT_FORMAT = new DecimalFormat("#,###");
	private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("#,###.##");

	/**
	 * Turns the string into a color-coded string.
	 *
	 * @param str the string to be colored
	 * @return color-coded string
	 */
	@Nonnull
	public static String color(@Nullable String str) {
		if (str == null || str.isEmpty()) return "";
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	/**
	 * Turns the list of strings into a color-coded list of strings.
	 *
	 * @param strList the list of strings
	 * @return color-coded list of messages
	 */
	@Nonnull
	public static List<String> color(@Nullable List<String> strList) {
		if (strList == null || strList.isEmpty()) return new ArrayList<>();
		return strList.stream().map(Text::color).collect(Collectors.toList());
	}

	/**
	 * Formats a String object.
	 *
	 * <p>In doing so, the following actions are performed:</p>
	 * <ul>
	 *   <li>Replaces all colons, dashes, and underscores with spaces.</li>
	 *   <li>Capitalizes the first letter of each word.</li>
	 * </ul>
	 *
	 * @param str the string to be formatted
	 * @return formatted string
	 */
	@Nonnull
	public static String format(@Nullable String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}

		// Replaces all colons, dashes, and underscores with spaces.
		str = SPECIAL_CHARACTERS.matcher(str).replaceAll(" ");

		StringBuilder builder = new StringBuilder(str.substring(0, 1).toUpperCase());
		for (int i=1; i<str.length(); i++) {
			if (i < str.length() - 1 && str.charAt(i) == ' ') {
				builder.append(" ").append(str.substring(++i, i+1).toUpperCase());
			} else {
				builder.append(str.substring(i, i+1).toLowerCase());
			}
		}

		return builder.toString();
	}

	/**
	 * Formats the specified Number with thousands separators.
	 * If a decimal, shows up to two decimal places only if present.
	 *
	 * <p>Examples:</p>
	 * <ul>
	 *   <li> 100000 -> "100,000" </li>
	 * 	 <li> 1200000.0 -> "1,200,000" </li>
	 * 	 <li> 123456.789 -> "123,456.79" </li>
	 * </ul>
	 *
	 * @param number the number to format
	 * @return formatted number
	 */
	@Nonnull
	public static String format(@Nullable Number number) {
		if (number instanceof Integer || number instanceof Long) {
			return INT_FORMAT.format(number);
		} else if (number instanceof Double || number instanceof Float) {
			return DOUBLE_FORMAT.format(number);
		} else {
			return "";
		}
	}

	/**
	 * Formats the specified Duration into a "m:ss" time format.
	 *
	 * @param duration the duration to format
	 * @return formatted time
	 */
	@Nonnull
	public static String format(@Nullable Duration duration) {
		if (duration == null) {
			return "";
		}

		long minutes = duration.toMinutes();
		long seconds = duration.minusMinutes(minutes).getSeconds();
		return String.format("%d:%02d", minutes, seconds);
	}

	/**
	 * Formats the duration using minutes and seconds, showing fractional minutes if needed.
	 *
	 * <p>Examples:</p>
	 * <ul>
	 *   <li>Duration.ofMinutes(5) -> "5 minutes"</li>
	 *   <li>Duration.ofSeconds(30) -> "30 seconds"</li>
	 *   <li>Duration.ofSeconds(330) -> "5.5 minutes"</li>
	 * </ul>
	 *
	 * @param duration the duration to format
	 * @return formatted time
	 */
	@Nonnull
	public static String formatApprox(@Nullable Duration duration) {
		if (duration == null || duration.isZero()) {
			return "0 seconds";
		}

		long seconds = duration.getSeconds();
		if (seconds < 60) {
			return seconds + " second" + (seconds == 1 ? "" : "s");
		}

		double minutes = seconds / 60.0;
		if (minutes % 1 == 0) {
			long wholeMinutes = (long) minutes;
			return wholeMinutes + " minute" + (wholeMinutes == 1 ? "" : "s");
		}

		return String.format("%.1f minutes", minutes);
	}

	/**
	 * Unformats a String object.
	 * In doing so, all capital letters become lowercase with an underscore behind them.
	 *
	 * @param str the string to be unformatted
	 * @return unformatted string
	 */
	@Nonnull
	public static String unformat(@Nullable String str) {
		if (str == null || str.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder(str.substring(0, 1).toLowerCase());
		for (char c : str.substring(1).toCharArray()) {
			if (c == ' ') continue;
			if (Character.isUpperCase(c)) {
				builder.append("_").append(Character.toLowerCase(c));
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	/**
	 * Strips the specified message.
	 *
	 * @param message the message
	 * @return new message with spaces, dashes, and colons removed
	 */
	@Nonnull
	public static String strip(@Nullable String message) {
		return message == null ? "" : message.replace("\\s+", "").replace("-", "").replace(":", "");
	}

	/**
	 * Use {@link Text#format(Duration)} instead.
	 */
	@Deprecated
	@Nonnull
	public static String formatTime(@Nullable Duration duration) {
		return format(duration);
	}

	private Text() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}