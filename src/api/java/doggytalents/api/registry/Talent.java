package doggytalents.api.registry;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogAlteration;
import net.minecraft.util.Util;
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
    public void init(AbstractDogEntity dog) {

    }

    public void set(AbstractDogEntity dog, int level) {

    }

    public void removed(AbstractDogEntity dog, int preLevel) {

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
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("talent", DoggyTalentsAPI.TALENTS.getKey(this));
        }
        return this.translationKey;
    }

    public String getInfoTranslationKey() {
        if (this.translationInfoKey == null) {
            this.translationInfoKey = this.getTranslationKey() + ".description";
        }
        return this.translationInfoKey;
    }


    public boolean hasRenderer() {
        return false;
    }
}
