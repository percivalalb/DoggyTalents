package doggytalents.common.event;

import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyItems;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.talent.HunterDogTalent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void rightClickEntity(final PlayerInteractEvent.EntityInteract event) {

        Level world = event.getLevel();

        ItemStack stack = event.getItemStack();
        Entity target = event.getTarget();

        if (target.getType() == EntityType.WOLF && target instanceof TamableAnimal && stack.getItem() == DoggyItems.TRAINING_TREAT.get()) {
            event.setCanceled(true);

            TamableAnimal wolf = (TamableAnimal) target;

            Player player = event.getEntity();

            if (wolf.isAlive() && wolf.isTame() && wolf.isOwnedBy(player)) {

                if (!world.isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    DogEntity dog = DoggyEntityTypes.DOG.get().create(world);
                    dog.tame(player);
                    dog.setHealth(dog.getMaxHealth());
                    dog.setOrderedToSit(false);
                    dog.setAge(wolf.getAge());
                    dog.absMoveTo(wolf.getX(), wolf.getY(), wolf.getZ(), wolf.getYRot(), wolf.getXRot());

                    world.addFreshEntity(dog);

                    wolf.discard();
                }

                event.setCancellationResult(InteractionResult.SUCCESS);
            } else {
                event.setCancellationResult(InteractionResult.FAIL);
            }
        }
    }

    @SubscribeEvent
    public void onEntitySpawn(final EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof AbstractSkeleton) {
            AbstractSkeleton skeleton = (AbstractSkeleton) entity;
            skeleton.goalSelector.addGoal(3, new AvoidEntityGoal<>(skeleton, DogEntity.class, 6.0F, 1.0D, 1.2D)); // Same goal as in AbstractSkeletonEntity
        }
    }

    @SubscribeEvent
    public void playerLoggedIn(final PlayerLoggedInEvent event) {
        if (ConfigHandler.SERVER.STARTING_ITEMS.get()) {

            Player player = event.getEntity();

            CompoundTag tag = player.getPersistentData();

            if (!tag.contains(Player.PERSISTED_NBT_TAG)) {
                tag.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
            }

            CompoundTag persistTag = tag.getCompound(Player.PERSISTED_NBT_TAG);

            if (!persistTag.getBoolean("gotDTStartingItems")) {
                persistTag.putBoolean("gotDTStartingItems", true);

                player.getInventory().add(new ItemStack(DoggyItems.DOGGY_CHARM.get()));
                player.getInventory().add(new ItemStack(DoggyItems.WHISTLE.get()));
            }
        }
    }

    @SubscribeEvent
    public void onLootDrop(final LootingLevelEvent event) {
        HunterDogTalent.onLootDrop(event);
    }
}
