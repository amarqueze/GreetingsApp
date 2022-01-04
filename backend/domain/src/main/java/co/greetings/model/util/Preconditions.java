package co.greetings.model.util;

import java.util.function.Predicate;

import lombok.NonNull;

public class Preconditions {
    private Preconditions() {}

    public static <T> void checkArgument(Predicate<T> expression, T obj, @NonNull String message) {
        if (expression.test(obj)) throw new IllegalArgumentException(message);
    }

    public static <T> void checkNull(T obj, @NonNull String message) {
        if (obj == null) throw new NullPointerException(message);
    }
}
