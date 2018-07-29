package doggytalents.item;

import doggytalents.entity.EntityDog;
import doggytalents.helper.DogLocationManager;
import doggytalents.helper.DogLocationManager.DogLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemDT {
	
	public ItemRadar() {
		super();
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

		if(!worldIn.isRemote) {
			DogLocationManager locationManager = DogLocationManager.getHandler(worldIn);
			
			for(DogLocation location : locationManager.locations) {
				playerIn.sendMessage(new TextComponentString(location.toString()));
			}
			playerIn.sendMessage(new TextComponentString("Size: " + locationManager.locations.size()));
			
			for(Entity entity : worldIn.loadedEntityList) {
				if(entity instanceof EntityDog) {
					EntityDog dog = (EntityDog)entity;
	
					if(dog.hasRadarCollar() && dog.canInteract(playerIn)) {
						StringBuilder builder = new StringBuilder();
						builder.append(dog.getName());
						builder.append(" is ");
						builder.append((int)Math.ceil(dog.getDistance(playerIn)));
						builder.append(" blocks away ");
						if(playerIn.posZ > dog.posZ)
							builder.append("north");
						else
							builder.append("south");
						
						if(playerIn.posX < dog.posX)
							builder.append(", east");
						else
							builder.append(", west");
						if(playerIn.dimension != dog.dimension) {
							builder.append(" and is in the dimension with the id: "+dog.dimension + "("+dog.world.provider.getDimension()+")");
						}else {
							builder.append(" and is in the same dimension");
						}
						playerIn.sendMessage(new TextComponentString(builder.toString()));
					}
				}
			}
		}
		
    	return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }
}
