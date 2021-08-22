package doggytalents.common.talent;

import org.apache.commons.lang3.tuple.Pair;

import doggytalents.DoggyTalents;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.registry.Talent;
import doggytalents.api.registry.TalentInstance;
import doggytalents.common.inventory.PackPuppyItemHandler;
import doggytalents.common.util.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public class DoggyTorchTalent extends TalentInstance {

    public DoggyTorchTalent(Talent talentIn, int levelIn) {
        super(talentIn, levelIn);
    }

    @Override
    public void tick(AbstractDogEntity dogIn) {
        if (dogIn.tickCount % 10 == 0 && dogIn.isTame()) {

            BlockPos pos = dogIn.blockPosition();
            BlockState torchState = Blocks.TORCH.defaultBlockState();

            if (dogIn.level.getMaxLocalRawBrightness(dogIn.blockPosition()) < 8 && dogIn.level.isEmptyBlock(pos) && torchState.canSurvive(dogIn.level, pos)) {
                PackPuppyItemHandler inventory = dogIn.getTalent(DoggyTalents.PACK_PUPPY)
                    .map((inst) -> inst.cast(PackPuppyTalent.class).inventory()).orElse(null);

                // If null might be because no pack puppy
                if (this.level() >= 5) {
                    dogIn.level.setBlockAndUpdate(pos, torchState);
                }
                else if (inventory != null) { // If null might be because no pack puppy
                    Pair<ItemStack, Integer> foundDetails = InventoryUtil.findStack(inventory, (stack) -> stack.getItem() == Items.TORCH);
                    if (foundDetails != null && !foundDetails.getLeft().isEmpty()) {
                        ItemStack torchStack = foundDetails.getLeft();
                        dogIn.consumeItemFromStack(dogIn, torchStack);
                        inventory.setStackInSlot(foundDetails.getRight(), torchStack);
                        dogIn.level.setBlockAndUpdate(pos, torchState);
                    }
                }
            }
        }
    }
}
