package doggytalents.entity.features;

import java.util.function.Function;

import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.IGenderFeature;
import doggytalents.api.inferface.IDogEntity;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

public class GenderFeature extends DogFeature implements IGenderFeature {

    public GenderFeature(EntityDog dogIn) {
        super(dogIn);
    }

    @Override
    public EnumGender getGender() {
        return this.dog.getGender();
    }

    @Override
    public void setGender(EnumGender gender) {
        this.dog.setGender(gender);
    }

    public TextComponentTranslation getTranslationKey(Function<EnumGender, String> function) {
        return new TextComponentTranslation(function.apply(ConfigValues.DOG_GENDER ? this.getGender() : EnumGender.UNISEX));
    }

    @Override
    public boolean canMateWith(IDogEntity matedog) {
        if(ConfigValues.DOG_GENDER) {
            boolean equalGenders = this.getGender() == matedog.getGenderFeature().getGender();
            return (equalGenders && this.getGender() == EnumGender.UNISEX) || !equalGenders;
        }

        return true;
    }

    @Override
    public TextComponentTranslation getGenderPronoun() {
        return this.getTranslationKey(EnumGender::getUnlocalisedPronoun);
    }

    @Override
    public TextComponentTranslation getGenderSubject() {
        return this.getTranslationKey(EnumGender::getUnlocalisedSubject);
    }

    @Override
    public TextComponentTranslation getGenderTitle() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTitle);
    }

    @Override
    public TextComponentTranslation getGenderTip() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTip);
    }

    @Override
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
}
