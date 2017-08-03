package doggytalents.proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import doggytalents.DoggyTalents;
import doggytalents.ModBlocks;
import doggytalents.ModEntities;
import doggytalents.ModItems;
import doggytalents.entity.EntityDog;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.PlayerConnection;
import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.inventory.ContainerPackPuppy;
import doggytalents.inventory.ContainerTreatBag;
import doggytalents.network.PacketDispatcher;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author ProPercivalalb
 */
public class CommonProxy implements IGuiHandler {

	public void preInit(FMLPreInitializationEvent event) {
		ModBlocks.init();
		ModItems.init();
			
        ModEntities.init();
    }
	
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(DoggyTalents.INSTANCE, DoggyTalents.PROXY);
		PacketDispatcher.registerPackets();
        this.registerEventHandlers();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
    
    protected void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new EntityInteract());
		FMLCommonHandler.instance().bus().register(new PlayerConnection());
    }
	
    public static int RENDER_ID_DOG_BED;
	public static int RENDER_ID_DOG_BATH;
    
	public static final int GUI_ID_DOGGY = 1;
	public static final int GUI_ID_PACKPUPPY = 2;
	public static final int GUI_ID_FOOD_BOWL = 3;
	public static final int GUI_ID_FOOD_BAG = 4;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		if(ID == GUI_ID_DOGGY) {}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.worldObj.getEntityByID(x);
            if(!(target instanceof EntityDog)) 
            	return null;
			EntityDog dog = (EntityDog)target;
			return new ContainerPackPuppy(player, dog);
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			TileEntity target = world.getTileEntity(x, y, z);
			if(!(target instanceof TileEntityFoodBowl))
				return null;
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
			return new ContainerFoodBowl(player.inventory, foodBowl);
		}
		else if(ID == GUI_ID_FOOD_BAG) {
			return new ContainerTreatBag(player, x, player.inventory.getCurrentItem());
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		return null;
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
	
	public EntityPlayer getPlayerEntity() {
		return null;
	}
	
	public void spawnCrit(World world, Entity entity) {}

}
