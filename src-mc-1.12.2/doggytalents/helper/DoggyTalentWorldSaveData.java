package doggytalents.helper;

import doggytalents.lib.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class DoggyTalentWorldSaveData extends WorldSavedData {
	private static final String DATA_NAME = Reference.MOD_ID + "_ExampleData";
	
	public DoggyTalentWorldSaveData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return null;
	}

}
