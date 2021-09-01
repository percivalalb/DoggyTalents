package doggytalents.api.inferface;

import java.util.UUID;
import java.util.function.BiFunction;

import javax.annotation.Nullable;

import com.google.common.base.Function;

import doggytalents.api.feature.EnumGender;
import doggytalents.api.feature.IDog;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

public abstract class AbstractDogEntity extends TamableAnimal implements IDog {

    protected AbstractDogEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
        super(type, worldIn);
    }

    public void setAttributeModifier(Attribute attribute, UUID modifierUUID, BiFunction<AbstractDogEntity, UUID, AttributeModifier> modifierGenerator) {
        AttributeInstance attributeInst = this.getAttribute(attribute);

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
        if (entity instanceof Player) {
            // Review: super.usePlayerItem((Player) entity, stack);
            stack.shrink(1);
        } else {
            stack.shrink(1);
        }
    }

    public abstract TranslatableComponent getTranslationKey(Function<EnumGender, String> function);

    public TranslatableComponent getGenderPronoun() {
        return this.getTranslationKey(EnumGender::getUnlocalisedPronoun);
    }

    public TranslatableComponent getGenderSubject() {
        return this.getTranslationKey(EnumGender::getUnlocalisedSubject);
    }

    public TranslatableComponent getGenderTitle() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTitle);
    }

    public TranslatableComponent getGenderTip() {
        return this.getTranslationKey(EnumGender::getUnlocalisedTip);
    }

    public TranslatableComponent getGenderName() {
        return this.getTranslationKey(EnumGender::getUnlocalisedName);
    }
}
