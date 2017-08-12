package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalents;
import doggytalents.ModEntities;
import doggytalents.base.ObjectLib;
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
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ProPercivalalb
 */
public class CommonProxy implements IGuiHandler {

	public void preInit(FMLPreInitializationEvent event) {
		ObjectLib.INITIALIZATION.preInit(event);
			
        ModEntities.init();
    }
	
	public void init(FMLInitializationEvent event) {
		ObjectLib.INITIALIZATION.init(event);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(DoggyTalents.INSTANCE, DoggyTalents.PROXY);
		PacketDispatcher.registerPackets();
        this.registerEventHandlers();
    }

    public void postInit(FMLPostInitializationEvent event) {
    	ObjectLib.INITIALIZATION.postInit(event);
    }
    
    protected void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new EntityInteract());
		MinecraftForge.EVENT_BUS.register(new PlayerConnection());
    }
	
	public static final int GUI_ID_DOGGY = 1;
	public static final int GUI_ID_PACKPUPPY = 2;
	public static final int GUI_ID_FOOD_BOWL = 3;
	public static final int GUI_ID_FOOD_BAG = 4;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		if(ID == GUI_ID_DOGGY) {}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.world.getEntityByID(x);
            if(!(target instanceof EntityDog)) 
            	return null;
			EntityDog dog = (EntityDog)target;
			return new ContainerPackPuppy(player, dog);
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			TileEntity target = ObjectLib.BRIDGE.getTileEntity(world, x, y, z);
			if(!(target instanceof TileEntityFoodBowl))
				return null;
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
			return new ContainerFoodBowl(player.inventory, foodBowl);
		}
		else if(ID == GUI_ID_FOOD_BAG) {
			return new ContainerTreatBag(player, x, player.inventory.getStackInSlot(x));
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		return null;
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}
	
	public EntityPlayer getPlayerEntity() {
		return null;
	}
	
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (IThreadListener)ctx.getServerHandler().player.mcServer;
	}
	
	public void spawnCrit(World world, Entity entity) {}

	public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {}

}
