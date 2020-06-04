package doggytalents.common.util;

import java.util.Optional;
import java.util.function.Supplier;

public class Cache<T> implements Supplier<T> {

    private Supplier<T> getter;
    private Optional<T> value;

    private Cache(Supplier<T> getterIn) {
        this.getter = getterIn;
    }

    @Override
    public T get() {
        if (this.value == null) {
            this.value = Optional.ofNullable(this.getter.get());
        }

        return this.value.orElse(null);
    }

    public static <T> Cache<T> make(Supplier<T> getterIn) {
        return new Cache<>(getterIn);
    }

}
