package doggytalents.entity.data;

import net.minecraft.nbt.NBTTagCompound;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 **/
public class DogMode {

	public EntityDTDoggy doggy;
	
	public DogMode(EntityDTDoggy par1EntityDTDoggy) {
		this.doggy = par1EntityDTDoggy;
	}
	
	public EnumMode getMode() {
		int mode = doggy.getDataWatcher().getWatchableObjectInt(WatchableDataLib.ID_MODE);
		switch(mode) {
		case 0:
			return EnumMode.WANDERING;
		case 1:
			return EnumMode.DOCILE;
		case 2:
			return EnumMode.AGGRESIVE;
		case 3:
			return EnumMode.BERSERKER;
		case 4:
			return EnumMode.TACTICAL;
		default: 
			return EnumMode.UNKNOWN;
		}	
	}
	
	public EnumMode getMode(int par1) {
		int mode = par1;
		switch(mode) {
		case 0:
			return EnumMode.WANDERING;
		case 1:
			return EnumMode.DOCILE;
		case 2:
			return EnumMode.AGGRESIVE;
		case 3:
			return EnumMode.BERSERKER;
		case 4:
			return EnumMode.TACTICAL;
		default: 
			return EnumMode.UNKNOWN;
		}	
	}
	
	public static int getId(EnumMode mode) {
		switch(mode) {
		case WANDERING:
			return 0;
		case DOCILE:
			return 1;
		case AGGRESIVE:
			return 2;
		case BERSERKER:
			return 3;
		case TACTICAL:
			return 4;
		default: 
			return 100;
		}
	}
	
	public boolean isMode(EnumMode par1EnumMode) {
		return this.getMode() == par1EnumMode;
	}

	public void setMode(EnumMode par1EnumMode) {	
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_MODE, getId(par1EnumMode));
	}
	
	public int getDefaultInt() {
		return 1;
	}
	
	public void setMode(int par1) {	
		this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_MODE, par1);
	}
	
	protected int getModeID() {
		return doggy.getDataWatcher().getWatchableObjectInt(WatchableDataLib.ID_MODE);
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger("mode", getModeID());
	}
	
	public void readFromNBT(NBTTagCompound tag) {
		 int mode = tag.getInteger("mode"); 
		 this.doggy.getDataWatcher().updateObject(WatchableDataLib.ID_MODE, mode);
	}
}
