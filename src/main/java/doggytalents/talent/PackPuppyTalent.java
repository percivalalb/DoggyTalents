package doggytalents.talent;

import java.util.List;
import java.util.function.Predicate;

import doggytalents.ModTags;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.entity.ai.EntityAIFetch;
import doggytalents.helper.CapabilityHelper;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.PackPuppyItemHandler;
import doggytalents.inventory.container.ContainerPackPuppy;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;

/**
 * @author ProPercivalalb
 */
public class PackPuppyTalent extends Talent {

    @CapabilityInject(PackPuppyItemHandler.class)
    public static Capability<PackPuppyItemHandler> PACK_PUPPY_CAPABILITY = null;

    public static Predicate<ItemEntity> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
        return entity.isAlive() && !entity.getItem().getItem().isIn(ModTags.PACK_PUPPY_BLACKLIST) && !EntityAIFetch.BONE_PREDICATE.test(entity.getItem());
    };
    
    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("packpuppyinventory", LazyOptional.of(() -> new PackPuppyItemHandler(dog)));
    }

    @Override
    public void writeAdditional(EntityDog dog, CompoundNBT nbt) {
        PackPuppyItemHandler inventory = CapabilityHelper.getOrThrow(dog, PACK_PUPPY_CAPABILITY);
        nbt.merge(inventory.serializeNBT());
    }
    
    @Override
    public void readAdditional(EntityDog dog, CompoundNBT nbt) {
        PackPuppyItemHandler inventory = CapabilityHelper.getOrThrow(dog, PACK_PUPPY_CAPABILITY);
        inventory.deserializeNBT(nbt);
    }
    
    @Override
    public void onLevelReset(EntityDog dog, int preLevel) {
        // No need to drop anything if dog didn't have pack puppy
        if(preLevel > 0) {
            PackPuppyItemHandler inventory = CapabilityHelper.getOrThrow(dog, PACK_PUPPY_CAPABILITY);
            for(int i = 0; i < inventory.getSlots(); ++i) {
                InventoryHelper.spawnItemStack(dog.world, dog.posX, dog.posY, dog.posZ, inventory.getStackInSlot(i));
                inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }
    
    @Override
    public ActionResultType onInteract(EntityDog dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        int level = dogIn.TALENTS.getLevel(this);
        
        if(dogIn.isTamed() && level > 0) { // Dog requirements
            if(playerIn.isSneaking() && stack.isEmpty()) { // Player requirements
                
                if(!playerIn.world.isRemote && dogIn.canInteract(playerIn)) {

                    if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                        ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)playerIn;
                        NetworkHooks.openGui(entityPlayerMP, new INamedContainerProvider() {
                            @Override
                            public Container createMenu(int windowId, PlayerInventory inventory, PlayerEntity player) {
                                return new ContainerPackPuppy(windowId, inventory, dogIn);
                            }

                            @Override
                            public ITextComponent getDisplayName() {
                                return new TranslationTextComponent("container.doggytalents.pack_puppy");
                            }
                        }, buf -> {
                            buf.writeInt(dogIn.getEntityId());
                        });
                    }

                    dogIn.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, dogIn.world.rand.nextFloat() * 0.1F + 0.9F);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        
        return ActionResultType.PASS;
    }
    
    @Override
    public void livingTick(EntityDog dog) {
        if(!dog.world.isRemote && dog.TALENTS.getLevel(this) >= 5 && dog.getHealth() > 1) {
            IItemHandler inventory = CapabilityHelper.getOrThrow(dog, PACK_PUPPY_CAPABILITY);
            
            List<ItemEntity> list = dog.world.getEntitiesWithinAABB(ItemEntity.class, dog.getBoundingBox().grow(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);
            for(ItemEntity entityItem : list) {
                ItemStack remaining = DogUtil.addItem(inventory, entityItem.getItem());

                if(!remaining.isEmpty())
                    entityItem.setItem(remaining);
                else {
                    entityItem.remove();
                    dog.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dog.world.rand.nextFloat() - dog.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
        }
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(EntityDog dogIn, Capability<T> cap, Direction side) {
        if (cap == PACK_PUPPY_CAPABILITY) {
            return ((LazyOptional<?>)dogIn.objects.get("packpuppyinventory")).cast();
        }
        return null; 
    }

    @Override
    public void invalidateCapabilities(EntityDog dogIn) {
        ((LazyOptional<?>)dogIn.objects.get("packpuppyinventory")).invalidate();
    }
}
