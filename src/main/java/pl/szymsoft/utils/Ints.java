package pl.szymsoft.utils;

import java.util.function.IntPredicate;
import java.util.function.Supplier;

public enum Ints {
    ;

    public static int require(int value, IntPredicate predicate, String message) {
        return require(value, predicate, () -> message);
    }

    public static int require(int value, IntPredicate predicate, Supplier<String> messageSupplier) {

        if (predicate.test(value)) {
            return value;
        }

        throw new IllegalArgumentException(messageSupplier.get() + ", but was " + value);
    }

    public static int requirePositiveOrZero(int number, String variableName) {
        return require(number, value -> value >= 0, () -> variableName + " has to be positive or zero");
    }
}
