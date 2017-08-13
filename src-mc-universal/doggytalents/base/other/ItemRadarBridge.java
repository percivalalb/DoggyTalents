package doggytalents.base.other;

import doggytalents.base.ObjectLib;
import doggytalents.base.VersionControl.VersionConfig;
import doggytalents.entity.EntityDog;
import doggytalents.item.ItemRadar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
@VersionConfig({"1.9.4", "1.10.2", "1.11.2", "1.12", "1.12.1"})
public class ItemRadarBridge extends ItemRadar {
	
	public ItemRadarBridge() {
		super();
	}
	
	public ActionResult<ItemStack> onItemRightClickGENERAL(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		if(!worldIn.isRemote) {
			for(Entity entity : worldIn.loadedEntityList) {
				if(entity instanceof EntityDog) {
					EntityDog dog = (EntityDog)entity;
	
					if(dog.hasRadarCollar() && dog.canInteract(playerIn)) {
						StringBuilder builder = new StringBuilder();
						builder.append(dog.getName());
						builder.append(" is ");
						builder.append((int)Math.ceil(dog.getDistanceToEntity(playerIn)));
						builder.append(" blocks away ");
						if(playerIn.posZ > dog.posZ)
							builder.append("north");
						else
							builder.append("south");
						
						if(playerIn.posX < dog.posX)
							builder.append(", east");
						else
								builder.append(", west");
						
						ObjectLib.BRIDGE.addMessage(playerIn, builder.toString());
					}
				}
			}
		}
		
    	return new ActionResult(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }
}
