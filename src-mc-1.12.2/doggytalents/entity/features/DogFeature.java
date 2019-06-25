package doggytalents.entity.features;

import doggytalents.entity.EntityDog;
import net.minecraft.nbt.NBTTagCompound;

public class DogFeature {

    protected EntityDog dog;
    
    public DogFeature(EntityDog dogIn) {
        this.dog = dogIn;
    }
    
    public void writeAdditional(NBTTagCompound compound) {

    }

    public void readAdditional(NBTTagCompound compound) {
        
    }
}
