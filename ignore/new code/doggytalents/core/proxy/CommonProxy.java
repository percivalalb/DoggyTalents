package doggytalents.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import doggytalents.client.gui.GuiDTDoggy;

public class CommonProxy implements IGuiHandler {

	public static final int GUI_ID_MAIN_MENU  = 1;
	public static final int GUI_ID_PACK_PUPPY = 2;
	public static final int GUI_ID_FOOD_BOWL  = 3;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_ID_MAIN_MENU) {}
		else if(ID == GUI_ID_PACK_PUPPY) {
			
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		if(ID == GUI_ID_MAIN_MENU) {
			return new GuiDTDoggy();
		}
		else if(ID == GUI_ID_PACK_PUPPY) {
			
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			
		}
		return null;
	}

	public void registerRenders() {}
}
