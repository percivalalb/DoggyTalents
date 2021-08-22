package doggytalents.common;

import doggytalents.common.inventory.PackPuppyItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {

    public static void init() {
        CapabilityManager.INSTANCE.register(PackPuppyItemHandler.class, new Capability.IStorage<PackPuppyItemHandler>()  {

            @Override
            public Tag writeNBT(Capability<PackPuppyItemHandler> capability, PackPuppyItemHandler instance, Direction side)  {
                ListTag nbtTagList = new ListTag();
                int size = instance.getSlots();
                for (int i = 0; i < size; i++) {
                    ItemStack stack = instance.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        CompoundTag itemTag = new CompoundTag();
                        itemTag.putInt("Slot", i);
                        stack.save(itemTag);
                        nbtTagList.add(itemTag);
                    }
                }
                return nbtTagList;
            }

            @Override
            public void readNBT(Capability<PackPuppyItemHandler> capability, PackPuppyItemHandler instance, Direction side, Tag base)  {
                ListTag tagList = (ListTag) base;
                for (int i = 0; i < tagList.size(); i++) {
                    CompoundTag itemTags = tagList.getCompound(i);
                    int j = itemTags.getInt("Slot");

                    if (j >= 0 && j < instance.getSlots())  {
                        instance.setStackInSlot(j, ItemStack.of(itemTags));
                    }
                }
            }
        }, PackPuppyItemHandler::new);
    }
}
