package doggytalents.entity;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ProPercivalalb
 **/
public class ModeUtil {

	public EntityDog dog;
	
	public ModeUtil(EntityDog dog) {
		this.dog = dog;
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
		this.dog.getDataWatcher().set(EntityDog.MODE, Math.min(mode, EnumMode.values().length - 1));
	}
	
	protected int getModeID() {
		return this.dog.getDataWatcher().get(EntityDog.MODE);
	}
	
	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("mode", this.getModeID());
	}
	
	public void readFromNBT(NBTTagCompound tagCompound) {
		this.setMode(tagCompound.getInteger("mode"));
	}
	
	public enum EnumMode {

		DOCILE("docile"),
		WANDERING("wandering", false),
		AGGRESIVE("aggressive"),
		BERSERKER("berserker"),
		TACTICAL("tactical");
		//PATROL("patrol", false);
		
		private String unlocalisedTip;
		private String unlocalisedName;
		private String unlocalisedInfo;
		/** By default it is true */
		public boolean followsOwner;
		
		private EnumMode(String name) {
			this(name, true);
		}
		
		private EnumMode(String name, boolean followsOwner) {
			this("doggui.modename." + name, "doggui.modetip." + name, "doggui.modeinfo." + name, followsOwner);
		}
		
		private EnumMode(String name, String tip, String info) {
			this(name, tip, info, true);
		}
		
		private EnumMode(String unlocalisedName, String tip, String info, boolean followsOwner) {
			this.unlocalisedName = unlocalisedName;
			this.unlocalisedTip = tip;
			this.unlocalisedInfo = info;
			this.followsOwner = followsOwner;
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
		
		public boolean doesFollowOwner() {
			return this.followsOwner;
		}
	}
}
