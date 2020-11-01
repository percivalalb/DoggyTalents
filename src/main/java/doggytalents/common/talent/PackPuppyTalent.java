package doggytalents.common.talent;

import java.util.List;
import java.util.function.Predicate;

import doggytalents.DoggyTags;
import doggytalents.DoggyTalents;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.inventory.PackPuppyItemHandler;
import doggytalents.common.util.InventoryUtil;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;

public class PackPuppyTalent extends TalentInstance {

    @CapabilityInject(PackPuppyItemHandler.class)
    public static Capability<PackPuppyItemHandler> PACK_PUPPY_CAPABILITY = null;

    private PackPuppyItemHandler packPuppyHandler;
    private LazyOptional<?> lazyPackPuppyHandler;

    public static Predicate<ItemEntity> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
        return entity.isAlive() && !entity.cannotPickup() && !entity.getItem().getItem().isIn(DoggyTags.PACK_PUPPY_BLACKLIST);// && !EntityAIFetch.BONE_PREDICATE.test(entity.getItem());
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
        if (dogIn.isAlive() && !dogIn.world.isRemote && this.level() >= 5) {
            List<ItemEntity> list = dogIn.world.getEntitiesWithinAABB(ItemEntity.class, dogIn.getBoundingBox().grow(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);

            if (!list.isEmpty()) {
                for (ItemEntity entityItem : list) {
                    ItemStack remaining = InventoryUtil.addItem(this.packPuppyHandler, entityItem.getItem());

                    if (!remaining.isEmpty()) {
                        entityItem.setItem(remaining);
                    } else {
                        entityItem.remove();
                        dogIn.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dogIn.world.rand.nextFloat() - dogIn.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (dogIn.isTamed() && this.level() > 0) { // Dog requirements
            if (playerIn.isSneaking() && stack.isEmpty()) { // Player requirements

                if (dogIn.canInteract(playerIn)) {

                    if (!playerIn.world.isRemote) {
                        playerIn.sendStatusMessage(new TranslationTextComponent("talent.doggytalents.pack_puppy.version_migration"), false);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }

        return ActionResultType.PASS;
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
            InventoryHelper.spawnItemStack(dogIn.world, dogIn.getPosX(), dogIn.getPosY(), dogIn.getPosZ(), this.packPuppyHandler.getStackInSlot(i));
            this.packPuppyHandler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void writeToNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.writeToNBT(dogIn, compound);
        compound.merge(this.packPuppyHandler.serializeNBT());
    }

    @Override
    public void readFromNBT(AbstractDogEntity dogIn, CompoundNBT compound) {
        super.readFromNBT(dogIn, compound);
        this.packPuppyHandler.deserializeNBT(compound);
    }

    // Left in for backwards compatibility for versions <= 2.0.0.5
    @Override
    public void onRead(AbstractDogEntity dogIn, CompoundNBT compound) {
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
        return ConfigValues.RENDER_CHEST;
    }

    public static boolean hasInventory(AbstractDogEntity dogIn) {
        return dogIn.isAlive() && dogIn.getTalent(DoggyTalents.PACK_PUPPY).isPresent();
    }
}
