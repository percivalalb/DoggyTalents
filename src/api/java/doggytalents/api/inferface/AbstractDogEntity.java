package doggytalents.api.inferface;

import java.util.UUID;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.google.common.base.Function;

import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.IDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public abstract class AbstractDogEntity extends TameableEntity implements IDog {

    protected AbstractDogEntity(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void setAttributeModifier(Attribute attribute, UUID modifierUUID, BiFunction<AbstractDogEntity, UUID, AttributeModifier> modifierGenerator) {
        ModifiableAttributeInstance attributeInst = this.getAttribute(attribute);

        AttributeModifier currentModifier = attributeInst.getModifier(modifierUUID);

        // Remove modifier if it exists
        if (currentModifier != null) {

            // Use UUID version as it is more efficient since
            // getModifier would need to be called again
            attributeInst.removeModifier(modifierUUID);
        }

        AttributeModifier newModifier = modifierGenerator.apply(this, modifierUUID);

        if (newModifier != null) {
            attributeInst.addTransientModifier(newModifier);
        }
    }

    public void removeAttributeModifier(Attribute attribute, UUID modifierUUID) {
        this.getAttribute(attribute).removeModifier(modifierUUID);
    }

    @Override
    public AbstractDogEntity getDog() {
        return this;
    }

    // Makes the method public
    @Override
    public float getSoundVolume() {
        return super.getSoundVolume();
    }

    @Override
    public void spawnTamingParticles(boolean play) {
        super.spawnTamingParticles(play);
    }

    public void consumeItemFromStack(@Nullable Entity entity, ItemStack stack) {
        if (entity instanceof PlayerEntity) {
            super.usePlayerItem((PlayerEntity) entity, stack);
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
