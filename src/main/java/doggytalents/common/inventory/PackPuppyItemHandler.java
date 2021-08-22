package doggytalents.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

public class PackPuppyItemHandler extends ItemStackHandler {

    public PackPuppyItemHandler() {
        super(15);
    }

    @Override
    public CompoundNBT serializeNBT() {
        ListNBT itemsList = new ListNBT();

        for(int i = 0; i < this.stacks.size(); i++) {
           ItemStack stack = this.stacks.get(i);
           if (!stack.isEmpty()) {
              CompoundNBT itemTag = new CompoundNBT();
              itemTag.putByte("Slot", (byte) i);
              stack.save(itemTag);
              itemsList.add(itemTag);
           }
        }

        CompoundNBT compound = new CompoundNBT();
        compound.put("items", itemsList);

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        if (compound.contains("items", Constants.NBT.TAG_LIST)) {
            ListNBT tagList = compound.getList("items", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundNBT itemTag = tagList.getCompound(i);
                int slot = itemTag.getInt("Slot");

                if (slot >= 0 && slot < this.stacks.size()) {
                    this.stacks.set(slot, ItemStack.of(itemTag));
                }
            }
            this.onLoad();
        } else if (compound.contains("packpuppyitems", Constants.NBT.TAG_LIST)) {
            ListNBT tagList = compound.getList("packpuppyitems", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundNBT itemTag = tagList.getCompound(i);
                int slot = itemTag.getInt("Slot");

                if (slot >= 0 && slot < this.stacks.size()) {
                    this.stacks.set(slot, ItemStack.of(itemTag));
                }
            }
            this.onLoad();
        }
    }
}
