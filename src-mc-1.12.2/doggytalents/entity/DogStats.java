package doggytalents.entity;

import doggytalents.entity.features.DogFeature;
import net.minecraft.nbt.NBTTagCompound;

public class DogStats extends DogFeature {

	public DogStats(EntityDog dogIn) {
		super(dogIn);
	}

	public NBTTagCompound write(NBTTagCompound compound) {
		return compound;
	}
	
	public void read(NBTTagCompound compound) {
		
	}
}
