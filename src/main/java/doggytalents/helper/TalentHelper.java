package doggytalents.helper;

import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ProPercivalalb
 */
public class TalentHelper {

    public static void onClassCreation(EntityDog dog) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            talent.onClassCreation(dog);
    }

    public static void writeAdditional(EntityDog dog, NBTTagCompound compound) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            talent.writeAdditional(dog, compound);
    }

    public static void readAdditional(EntityDog dog, NBTTagCompound compound) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            talent.readAdditional(dog, compound);
    }

    public static EnumActionResult interactWithPlayer(EntityDog dog, EntityPlayer player, EnumHand hand) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
            EnumActionResult result = talent.onInteract(dog, player, hand);

            switch(result) {
                case PASS: continue;
                default: return result;
            }
        }

        return EnumActionResult.PASS;
    }

    public static void tick(EntityDog  dog) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            talent.tick(dog);
    }

    public static void livingTick(EntityDog dog) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            talent.livingTick(dog);
    }

    public static int hungerTick(EntityDog dog, int totalInTick) {
        int total = totalInTick;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            total = talent.onHungerTick(dog, total);
        return total;
    }

    public static int regenerationTick(EntityDog dog, int totalInTick) {
        int total = totalInTick;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            total = talent.onRegenerationTick(dog, total);
        return total;
    }

    public static int attackEntityAsMob(EntityDog dog, Entity entity, int damage) {
        int total = damage;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            total = talent.attackEntityAsMob(dog, entity, total);
        return total;
    }

    public static int changeFoodValue(EntityDog dog, ItemStack stack, int foodValue) {
        int total = foodValue;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            total = talent.changeFoodValue(dog, stack, total);
        return total;
    }

    public static int getUsedPoints(EntityDog dog) {
        int total = 0;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            total += talent.getCumulativeCost(dog, dog.TALENTS.getLevel(talent));
        return total;
    }

    public static boolean isPostionApplicable(EntityDog dog, PotionEffect potionEffect) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(!talent.isPostionApplicable(dog, potionEffect))
                return false;
        return true;
    }

    public static double addToMoveSpeed(EntityDog dog) {
        double total = 0;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            total += talent.addToMoveSpeed(dog);
        return total;
    }

    public static boolean canBreatheUnderwater(EntityDog dog) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(talent.canBreatheUnderwater(dog))
                return true;
        return false;
    }

    public static boolean canTriggerWalking(EntityDog dog) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(!talent.canTriggerWalking(dog))
                return false;
        return true;
    }

    public static boolean isImmuneToFalls(EntityDog dog) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(talent.isImmuneToFalls(dog))
                return true;
        return false;
    }

    public static int fallProtection(EntityDog dogIn) {
        int total = 0;
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
            ActionResult<Integer> result = talent.fallProtection(dogIn);

            switch(result.getType()) {
            case SUCCESS:
                total += result.getResult();
                break;
            default:
                continue;
            }
        }
        return total;
    }

    public static boolean attackEntityFrom(EntityDog dog, DamageSource damageSource, float damage) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(!talent.attackEntityFrom(dog, damageSource, damage))
                return false;
        return true;
    }

    public static boolean shouldDamageMob(EntityDog dog, Entity entity) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(!talent.shouldDamageMob(dog, entity))
                return false;
        return true;
    }

    public static boolean canAttackClass(EntityDog dog, Class entityClass) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(talent.canAttackClass(dog, entityClass))
                return true;
        return false;
    }

    public static boolean canAttackEntity(EntityDog dog, Entity entity) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(talent.canAttackEntity(dog, entity))
                return true;
        return false;
    }

    public static boolean setFire(EntityDog dog, int amount) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(!talent.setFire(dog, amount))
                return false;
        return true;
    }

    public static EnumActionResult canBeRiddenInWater(EntityDog dog, Entity rider) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues()) {
            EnumActionResult result = talent.canBeRiddenInWater(dog, rider);

            switch(result) {
                case PASS: continue;
                default: return result;
            }
        }

        return EnumActionResult.PASS;
    }

    public static void onFinishShaking(EntityDog dogIn, boolean gotWetInWater) {
        DoggyTalentsAPI.TALENTS.getValues().forEach(talent -> {
            talent.onFinishShaking(dogIn, gotWetInWater);
        });

    }

    public static boolean shouldDecreaseAir(EntityDog dogIn, int air) {
        for(Talent talent : DoggyTalentsAPI.TALENTS.getValues())
            if(!talent.shouldDecreaseAir(dogIn, air))
                return false;
        return true;
    }
}
