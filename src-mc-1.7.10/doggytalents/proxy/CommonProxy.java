package doggytalents.proxy;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.entity.EntityDog;
import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.inventory.ContainerPackPuppy;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author ProPercivalalb
 */
public class CommonProxy implements IGuiHandler {

	public static int RENDER_ID_DOG_BED;
	public static int RENDER_ID_DOG_BATH;
	
	public static final int GUI_ID_DOGGY = 1;
	public static final int GUI_ID_PACKPUPPY = 2;
	public static final int GUI_ID_FOOD_BOWL = 3;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI_ID_DOGGY) {}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDog)) 
            	return null;
			EntityDog dog = (EntityDog)target;
			return new ContainerPackPuppy(player.inventory, dog);
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			TileEntity target = world.getTileEntity(x, y, z);
			if(!(target instanceof TileEntityFoodBowl))
				return null;
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
			return new ContainerFoodBowl(player.inventory, foodBowl);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		if(ID == GUI_ID_DOGGY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			return new GuiDogInfo(dog, player);
		}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDog)) 
            	return null;
			EntityDog dog = (EntityDog)target;
			return new GuiPackPuppy(player.inventory, dog);
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			TileEntity target = world.getTileEntity(x, y, z);
			if(!(target instanceof TileEntityFoodBowl))
				return null;
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
			return new GuiFoodBowl(player.inventory, foodBowl);
		}
		return null;
	}
	
	public void preInit() {}
	public void init() {}
	public void postInit() {}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
	
	public EntityPlayer getPlayerEntity() {
		return null;
	}
	
	public void spawnCrit(World world, Entity entity) {}

}
