package doggytalents.common.item;

import doggytalents.api.inferface.IThrowableItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

/**
 * @author ProPercivalalb
 **/
public class ThrowableItem extends Item implements IThrowableItem {

    public Supplier<? extends Item> altBone;
    public Supplier<? extends Item> renderBone;

    public ThrowableItem(Supplier<? extends Item> altBone, Supplier<? extends Item> renderBone, Properties properties) {
        super(properties);
        this.altBone = altBone;
        this.renderBone = renderBone;
    }

    @Override
    public ItemStack getReturnStack(ItemStack stack) {
        ItemStack returnStack = new ItemStack(this.altBone.get());
        if (stack.hasTag()) {
            returnStack.setTag(stack.getTag().copy());
        }

        return returnStack;
    }

    @Override
    public ItemStack getRenderStack(ItemStack stack) {
        return new ItemStack(this.renderBone.get());
    }

    public void setHeadingFromThrower(ItemEntity entityItem, Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        float f = -Mth.sin(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((rotationPitchIn + pitchOffset) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(rotationYawIn * ((float)Math.PI / 180F)) * Mth.cos(rotationPitchIn * ((float)Math.PI / 180F));
        this.setThrowableHeading(entityItem, f, f1, f2, velocity, inaccuracy);
        Vec3 vec3d = entityThrower.getDeltaMovement();
        entityItem.setDeltaMovement(entityItem.getDeltaMovement().add(vec3d.x, entityThrower.isOnGround() ? 0.0D : vec3d.y, vec3d.z));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);

        worldIn.playSound((Player)null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isClientSide) {
            ItemStack stack = itemStackIn.copy();
            stack.setCount(1);
            ItemEntity entityitem = new ItemEntity(playerIn.level, playerIn.getX(), (playerIn.getY() - 0.30000001192092896D) + playerIn.getEyeHeight(), playerIn.getZ(), stack);
            entityitem.setPickUpDelay(20);
            this.setHeadingFromThrower(entityitem, playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0F, 1.2F, 1.0F);
            worldIn.addFreshEntity(entityitem);
        }

        if (!playerIn.getAbilities().instabuild)
            itemStackIn.shrink(1);

        playerIn.awardStat(Stats.ITEM_USED.get(this));
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, itemStackIn);

    }

    public void setThrowableHeading(ItemEntity entityItem, double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3d = (new Vec3(x, y, z)).normalize().add(entityItem.level.random.nextGaussian() * 0.0075F * inaccuracy, entityItem.level.random.nextGaussian() * 0.0075F * inaccuracy, entityItem.level.random.nextGaussian() * 0.0075F * inaccuracy).scale(velocity);
        entityItem.setDeltaMovement(vec3d);
        float f = Mth.sqrt((float) (vec3d.x * vec3d.x + vec3d.z * vec3d.z));
        entityItem.setYRot((float)(Mth.atan2(vec3d.x, vec3d.z) * (180F / (float)Math.PI)));
        entityItem.setXRot((float)(Mth.atan2(vec3d.y, f) * (180F / (float)Math.PI)));
        entityItem.yRotO =  entityItem.getYRot();
        entityItem.xRotO = entityItem.getXRot();
    }
}
