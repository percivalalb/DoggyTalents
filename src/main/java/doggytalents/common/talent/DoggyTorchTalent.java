package doggytalents.common.talent;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.common.inventory.PackPuppyItemHandler;
import doggytalents.common.util.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class DoggyTorchTalent extends Talent {

    @Override
    public void tick(AbstractDogEntity dogIn) {
        if (dogIn.ticksExisted % 10 == 0 && dogIn.isTamed()) {

            BlockPos pos = dogIn.getPosition();
            BlockState torchState = Blocks.TORCH.getDefaultState();

            if (dogIn.world.getLight(dogIn.getPosition()) < 8 && torchState.isValidPosition(dogIn.world, pos)) {
                int level = dogIn.getLevel(this);
                PackPuppyItemHandler inventory = dogIn.getData(PackPuppyTalent.PACK_PUPPY_HANDLER);

                // If null might be because no pack puppy
                if (inventory != null) {
                    Pair<ItemStack, Integer> foundDetails = InventoryUtil.findStack(inventory, (stack) -> stack.getItem() == Items.TORCH);
                    if (level >= 5) {
                        dogIn.world.setBlockState(pos, torchState);
                    }
                    else {
                        if (foundDetails != null && !foundDetails.getLeft().isEmpty()) {
                            ItemStack torchStack = foundDetails.getLeft();
                            dogIn.consumeItemFromStack(dogIn, torchStack);
                            inventory.setStackInSlot(foundDetails.getRight(), torchStack);
                            dogIn.world.setBlockState(pos, torchState);
                        }
                    }
                }
            }
        }
    }
}
