package doggytalents.common.event;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyItems;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void rightClickEntity(final PlayerInteractEvent.EntityInteract event) {

        World world = event.getWorld();

        if(!world.isRemote) {

            ItemStack stack = event.getItemStack();
            Entity target = event.getTarget();

            if(target instanceof WolfEntity && stack.getItem() == DoggyItems.TRAINING_TREAT.get()) {
                WolfEntity wolf = (WolfEntity) target;

                PlayerEntity player = event.getPlayer();

                if(wolf.isAlive() && wolf.isTamed() && wolf.isOwner(player)) {

                    if(!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    DogEntity dog = DoggyEntityTypes.DOG.get().create(world);
                    dog.setTamedBy(player);
                    dog.setHealth(dog.getMaxHealth());
                    dog.getAISit().setSitting(false);
                    dog.setGrowingAge(wolf.getGrowingAge());
                    dog.setPositionAndRotation(wolf.getPosX(), wolf.getPosY(), wolf.getPosZ(), wolf.rotationYaw, wolf.rotationPitch);

                    world.addEntity(dog);

                    wolf.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(final EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof AbstractSkeletonEntity) {
            AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity) entity;
            skeleton.goalSelector.addGoal(3, new AvoidEntityGoal<>(skeleton, DogEntity.class, 6.0F, 1.0D, 1.2D));
        }
    }

    @SubscribeEvent
    public  void playerLoggedIn(final PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();

        CompoundNBT tag = player.getPersistentData();

        if(!tag.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            tag.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
        }

        CompoundNBT persistTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);

        if(ConfigValues.STARTING_ITEMS && !persistTag.getBoolean("gotDTStartingItems")) {
            persistTag.putBoolean("gotDTStartingItems", true);

            player.inventory.addItemStackToInventory(new ItemStack(DoggyItems.DOGGY_CHARM.get()));
            player.inventory.addItemStackToInventory(new ItemStack(DoggyItems.WHISTLE.get()));
        }
    }


}
