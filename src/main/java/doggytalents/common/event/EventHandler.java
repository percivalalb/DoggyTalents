package doggytalents.common.event;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyItems;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.talent.HunterDogTalent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void rightClickEntity(final PlayerInteractEvent.EntityInteract event) {

        World world = event.getWorld();

        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();

        if (target.getType() == EntityType.WOLF && target instanceof TameableEntity && stack.getItem() == DoggyItems.TRAINING_TREAT.get()) {
            event.setCanceled(true);

            TameableEntity wolf = (TameableEntity) target;

            PlayerEntity player = event.getPlayer();

            if (wolf.isAlive() && wolf.isTamed() && wolf.isOwner(player)) {

                if (!world.isRemote) {
                    if (!player.abilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    DogEntity dog = DoggyEntityTypes.DOG.get().create(world);
                    dog.setTamedBy(player);
                    dog.setHealth(dog.getMaxHealth());
                    dog.setSitting(false);
                    dog.setGrowingAge(wolf.getGrowingAge());
                    dog.setPositionAndRotation(wolf.getPosX(), wolf.getPosY(), wolf.getPosZ(), wolf.rotationYaw, wolf.rotationPitch);

                    world.addEntity(dog);

                    wolf.remove();
                }

                event.setCancellationResult(ActionResultType.SUCCESS);
            } else {
                event.setCancellationResult(ActionResultType.FAIL);
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(final EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof AbstractSkeletonEntity) {
            AbstractSkeletonEntity skeleton = (AbstractSkeletonEntity) entity;
            skeleton.goalSelector.addGoal(3, new AvoidEntityGoal<>(skeleton, DogEntity.class, 6.0F, 1.0D, 1.2D)); // Same goal as in AbstractSkeletonEntity
        }
    }

    @SubscribeEvent
    public void playerLoggedIn(final PlayerLoggedInEvent event) {
        if (ConfigValues.STARTING_ITEMS) {

            PlayerEntity player = event.getPlayer();

            CompoundNBT tag = player.getPersistentData();

            if (!tag.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                tag.put(PlayerEntity.PERSISTED_NBT_TAG, new CompoundNBT());
            }

            CompoundNBT persistTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);

            if (!persistTag.getBoolean("gotDTStartingItems")) {
                persistTag.putBoolean("gotDTStartingItems", true);

                player.inventory.addItemStackToInventory(new ItemStack(DoggyItems.DOGGY_CHARM.get()));
                player.inventory.addItemStackToInventory(new ItemStack(DoggyItems.WHISTLE.get()));
            }
        }
    }

    @SubscribeEvent
    public void onLootDrop(final LootingLevelEvent event) {
        HunterDogTalent.onLootDrop(event);
    }
}
