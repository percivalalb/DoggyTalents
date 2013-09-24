package doggytalents.core.proxy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import doggytalents.client.gui.GuiDTDoggy;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.inventory.ContainerPackPuppy;

public class CommonProxy implements IGuiHandler {

	public static final int GUI_ID_DOGGY = 1;
	public static final int GUI_ID_PACKPUPPY = 2;
	public int RENDER_ID_DOG_BED = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_ID_DOGGY) {}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDTDoggy)) {
            	return null;
            }
			EntityDTDoggy dog = (EntityDTDoggy)target;
			ContainerPackPuppy packPuppyContainer = new ContainerPackPuppy(player.inventory, dog);
			return packPuppyContainer;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		if(ID == GUI_ID_DOGGY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDTDoggy)) {
            	return null;
            }
			EntityDTDoggy dog = (EntityDTDoggy)target;
			GuiDTDoggy dogGui = new GuiDTDoggy(dog, dog.getWolfName(), dog.level.getLevel(), player, x);
			return dogGui;
		}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDTDoggy)) {
            	return null;
            }
			EntityDTDoggy dog = (EntityDTDoggy)target;
			GuiPackPuppy packPuppyGui = new GuiPackPuppy(player.inventory, dog);
			return packPuppyGui;
		}
		return null;
	}

	public void registerHandlers() {}

	public void onPreLoad() {
	
	}
	
	/**
	 * Only does something on the client proxy
	 * @param world The world the particles are in
	 * @param entity The entity to spawn the crit particles around
	 */
	public void spawnCrit(World world, Entity entity) {
		
	}
}
