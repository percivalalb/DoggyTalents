package doggytalents.api.feature;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;

public interface IDog {

    public ICoordFeature getCoordFeature();
    public IGenderFeature getGenderFeature();
    public ILevelFeature getLevelFeature();
    public IModeFeature getModeFeature();
    public IStatsFeature getStatsFeature();
    public ITalentFeature getTalentFeature();
    public IHungerFeature getHungerFeature();

    public TameableEntity getDog();

    public boolean canInteract(LivingEntity playerIn);

    public int getDogSize();

    public void setDogSize(int size);

    /**
     * @param key
     * @param
     */
    public <T> void putObject(String key, T i);

    public <T> T getObject(String key, Class<T> type);
}
