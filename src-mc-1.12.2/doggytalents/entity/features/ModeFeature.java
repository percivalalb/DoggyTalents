package doggytalents.entity.features;

import doggytalents.entity.EntityDog;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 **/
public class ModeFeature extends DogFeature {
	
	public ModeFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
	public EnumMode getMode() {
		return EnumMode.values()[this.getModeID()];
	}
	
	public EnumMode getMode(int index) {
		return EnumMode.values()[index];
	}
	
	public static int getId(EnumMode mode) {
		return mode.ordinal();
	}
	
	public boolean isMode(EnumMode mode) {
		return this.getMode() == mode;
	}

	public void setMode(EnumMode mode) {	
		this.setMode(this.getId(mode));
	}
	
	public void setMode(int mode) {	
		this.dog.setModeId(mode);
	}
	
	protected int getModeID() {
		return this.dog.getModeId();
	}
	
	@Override
	public void writeAdditional(NBTTagCompound tagCompound) {
		tagCompound.setInteger("mode", this.getModeID());
	}
	
	@Override
	public void readAdditional(NBTTagCompound tagCompound) {
		this.setMode(tagCompound.getInteger("mode"));
	}
	
	public enum EnumMode {

		DOCILE("docile"),
		WANDERING("wandering"),
		AGGRESIVE("aggressive"),
		BERSERKER("berserker"),
		TACTICAL("tactical");
		//PATROL("patrol");
		
		private String unlocalisedTip;
		private String unlocalisedName;
		private String unlocalisedInfo;

		private EnumMode(String name) {
			this("dog.mode." + name, "dog.mode." + name + ".indicator", "dog.mode." + name + ".description");
		}
		
		private EnumMode(String unlocalisedName, String tip, String info) {
			this.unlocalisedName = unlocalisedName;
			this.unlocalisedTip = tip;
			this.unlocalisedInfo = info;
		}
		
		public String getTip() {
			return this.unlocalisedTip;
		}
		
		public String getUnlocalisedName() {
			return this.unlocalisedName;
		}
		
		public String getUnlocalisedInfo() {
			return this.unlocalisedInfo;
		}
	}
}
