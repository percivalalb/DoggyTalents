package doggytalents.api.inferface;

import javax.annotation.Nullable;

import doggytalents.api.DoggyTalentsAPI;
import io.netty.handler.codec.http2.Http2FrameLogger.Direction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

/**
 * @author ProPercivalalb
 */
public abstract class Talent extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Talent> {

    @Nullable
    private String translationKey, translationInfoKey;

    public void onClassCreation(IDogEntity dogIn) {}
    public void writeAdditional(IDogEntity dogIn, NBTTagCompound compound) {}
    public void readAdditional(IDogEntity dogIn, NBTTagCompound compound) {}

    /**
     * PASS will indicate no action is required
     * SUCCESS and FAIL results are passed to the final interact
     */
    public EnumActionResult onInteract(IDogEntity dogIn, EntityPlayer playerIn, EnumHand handIn) {
        return EnumActionResult.PASS;
    }

    public void tick(IDogEntity dog) {}
    public void livingTick(IDogEntity dog) {}
    public int onHungerTick(IDogEntity dog, int totalInTick) { return totalInTick; }
    public int onRegenerationTick(IDogEntity dog, int totalInTick) { return totalInTick; }
    public int attackEntityAsMob(IDogEntity dog, Entity entity, int damage) { return damage; }
    public int changeFoodValue(IDogEntity dog, ItemStack stack, int foodValue) { return foodValue; }
    public EnumActionResult isPostionApplicable(IDogEntity dog, PotionEffect potionEffect) { return EnumActionResult.PASS; }
    public double addToMoveSpeed(IDogEntity dog) { return 0.0D; }
    public boolean canBreatheUnderwater(IDogEntity dog) { return false; }
    public boolean canTriggerWalking(IDogEntity dog) { return true; }
    public boolean isImmuneToFalls(IDogEntity dog) { return false; }

    /**
     * Will apply the reduction in number of blocks fell when result is SUCCESS
     * PASS and FAIL will have no effect
     */
    public ActionResult<Integer> fallProtection(IDogEntity dog) {
        return ActionResult.newResult(EnumActionResult.PASS, 0);
    }
    public boolean attackEntityFrom(IDogEntity dog, DamageSource damageSource, float damage) { return true; }
    public boolean shouldDamageMob(IDogEntity dog, Entity entity) { return true; }
    public boolean canAttackClass(IDogEntity dog, Class<? extends EntityLivingBase> entityType) { return false; }
    public boolean canAttackEntity(IDogEntity dog, Entity entity) { return false; }
    public boolean setFire(IDogEntity dog, int amount) { return true; }
    public EnumActionResult canBeRiddenInWater(IDogEntity dog, Entity rider) { return EnumActionResult.PASS; }
    public void onFinishShaking(IDogEntity dogIn, boolean gotWetInWater) {}
    public boolean shouldDecreaseAir(IDogEntity dogIn, int air) { return true; }
    public void onLevelSet(IDogEntity dog, int postLevel) {}
    public void onLevelReset(IDogEntity dog, int preLevel) {}

    public <T> T getCapability(IDogEntity dogIn, Capability<T> cap, Direction side) { return null; }
    public void invalidateCapabilities(IDogEntity dogIn) {}

    public int getHighestLevel(IDogEntity dog) { return 5; }
    //public int getTotalCost(IDogEntity dog) { return 15; }

    public int getCumulativeCost(IDogEntity dog, int level) {
        switch(level) {
        case 1: return 1;
        case 2: return 3;
        case 3: return 6;
        case 4: return 10;
        case 5: return 15;
        default: return 0;
        }
    }

    public int getCost(IDogEntity dog, int level) {
        return level;
    }

    public String getTranslationKey() {
        if(this.translationKey == null) {
            this.translationKey = makeTranslationKey("talent", DoggyTalentsAPI.TALENTS.getKey(this));
        }
        return this.translationKey;
    }

    // 1.13 function Util.make(String, ResourceLocation)
    public static String makeTranslationKey(String type, @Nullable ResourceLocation id) {
        return id == null ? type + ".unregistered_sadface" : type + '.' + id.getNamespace() + '.' + id.getPath().replace('/', '.');
    }

    public String getInfoTranslationKey() {
        if(this.translationInfoKey == null) {
            this.translationInfoKey = this.getTranslationKey() + ".description";
        }
        return this.translationInfoKey;
    }
}
