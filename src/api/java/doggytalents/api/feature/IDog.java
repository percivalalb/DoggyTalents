package doggytalents.api.feature;

import java.util.List;
import java.util.function.Supplier;

import doggytalents.api.registry.AccessoryInstance;
import doggytalents.api.registry.Talent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;

// TODO: Add javadoc
public interface IDog {

    public TameableEntity getDog();

    public void untame();

    public boolean canInteract(LivingEntity playerIn);

    public EnumMode getMode();

    public DogLevel getLevel();
    public void increaseLevel(DogLevel.Type typeIn);

    /**
     * Convenience method to get the level of a talent
     * @param talentGetter A getter function, typically a {@link RegistryObject<Talent>} would be provided
     * @return The level of the talent
     */
    default int getLevel(Supplier<? extends Talent> talentGetter) {
        return this.getLevel(talentGetter.get());
    }

    /**
     * Returns the level of the given talent
     * @param talentIn The {@link Talent}
     * @return The level of the talent
     */
    public int getLevel(Talent talentIn);

    public int getDogSize();
    public void setDogSize(int size);

    public float getMaxHunger();
    public float getDogHunger();
    public void addHunger(float add);
    public void setDogHunger(float hunger);

    public boolean addAccessory(AccessoryInstance inst);
    public List<AccessoryInstance> getAccessories();
    public List<AccessoryInstance> removeAccessories();
    public void markAccessoriesDirty();

    public float getWagAngle(float limbSwing, float limbSwingAmount, float partialTickTime);
    public float getShakeAngle(float partialTicks, float p_70923_2_);
    public float getInterestedAngle(float partialTicks);

    public boolean isLying();

    public <T> void setData(DataKey<T> key, T value);
    /**
     * Tries to put the object in the map, does nothing if the key already exists
     */
    public <T> void setDataIfEmpty(DataKey<T> key, T value);
    public <T> T getData(DataKey<T> key);
    public <T> T getDataOrGet(DataKey<T> key, Supplier<T> other);
    public <T> T getDataOrDefault(DataKey<T> key, T other);
    public <T> boolean hasData(DataKey<T> key);
}
