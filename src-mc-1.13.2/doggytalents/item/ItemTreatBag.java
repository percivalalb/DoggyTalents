package doggytalents.item;

import doggytalents.inventory.InventoryTreatBag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemTreatBag extends Item {

	public ItemTreatBag(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if(worldIn.isRemote) {
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
		}
		else {
			int slotId = playerIn.inventory.currentItem;
			IInteractionObject bagInventory = new InventoryTreatBag(playerIn, slotId, playerIn.inventory.getStackInSlot(slotId));
			
			if(bagInventory != null) {
                if(playerIn instanceof EntityPlayerMP && !(playerIn instanceof FakePlayer)) {
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP)playerIn;

                    NetworkHooks.openGui(entityPlayerMP, bagInventory, buf -> buf.writeInt(playerIn.inventory.currentItem));
                }
            }
			
			return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
	    }
	}
	
}
