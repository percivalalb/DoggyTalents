package doggytalents.common.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import doggytalents.DoggyTalents2;
import doggytalents.api.feature.DataKey;
import doggytalents.api.inferface.AbstractDogEntity;
import doggytalents.api.inferface.IDogItem;
import doggytalents.common.util.NBTUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class PatrolItem extends Item implements IDogItem  {

    public static DataKey<List<BlockPos>> POS = DataKey.make();

    public PatrolItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        this.addPosToStack(playerIn.getHeldItem(handIn), playerIn.getPosition());
        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }

    public void addPosToStack(ItemStack stackIn, BlockPos posIn) {
        CompoundNBT tag = stackIn.getOrCreateTag();
        ListNBT list = tag.getList("patrolPos", Constants.NBT.TAG_COMPOUND);
        CompoundNBT pos = new CompoundNBT();
        NBTUtil.putBlockPos(pos, posIn);
        list.add(pos);
        tag.put("patrolPos", list);
    }

    public List<BlockPos> getPos(ItemStack stackIn) {
        if (stackIn.hasTag() && stackIn.getTag().contains("patrolPos", Constants.NBT.TAG_LIST)) {
            ListNBT list = stackIn.getTag().getList("patrolPos", Constants.NBT.TAG_COMPOUND);
            List<BlockPos> pos = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                pos.add(NBTUtil.getBlockPos(list.getCompound(i)));
            }
            return pos;
        }

        return Collections.emptyList();
    }

    @Override
    public ActionResultType processInteract(AbstractDogEntity dogIn, World worldIn, PlayerEntity playerIn, Hand handIn) {
        List<BlockPos> pos = getPos(playerIn.getHeldItem(handIn));
        DoggyTalents2.LOGGER.debug("{}", pos);
        dogIn.setData(POS, pos);
        return ActionResultType.SUCCESS;
    }
}
