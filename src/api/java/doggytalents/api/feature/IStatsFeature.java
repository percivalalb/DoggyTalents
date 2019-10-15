package doggytalents.api.feature;

import net.minecraftforge.fml.common.registry.EntityEntry;

public interface IStatsFeature {

    /**
     * Gets the number of times the dog has killed the given
     * entity type.
     * @param type The {@link net.minecraftforge.fml.common.registry.EntityEntry}
     * @return The kill count for the entity type
     */
    public int getKillCountFor(EntityEntry type);

    /**
     * Gets the total number of entities killed
     * @return The number of entities killed
     */
    public int getTotalKillCount();
}
