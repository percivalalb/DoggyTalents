package doggytalents.entity.features;

import doggytalents.api.feature.IHungerFeature;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;

public class HungerFeature extends DogFeature implements IHungerFeature {

    public HungerFeature(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public int getDogHunger() {
        return this.dog.getDogHunger();
    }

    @Override
    public boolean isIncapacicated() {
        return this.dog.isIncapacicated();
    }

    @Override
    public void setDogHunger(int i) {
        this.dog.setDogHunger(i);
    }

    @Override
    public int getMaxHunger() {
        return ConfigValues.HUNGER_POINTS;
    }
}
