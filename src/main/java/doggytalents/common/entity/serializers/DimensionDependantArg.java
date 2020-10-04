package doggytalents.common.entity.serializers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class DimensionDependantArg<T> implements Map<RegistryKey<World>, T> {

    private Supplier<IDataSerializer<T>> serializer;

    public Map<RegistryKey<World>, T> map = new HashMap<>();

    public DimensionDependantArg(Supplier<IDataSerializer<T>> serializer) {
        this.serializer = serializer;
    }

    @Override
    public T put(RegistryKey<World> dim, T obj) {
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

    public IDataSerializer<T> getSerializer() {
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

    public DimensionDependantArg<T> set(RegistryKey<World> dim, T value) {
        this.put(dim, value);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(map.size());
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
            DimensionDependantArg other = (DimensionDependantArg) obj;
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
    public void putAll(Map<? extends RegistryKey<World>, ? extends T> m) {
        this.map.putAll(m);
    }

    @Override
    public Set<RegistryKey<World>> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<T> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<RegistryKey<World>, T>> entrySet() {
        return Collections.unmodifiableSet(this.map.entrySet());
    }
}
