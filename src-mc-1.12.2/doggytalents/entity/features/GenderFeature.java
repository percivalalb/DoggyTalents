package doggytalents.entity.features;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class GenderFeature extends DogFeature {
    
    public GenderFeature(EntityDog dogIn) {
        super(dogIn);
    }
    
    public EnumGender getGender() {
        return this.dog.getGender();
    }
    
    public void setGender(EnumGender gender) {
        this.dog.setGender(gender);
    }
    
    public TextComponentTranslation getTranslationKey(Function<EnumGender, String> function) {
        return new TextComponentTranslation(function.apply(ConfigValues.DOG_GENDER ? this.getGender() : EnumGender.UNISEX));
    }
    
    public boolean canMateWith(EntityDog matedog) {
        if(ConfigValues.DOG_GENDER) {
            boolean equalGenders = this.getGender() == matedog.getGender();
            return (equalGenders && this.getGender() == EnumGender.UNISEX) || !equalGenders;
        }
        
        return true;
    }
    
    public TextComponentTranslation getGenderPronoun() {
        return this.getTranslationKey(EnumGender::getUnlocalisedPronoun);
    }

    public TextComponentTranslation getGenderSubject() {
        return this.getTranslationKey(EnumGender::getUnlocalisedSubject);
    }
    
    public TextComponentTranslation getGenderTitle() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTitle);
    }
    
    public TextComponentTranslation getGenderTip() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTip);
    }
    
    public TextComponentTranslation getGenderName() {
        return this.getTranslationKey(EnumGender::getUnlocalisedName);
    }
    
    @Override
    public void writeAdditional(NBTTagCompound compound) {
        if(ConfigValues.DOG_GENDER) {
            compound.setString("dogGender", this.getGender().getSaveName());
        }
    }
    
    @Override
    public void readAdditional(NBTTagCompound compound) {
        if(compound.hasKey("dogGender", 8)) {
            this.setGender(EnumGender.bySaveName(compound.getString("dogGender")));
        } else if(ConfigValues.DOG_GENDER) {
            this.setGender(this.dog.getRNG().nextBoolean() ? EnumGender.MALE : EnumGender.FEMALE);
        }
    }
    
    public enum EnumGender {

        MALE(1, "male"),
        FEMALE(2, "female"),
        UNISEX(0, "unisex");
        
        private int index;
        private String saveName;
        private String unlocalisedTip;
        private String unlocalisedName;
        private String unlocalisedPronoun;
        private String unlocalisedSubject;
        private String unlocalisedTitle;
        
        public static final EnumGender[] VALUES = Arrays.stream(EnumGender.values()).sorted(Comparator.comparingInt(EnumGender::getIndex)).toArray(size -> {
            return new EnumGender[size];
        });
        
        private EnumGender(int index, String name) {
            this.index = index;
            this.saveName = name;
            this.unlocalisedName = "dog.gender." + name;
            this.unlocalisedTip = this.unlocalisedName + ".indicator";
            this.unlocalisedPronoun = this.unlocalisedName + ".pronoun";
            this.unlocalisedSubject = this.unlocalisedName + ".subject";
            this.unlocalisedTitle = this.unlocalisedName + ".title";
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public String getSaveName() {
            return this.saveName;
        }
        
        public String getUnlocalisedTip() {
            return this.unlocalisedTip;
        }
        
        public String getUnlocalisedName() {
            return this.unlocalisedName;
        }
        
        public String getUnlocalisedPronoun() {
            return this.unlocalisedPronoun;
        }
        
        public String getUnlocalisedSubject() {
            return this.unlocalisedSubject;
        }
        
        public String getUnlocalisedTitle() {
            return this.unlocalisedTitle;
        }
        
        public static EnumGender byIndex(int i) {
            if(i < 0 | i >= VALUES.length) {
                i = EnumGender.UNISEX.getIndex();
            }
            return VALUES[i];
        }
        
        public static EnumGender bySaveName(String saveName) {
            for(EnumGender gender : EnumGender.values()) {
                if(gender.getSaveName().equals(saveName)) {
                    return gender;
                }
            }
            
            return UNISEX;
        }
    }
}
