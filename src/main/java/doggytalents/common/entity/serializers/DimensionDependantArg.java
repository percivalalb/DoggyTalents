package doggytalents.common.entity.serializers;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Supplier;

public class DimensionDependantArg<T> implements Map<ResourceKey<Level>, T> {

    private Supplier<EntityDataSerializer<T>> serializer;

    public Map<ResourceKey<Level>, T> map = new HashMap<>();

    public DimensionDependantArg(Supplier<EntityDataSerializer<T>> serializer) {
        this.serializer = serializer;
    }

    @Override
    public T put(ResourceKey<Level> dim, T obj) {
        return this.map.put(dim, obj);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    public EntityDataSerializer<T> getSerializer() {
        return this.serializer.get();
    }

    public DimensionDependantArg<T> copy() {
        DimensionDependantArg<T> clone = new DimensionDependantArg<>(this.serializer);
        clone.map.putAll(this.map);
        return clone;
    }

    public DimensionDependantArg<T> copyEmpty() {
        return new DimensionDependantArg<>(this.serializer);
    }

    public DimensionDependantArg<T> set(ResourceKey<Level> dim, T value) {
        this.put(dim, value);
        return this;
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public String toString() {
        return Objects.toString(this.map);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        } else if (!(obj instanceof DimensionDependantArg)) {
            return false;
        } else {
            DimensionDependantArg<?> other = (DimensionDependantArg<?>) obj;
            return this.map.equals(other.map);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public T get(Object key) {
        return this.map.get(key);
    }

    @Override
    public T remove(Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends ResourceKey<Level>, ? extends T> m) {
        this.map.putAll(m);
    }

    @Override
    public Set<ResourceKey<Level>> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<T> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<ResourceKey<Level>, T>> entrySet() {
        return Collections.unmodifiableSet(this.map.entrySet());
    }
}
