package doggytalents.api.feature;

import java.util.concurrent.atomic.AtomicInteger;

public class DataKey<T> {

    private static final AtomicInteger BASE = new AtomicInteger(0);

    private final int id;
    private final boolean isFinal;

    public DataKey(boolean isFinal) {
        this.id = BASE.getAndIncrement();
        this.isFinal = isFinal;
    }

    public int getIndex() {
        return this.id;
    }

    public boolean isFinal() {
        return this.isFinal;
    }

    public static <T> DataKey<T> make() {
        return new DataKey<>(false);
    }

    public static <T> DataKey<T> makeFinal() {
        return new DataKey<>(true);
    }

}
