package doggytalents.entity.ai;

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
		this.dog.dataTracker.setModeId(mode);
	}
	
	protected int getModeID() {
		return this.dog.dataTracker.getModeId();
	}
	
	@Override
	public void writeAdditional(NBTTagCompound tagCompound) {
		tagCompound.setInt("mode", this.getModeID());
	}
	
	@Override
	public void readAdditional(NBTTagCompound tagCompound) {
		this.setMode(tagCompound.getInt("mode"));
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
