package doggytalents.common;

import doggytalents.common.inventory.PackPuppyItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {

    public static void init() {
        CapabilityManager.INSTANCE.register(PackPuppyItemHandler.class, new Capability.IStorage<PackPuppyItemHandler>()  {

            @Override
            public INBT writeNBT(Capability<PackPuppyItemHandler> capability, PackPuppyItemHandler instance, Direction side)  {
                ListNBT nbtTagList = new ListNBT();
                int size = instance.getSlots();
                for (int i = 0; i < size; i++) {
                    ItemStack stack = instance.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        CompoundNBT itemTag = new CompoundNBT();
                        itemTag.putInt("Slot", i);
                        stack.write(itemTag);
                        nbtTagList.add(itemTag);
                    }
                }
                return nbtTagList;
            }

            @Override
            public void readNBT(Capability<PackPuppyItemHandler> capability, PackPuppyItemHandler instance, Direction side, INBT base)  {
                ListNBT tagList = (ListNBT) base;
                for (int i = 0; i < tagList.size(); i++) {
                    CompoundNBT itemTags = tagList.getCompound(i);
                    int j = itemTags.getInt("Slot");

                    if (j >= 0 && j < instance.getSlots())  {
                        instance.setStackInSlot(j, ItemStack.read(itemTags));
                    }
                }
            }
        }, PackPuppyItemHandler::new);
    }
}
