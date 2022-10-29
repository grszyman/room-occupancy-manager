package pl.szymsoft.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public enum Objects {
    ;

    public static <T> T require(T value, Predicate<T> predicate, String message) {
        return require(value, predicate, () -> message);
    }

    public static <T> T require(T value, Predicate<T> predicate, Supplier<String> messageSupplier) {

        if (predicate.test(requireNonNull(value, messageSupplier))) {
            return value;
        }

        throw new IllegalArgumentException(messageSupplier.get() + ", but was " + value);
    }
}
