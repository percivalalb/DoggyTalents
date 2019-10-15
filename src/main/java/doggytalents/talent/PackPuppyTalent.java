package doggytalents.talent;

import java.util.List;

import com.google.common.base.Predicate;

import doggytalents.DoggyTalents;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.api.inferface.IDogEntity;
import doggytalents.api.inferface.Talent;
import doggytalents.helper.DogUtil;
import doggytalents.inventory.InventoryPackPuppy;
import doggytalents.lib.GuiNames;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

/**
 * @author ProPercivalalb
 */
public class PackPuppyTalent extends Talent {

    public static Predicate<EntityItem> SHOULD_PICKUP_ENTITY_ITEM = (entity) -> {
        return entity.isEntityAlive() && !DoggyTalentsAPI.PACKPUPPY_BLACKLIST.containsItem(entity.getItem());
    };

    @Override
    public void onClassCreation(IDogEntity dog) {
        dog.putObject("packpuppyinventory", new InventoryPackPuppy(dog));
    }

    @Override
    public void writeAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        InventoryPackPuppy inventory = dog.getObject("packpuppyinventory", InventoryPackPuppy.class);
        inventory.writeToNBT(tagCompound);
    }

    @Override
    public void readAdditional(IDogEntity dog, NBTTagCompound tagCompound) {
        InventoryPackPuppy inventory = dog.getObject("packpuppyinventory", InventoryPackPuppy.class);
        inventory.readFromNBT(tagCompound);
    }

    @Override
    public void onLevelReset(IDogEntity dog, int preLevel) {
        // No need to drop anything if dog didn't have pack puppy
        if(preLevel > 0) {
            InventoryPackPuppy inventory = dog.getObject("packpuppyinventory", InventoryPackPuppy.class);
            InventoryHelper.dropInventoryItems(dog.world, dog, inventory);
            inventory.clear();
        }
    }

    @Override
    public EnumActionResult onInteract(IDogEntity dog, EntityPlayer player, EnumHand hand) {
        if(dog.isTamed()) {
            int level = dog.getTalentFeature().getLevel(this);
            if(level > 0) {
                ItemStack stack = player.getHeldItem(hand);
                if(stack.isEmpty()) {
                    if(player.isSneaking() && !player.world.isRemote && dog.canInteract(player)) {
                        player.openGui(DoggyTalents.INSTANCE, GuiNames.GUI_ID_PACKPUPPY, dog.world, dog.getEntityId(), 0, 0);
                        dog.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, dog.world.rand.nextFloat() * 0.1F + 0.9F);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public void livingTick(IDogEntity dog) {
        if(!dog.world.isRemote && dog.getTalentFeature().getLevel(this) >= 5 && dog.getHealth() > 1) {
            InventoryPackPuppy inventory = dog.getObject("packpuppyinventory", InventoryPackPuppy.class);

            List<EntityItem> list = dog.world.getEntitiesWithinAABB(EntityItem.class, dog.getEntityBoundingBox().grow(2.5D, 1D, 2.5D), SHOULD_PICKUP_ENTITY_ITEM);

            for(EntityItem entityItem : list) {
                ItemStack itemstack1 = DogUtil.addItem(inventory, entityItem.getItem());

                if(!itemstack1.isEmpty())
                    entityItem.setItem(itemstack1);
                else {
                    entityItem.setDead();
                    dog.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.25F, ((dog.world.rand.nextFloat() - dog.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }
            }
        }
    }
}
