package doggytalents.core.proxy;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import doggytalents.client.gui.GuiDTDoggy;
import doggytalents.entity.EntityDTDoggy;

public class CommonProxy implements IGuiHandler {

	public static final int GUI_ID_DOGGY = 1;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_ID_DOGGY) {}
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
		return null;
	}

	public void registerHandlers() {}

	public void onPreLoad() {
	
	}
	
	public void spawnCrit(World world, Entity entity) {
		
	}
}
