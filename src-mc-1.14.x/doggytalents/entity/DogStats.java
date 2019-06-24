package doggytalents.entity;

import doggytalents.entity.features.DogFeature;
import net.minecraft.nbt.CompoundNBT;

public class DogStats extends DogFeature {

    public DogStats(EntityDog dogIn) {
        super(dogIn);
    }

    public CompoundNBT write(CompoundNBT compound) {
        return compound;
    }
    
    public void read(CompoundNBT compound) {
        
    }
}
