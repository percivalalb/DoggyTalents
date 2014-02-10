package doggytalents.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import doggytalents.client.gui.GuiDTDoggy;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class CommonProxy implements IGuiHandler {

	public void onModPre() {
    	
    }
	
	public void onModLoad() {
    	
    }
	
	public void onModPost() {
    	
    }

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 2) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDTDoggy)) {
            	return null;
            }
			EntityDTDoggy dog = (EntityDTDoggy)target;
			ContainerPackPuppy slimeGui = new ContainerPackPuppy(player.inventory, dog);
			return slimeGui;
		}
		if(ID == 3) {
			ContainerFoodBowl bowl = new ContainerFoodBowl(player.inventory, (TileEntityFoodBowl)world.getBlockTileEntity(x, y, z));
			return bowl;
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 1) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDTDoggy)) {
            	return null;
            }
			EntityDTDoggy dog = (EntityDTDoggy)target;
			GuiDTDoggy slimeGui = new GuiDTDoggy(dog, dog.getWolfName(), dog.level.getLevel(), player, x);
			return slimeGui;
		}
		else if(ID == 2) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDTDoggy)) {
            	return null;
            }
			EntityDTDoggy dog = (EntityDTDoggy)target;
			GuiPackPuppy slimeGui = new GuiPackPuppy(player.inventory, dog);
			return slimeGui;
		}
		if(ID == 3) {
			GuiFoodBowl bowl = new GuiFoodBowl(player.inventory, (TileEntityFoodBowl)world.getBlockTileEntity(x, y, z));
			return bowl;
		}
		
		return null;
	}
}
