package doggytalents.item;

import doggytalents.inventory.InventoryTreatBag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class ItemTreatBag extends Item {

	public ItemTreatBag(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if(worldIn.isRemote) {
			return new ActionResult<ItemStack>(ActionResultType.PASS, itemstack);
		}
		else {
			int slotId = playerIn.inventory.currentItem;
			INamedContainerProvider bagInventory = new InventoryTreatBag(playerIn.inventory, slotId, itemstack);
			
			if(bagInventory != null) {
                if(playerIn instanceof ServerPlayerEntity && !(playerIn instanceof FakePlayer)) {
                    ServerPlayerEntity entityPlayerMP = (ServerPlayerEntity)playerIn;
                    entityPlayerMP.openContainer(bagInventory);
                }
            }
			
			return new ActionResult<ItemStack>(ActionResultType.PASS, itemstack);
	    }
	}
	
}
