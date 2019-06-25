package doggytalents.entity.features;

import java.util.Arrays;
import java.util.Comparator;

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
        return this.dog.getMode();
    }
    
    public void setMode(EnumMode mode) {    
        this.dog.setMode(mode);
    }
    
    public boolean isMode(EnumMode mode) {
        return this.getMode() == mode;
    }
    
    @Override
    public void writeAdditional(NBTTagCompound tagCompound) {
        tagCompound.setInteger("mode", this.getMode().getIndex());
    }
    
    @Override
    public void readAdditional(NBTTagCompound tagCompound) {
        this.setMode(EnumMode.byIndex(tagCompound.getInteger("mode")));
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
        
        public EnumMode previousMode() {
            int i = this.getIndex() - 1;
            if(i < 0) {
                i = VALUES.length - 1;
            }
            return VALUES[i];
        }
        
        public EnumMode nextMode() {
            int i = this.getIndex() + 1;
            if(i >= VALUES.length) {
                i = 0;
            }
            return VALUES[i];
        }
        
        public static EnumMode byIndex(int i) {
            if(i < 0 | i >= VALUES.length) {
                i = EnumMode.DOCILE.getIndex();
            }
            return VALUES[i];
        }
    }
}
