package doggytalents.talent;

import java.util.List;
import java.util.function.Predicate;

import doggytalents.ModTags;
import doggytalents.api.inferface.Talent;
import doggytalents.entity.EntityDog;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * @author ProPercivalalb
 */
public class PackPuppyTalent extends Talent {

    public static Predicate<ItemEntity> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
        return entity.isAlive() && !entity.cannotPickup() && !entity.getItem().getItem().isIn(ModTags.getTag(ModTags.PACK_PUPPY_BLACKLIST));
    };
    
    @Override
    public void onClassCreation(EntityDog dog) {
        dog.objects.put("packpuppyinventory", new InventoryPackPuppy(dog));
    }
    
    @Override
    public void writeAdditional(EntityDog dog, CompoundNBT tagCompound) {
        InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
        inventory.writeToNBT(tagCompound);
    }
    
    @Override
    public void readAdditional(EntityDog dog, CompoundNBT tagCompound) {
        InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
        inventory.readFromNBT(tagCompound);
    }
    
    @Override
    public void onLevelReset(EntityDog dog, int preLevel) {
        // No need to drop anything if dog didn't have pack puppy
        if(preLevel > 0) {
            InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
            InventoryHelper.dropInventoryItems(dog.world, dog, inventory);
            inventory.clear();
        }
    }
    
    @Override
    public ActionResultType onInteract(EntityDog dogIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        int level = dogIn.TALENTS.getLevel(this);
        
        if(dogIn.isTamed() && level > 0) { // Dog requirements
            if(playerIn.isSneaking() && stack.isEmpty()) { // Player requirements
                
                if(!playerIn.world.isRemote && dogIn.canInteract(playerIn)) {
                    INamedContainerProvider inventory = (INamedContainerProvider)dogIn.objects.get("packpuppyinventory");
                    
                    if(inventory != null) {
                        if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                            ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)playerIn;
                            NetworkHooks.openGui(entityPlayerMP, inventory, buf -> buf.writeInt(dogIn.getEntityId()));
                        }
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
            InventoryPackPuppy inventory = (InventoryPackPuppy)dog.objects.get("packpuppyinventory");
            
            List<ItemEntity> list = dog.world.getEntitiesWithinAABB(ItemEntity.class, dog.getBoundingBox().grow(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);
            for(ItemEntity entityItem : list) {
                ItemStack itemstack1 = DogUtil.addItem(inventory, entityItem.getItem());

                if(!itemstack1.isEmpty())
                    entityItem.setItem(itemstack1);
                else {
                    entityItem.remove();
                    dog.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dog.world.rand.nextFloat() - dog.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
        }
    }
}
