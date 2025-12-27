package me.colingrimes.tweaky.util;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public final class Types {

    /**
     * Gets whether the specified string could be parsed into an integer.
     *
     * @param str the string to check
     * @return true if the string is an integer
     */
    public static boolean isInteger(@Nonnull String str) {
        return str.matches("\\d+");
    }

    /**
     * Gets whether the specified string could be parsed into a double.
     *
     * @param str the string to check
     * @return true if the string is a double
     */
    public static boolean isDouble(@Nonnull String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }

    /**
     * Gets whether the specificed object could be parsed into a string list.
     *
     * @param candidateList the object to check
     * @return true if the object is a string list
     */
    public static boolean isStringList(@Nonnull Object candidateList) {
        return candidateList instanceof List<?> list && !list.isEmpty() && list.stream().allMatch(item -> item instanceof String);
    }

    /**
     * Gets the object as a string list if the conversion is possible.
     *
     * @param candidateList the object to check
     * @return an optional containing a list of strings if available
     */
    @Nonnull
    public static Optional<List<String>> asStringList(@Nonnull Object candidateList) {
        if (isStringList(candidateList)) {
            return Optional.of(((List<?>) candidateList).stream().map(String::valueOf).toList());
        } else {
            return Optional.empty();
        }
    }

    private Types() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}