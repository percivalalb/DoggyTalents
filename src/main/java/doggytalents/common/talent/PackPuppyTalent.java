package doggytalents.common.talent;

import doggytalents.DoggyTags;
import doggytalents.DoggyTalents;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.inventory.PackPuppyItemHandler;
import doggytalents.common.util.InventoryUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.function.Predicate;

public class PackPuppyTalent extends TalentInstance {

    public static Capability<PackPuppyItemHandler> PACK_PUPPY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});;

    private PackPuppyItemHandler packPuppyHandler;
    private LazyOptional<?> lazyPackPuppyHandler;

    public static Predicate<ItemEntity> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
        return entity.isAlive() && !entity.hasPickUpDelay() && !entity.getItem().is(DoggyTags.PACK_PUPPY_BLACKLIST);// && !EntityAIFetch.BONE_PREDICATE.test(entity.getItem());
    };

    public PackPuppyTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
        PackPuppyItemHandler handler = new PackPuppyItemHandler();
        this.packPuppyHandler = handler;
        this.lazyPackPuppyHandler = LazyOptional.of(() -> handler);
    }

    public PackPuppyItemHandler inventory() {
        return this.packPuppyHandler;
    }

    @Override
    public void tick(AbstractDogEntity dogIn) {
        if (dogIn.isAlive() && !dogIn.level.isClientSide && this.level() >= 5) {
            List<ItemEntity> list = dogIn.level.getEntitiesOfClass(ItemEntity.class, dogIn.getBoundingBox().inflate(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);

            if (!list.isEmpty()) {
                for (ItemEntity entityItem : list) {
                    ItemStack remaining = InventoryUtil.addItem(this.packPuppyHandler, entityItem.getItem());

                    if (!remaining.isEmpty()) {
                        entityItem.setItem(remaining);
                    } else {
                        entityItem.discard();
                        dogIn.playSound(SoundEvents.ITEM_PICKUP, 0.25F, ((dogIn.level.random.nextFloat() - dogIn.level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult processInteract(AbstractDogEntity dogIn, Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        if (dogIn.isTame() && this.level() > 0) { // Dog requirements
            if (playerIn.isShiftKeyDown() && stack.isEmpty()) { // Player requirements

                if (dogIn.canInteract(playerIn)) {

                    if (!playerIn.level.isClientSide) {
                        playerIn.displayClientMessage(Component.translatable("talent.doggytalents.pack_puppy.version_migration"), false);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void set(AbstractDogEntity dog, int preLevel) {
        // No need to drop anything if dog didn't have pack puppy
        if (preLevel > 0 && this.level == 0) {
            this.dropInventory(dog);
        }
    }

    @Override
    public void dropInventory(AbstractDogEntity dogIn) {
        //TODO either drop inventory or save to respawn data, currently does both
        // No need to drop anything if dog didn't have pack puppy
        for (int i = 0; i < this.packPuppyHandler.getSlots(); ++i) {
            Containers.dropItemStack(dogIn.level, dogIn.getX(), dogIn.getY(), dogIn.getZ(), this.packPuppyHandler.getStackInSlot(i));
            this.packPuppyHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void writeToNBT(AbstractDogEntity dogIn, CompoundTag compound) {
        super.writeToNBT(dogIn, compound);
        compound.merge(this.packPuppyHandler.serializeNBT());
    }

    @Override
    public void readFromNBT(AbstractDogEntity dogIn, CompoundTag compound) {
        super.readFromNBT(dogIn, compound);
        this.packPuppyHandler.deserializeNBT(compound);
    }

    // Left in for backwards compatibility for versions <= 2.0.0.5
    @Override
    public void onRead(AbstractDogEntity dogIn, CompoundTag compound) {
        this.packPuppyHandler.deserializeNBT(compound);
    }

    @Override
    public <T> LazyOptional<T> getCapability(AbstractDogEntity dogIn, Capability<T> cap, Direction side) {
        if (cap == PackPuppyTalent.PACK_PUPPY_CAPABILITY) {
            return (LazyOptional<T>) this.lazyPackPuppyHandler;
        }
        return null;
    }

    @Override
    public void invalidateCapabilities(AbstractDogEntity dogIn) {
        this.lazyPackPuppyHandler.invalidate();
    }

    @Override
    public boolean hasRenderer() {
        return ConfigHandler.CLIENT.RENDER_CHEST.get();
    }

    public static boolean hasInventory(AbstractDogEntity dogIn) {
        return dogIn.isAlive() && dogIn.getTalent(DoggyTalents.PACK_PUPPY).isPresent();
    }
}
