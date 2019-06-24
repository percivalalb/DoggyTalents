package doggytalents.entity.features;

import java.util.Arrays;
import java.util.Comparator;

import doggytalents.entity.EntityDog;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author ProPercivalalb
 **/
public class ModeFeature extends DogFeature {
	
	public ModeFeature(EntityDog dogIn) {
		super(dogIn);
	}
	
	public EnumMode getMode() {
		return this.dog.getMode();
	}
	
	public void setMode(int index) {
        if(index < 0 || index >= EnumMode.VALUES.length) {
            index = EnumMode.DOCILE.getIndex();
        }
        this.setMode(EnumMode.VALUES[index]);
    }
	
    public void setMode(EnumMode mode) {    
        this.dog.setMode(mode);
    }
	
	public boolean isMode(EnumMode mode) {
		return this.getMode() == mode;
	}
	
	@Override
	public void writeAdditional(CompoundNBT tagCompound) {
		tagCompound.putInt("mode", this.getMode().getIndex());
	}
	
	@Override
	public void readAdditional(CompoundNBT tagCompound) {
		this.setMode(tagCompound.getInt("mode"));
	}
	
	public enum EnumMode {

		DOCILE(0, "docile"),
		WANDERING(1, "wandering"),
		AGGRESIVE(2, "aggressive"),
		BERSERKER(3, "berserker"),
		TACTICAL(4, "tactical");
		//PATROL(5, "patrol");
		
	    private int index;
		private String unlocalisedTip;
		private String unlocalisedName;
		private String unlocalisedInfo;

		public static final EnumMode[] VALUES = Arrays.stream(EnumMode.values()).sorted(Comparator.comparingInt(EnumMode::getIndex)).toArray(size -> {
            return new EnumMode[size];
        });
		
		private EnumMode(int index, String name) {
			this(index, "dog.mode." + name, "dog.mode." + name + ".indicator", "dog.mode." + name + ".description");
		}
		
		private EnumMode(int index, String unlocalisedName, String tip, String info) {
		    this.index = index;
			this.unlocalisedName = unlocalisedName;
			this.unlocalisedTip = tip;
			this.unlocalisedInfo = info;
		}
		
		public int getIndex() {
		    return this.index;
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
