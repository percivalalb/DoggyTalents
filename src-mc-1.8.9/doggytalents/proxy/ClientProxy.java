package doggytalents.proxy;

import doggytalents.DoggyTalents;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.client.model.ModelHelper;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.GameOverlay;
import doggytalents.handler.KeyState;
import doggytalents.handler.ModelBake;
import doggytalents.network.PacketDispatcher;
import doggytalents.talent.WorldRender;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ClientRegistry.registerKeyBinding(KeyState.come);
		ClientRegistry.registerKeyBinding(KeyState.stay);
		ClientRegistry.registerKeyBinding(KeyState.ok);
		ClientRegistry.registerKeyBinding(KeyState.heel);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, RenderDog::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, RenderDogBeam::new);
		
		ModelHelper.setModel(ModItems.THROW_BONE, 0, "doggytalents:throw_bone");
		ModelHelper.setModel(ModItems.THROW_BONE, 1, "doggytalents:throw_bone_wet");
		ModelHelper.setModel(ModItems.COMMAND_EMBLEM, 0, "doggytalents:command_emblem");
		ModelHelper.setModel(ModItems.TRAINING_TREAT, 0, "doggytalents:training_treat");
	    ModelHelper.setModel(ModItems.SUPER_TREAT, 0, "doggytalents:super_treat");
	    ModelHelper.setModel(ModItems.MASTER_TREAT, 0, "doggytalents:master_treat");
	    ModelHelper.setModel(ModItems.DIRE_TREAT, 0, "doggytalents:dire_treat");
	    ModelHelper.setModel(ModItems.BREEDING_BONE, 0, "doggytalents:breeding_bone");
	    ModelHelper.setModel(ModItems.COLLAR_SHEARS, 0, "doggytalents:collar_shears");
	    ModelHelper.setModel(ModItems.DOGGY_CHARM, 0, "doggytalents:doggy_charm");
	    ModelHelper.setModel(ModItems.RADAR, 0, "doggytalents:radar");
	    ModelHelper.setModel(ModItems.RADIO_COLLAR, 0, "doggytalents:radio_collar");
	    ModelHelper.setModel(ModItems.WOOL_COLLAR, 0, "doggytalents:wool_collar");
	    ModelHelper.setModel(ModItems.WHISTLE, 0, "doggytalents:whistle");
	    ModelHelper.setModel(ModItems.CHEW_STICK, 0, "doggytalents:chew_stick");
	    ModelHelper.setModel(ModItems.TREAT_BAG, 0, "doggytalents:treat_bag");
	    
		ModelHelper.setModel(ModBlocks.DOG_BATH, 0, "doggytalents:dog_bath");
		ModelHelper.setModel(ModBlocks.DOG_BED, 0, "doggytalents:dog_bed");
		ModelHelper.setModel(ModBlocks.FOOD_BOWL, 0, "doggytalents:food_bowl");
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
    }
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
	
	@Override
    protected void registerEventHandlers() {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new WorldRender());
		MinecraftForge.EVENT_BUS.register(new GameOverlay());
		MinecraftForge.EVENT_BUS.register(new KeyState());
		MinecraftForge.EVENT_BUS.register(new ModelBake());
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
			return new GuiPackPuppy(player, dog);
		}
		else if(ID == GUI_ID_FOOD_BOWL) {
			TileEntity target = world.getTileEntity(new BlockPos(x, y, z));
			if(!(target instanceof TileEntityFoodBowl))
				return null;
			TileEntityFoodBowl foodBowl = (TileEntityFoodBowl)target;
			return new GuiFoodBowl(player.inventory, foodBowl);
		}
		else if(ID == GUI_ID_FOOD_BAG) {
			return new GuiTreatBag(player, x, player.inventory.getCurrentItem());
		}
		return null;
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
	}
	
	@Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getMinecraft().thePlayer;
	}
	
	@Override
	public IThreadListener getThreadFromContext(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
	}
	
	@Override
	public void spawnCrit(World world, Entity entity) {
		FMLClientHandler.instance().getClient().effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
	}
}
