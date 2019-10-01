package doggytalents.entity.features;

import java.util.function.Function;

import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.IGenderFeature;
import doggytalents.api.inferface.IDogEntity;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

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

    @Override
    public boolean canMateWith(IDogEntity matedog) {
        if(ConfigValues.DOG_GENDER) {
            boolean equalGenders = this.getGender() == matedog.getGenderFeature().getGender();
            return (equalGenders && this.getGender() == EnumGender.UNISEX) || !equalGenders;
        }

        return true;
    }

    private TranslationTextComponent getTranslationKey(Function<EnumGender, String> function) {
        return new TranslationTextComponent(function.apply(ConfigValues.DOG_GENDER ? this.getGender() : EnumGender.UNISEX));
    }

    @Override
    public TranslationTextComponent getGenderPronoun() {
        return this.getTranslationKey(EnumGender::getUnlocalisedPronoun);
    }

    @Override
    public TranslationTextComponent getGenderSubject() {
        return this.getTranslationKey(EnumGender::getUnlocalisedSubject);
    }

    @Override
    public TranslationTextComponent getGenderTitle() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTitle);
    }

    @Override
    public TranslationTextComponent getGenderTip() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTip);
    }

    @Override
    public TranslationTextComponent getGenderName() {
        return this.getTranslationKey(EnumGender::getUnlocalisedName);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        if(ConfigValues.DOG_GENDER) {
            compound.putString("dogGender", this.getGender().getSaveName());
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        if(compound.contains("dogGender", Constants.NBT.TAG_STRING)) {
            this.setGender(EnumGender.bySaveName(compound.getString("dogGender")));
        } else if(ConfigValues.DOG_GENDER) {
            this.setGender(this.dog.getRNG().nextBoolean() ? EnumGender.MALE : EnumGender.FEMALE);
        }
    }
}
