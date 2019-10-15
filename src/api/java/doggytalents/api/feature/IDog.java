package doggytalents.api.feature;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public interface IDog {

    public ICoordFeature getCoordFeature();
    public IGenderFeature getGenderFeature();
    public ILevelFeature getLevelFeature();
    public IModeFeature getModeFeature();
    public IStatsFeature getStatsFeature();
    public ITalentFeature getTalentFeature();
    public IHungerFeature getHungerFeature();

    public EntityTameable getDog();

    public boolean canInteract(EntityLivingBase playerIn);

    public int getDogSize();

    public void setDogSize(int size);

    /**
     * @param key
     * @param
     */
    public <T> void putObject(String key, T i);

    public <T> T getObject(String key, Class<T> type);
}
