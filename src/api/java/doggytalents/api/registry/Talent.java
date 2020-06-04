package doggytalents.api.registry;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.IDogAlteration;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author ProPercivalalb
 */
public abstract class Talent extends ForgeRegistryEntry<Talent> implements IDogAlteration {

    @Nullable
    private String translationKey, translationInfoKey;

    /**
     * Called when the talent is added to the dog and anytime the talent map is updated
     */
    public void init(DogEntity dog) {

    }

    public void set(DogEntity dog, int level) {

    }

    public void removed(DogEntity dog, int preLevel) {

    }

    public int getMaxLevel() {
        return 5;
    }

    public int getLevelCost(int toGoToLevel) {
        return toGoToLevel;
    }

    public int getCummulativeCost(int level) {
        return level * (level + 1) / 2;
    }

    public String getTranslationKey() {
        if(this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("talent", DoggyTalentsAPI.TALENTS.getKey(this));
        }
        return this.translationKey;
    }

    public String getInfoTranslationKey() {
        if(this.translationInfoKey == null) {
            this.translationInfoKey = this.getTranslationKey() + ".description";
        }
        return this.translationInfoKey;
    }


    public boolean hasRenderer() {
        return false;
    }
}
