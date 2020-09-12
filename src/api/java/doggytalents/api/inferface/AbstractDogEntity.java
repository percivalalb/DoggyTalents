package doggytalents.api.inferface;

import javax.annotation.Nullable;

import com.google.common.base.Function;

import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.IDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public abstract class AbstractDogEntity extends TameableEntity implements IDog {

    public static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute)null, "generic.jumpStrength", 0.0D, 0.0D, 8.0D)).setShouldWatch(true);

    protected AbstractDogEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public TameableEntity getDog() {
        return this;
    }

    // Makes the method public
    @Override
    public float getSoundVolume() {
        return super.getSoundVolume();
    }

    @Override
    public void playTameEffect(boolean play) {
        super.playTameEffect(play);
    }

    public void consumeItemFromStack(@Nullable Entity entity, ItemStack stack) {
        if (entity instanceof PlayerEntity) {
            super.consumeItemFromStack((PlayerEntity) entity, stack);
        } else {
            stack.shrink(1);
        }
    }

    public abstract TranslationTextComponent getTranslationKey(Function<EnumGender, String> function);

    public TranslationTextComponent getGenderPronoun() {
        return this.getTranslationKey(EnumGender::getUnlocalisedPronoun);
    }

    public TranslationTextComponent getGenderSubject() {
        return this.getTranslationKey(EnumGender::getUnlocalisedSubject);
    }

    public TranslationTextComponent getGenderTitle() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTitle);
    }

    public TranslationTextComponent getGenderTip() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTip);
    }

    public TranslationTextComponent getGenderName() {
        return this.getTranslationKey(EnumGender::getUnlocalisedName);
    }
}
