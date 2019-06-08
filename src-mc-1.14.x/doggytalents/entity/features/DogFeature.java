package doggytalents.entity.features;

import doggytalents.entity.EntityDog;
import net.minecraft.nbt.CompoundNBT;

public class DogFeature {

	protected EntityDog dog;
	
	public DogFeature(EntityDog dogIn) {
		this.dog = dogIn;
	}
	
	public void writeAdditional(CompoundNBT compound) {

	}

	public void readAdditional(CompoundNBT compound) {
		
	}
}
