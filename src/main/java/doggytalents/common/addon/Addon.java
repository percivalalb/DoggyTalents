package doggytalents.common.addon;

import java.util.Collection;
import java.util.Collections;

import net.minecraftforge.fml.ModList;

public interface Addon {

    /**
     * Called from the DT2 mod class constructor
     *
     * @throws RuntimeException You should re-throw any exceptions as {@link RuntimeException}, this are
     */
    default void init() throws RuntimeException {

    }

    /**
     * Called from the DT2 {@link InterModProcessEvent}
     *
     * @throws RuntimeException You should re-throw any exceptions as {@link RuntimeException}, this are
     */
    default void exec() throws RuntimeException {

    }

    /**
     * Empty collections means run regardless of the mods loaded, otherwise
     * only run when all the given mods are loaded.
     */
    default Collection<String> getMods() {
        return Collections.emptySet();
    }

    default boolean shouldLoad() {
        Collection<String> modIds = this.getMods();
        return modIds.isEmpty() || modIds.stream().allMatch(ModList.get()::isLoaded);
    }

    public String getName();
}
