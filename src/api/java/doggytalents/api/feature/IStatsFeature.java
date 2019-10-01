package doggytalents.api.feature;

import net.minecraft.entity.EntityType;

public interface IStatsFeature {

    /**
     * Gets the number of times the dog has killed the given
     * entity type.
     * @param type The {@link net.minecraft.entity.EntityType}
     * @return The kill count for the entity type
     */
    public int getKillCountFor(EntityType<?> type);

    /**
     * Gets the total number of entities killed
     * @return The number of entities killed
     */
    public int getTotalKillCount();
}
