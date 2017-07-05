package doggytalents.proxy;

import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.model.ModelDog;
import doggytalents.client.model.ModelHelper;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.ClientHandler;
import doggytalents.handler.KeyStateHandler;
import doggytalents.handler.RenderHandHandler;
import doggytalents.handler.ScreenRenderHandler;
import doggytalents.lib.Reference;
import doggytalents.talent.BedFinderHandler;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ProPercivalalb
 */
public class ClientProxy extends CommonProxy {

	
	@Override
	public void preInit() {
		Minecraft mc = Minecraft.getMinecraft();
		
		MinecraftForge.EVENT_BUS.register(new BedFinderHandler());
		MinecraftForge.EVENT_BUS.register(new ClientHandler());
		MinecraftForge.EVENT_BUS.register(new ScreenRenderHandler());
		MinecraftForge.EVENT_BUS.register(new RenderHandHandler());
		ClientRegistry.registerKeyBinding(KeyStateHandler.come);
		ClientRegistry.registerKeyBinding(KeyStateHandler.stay);
		ClientRegistry.registerKeyBinding(KeyStateHandler.ok);
		ClientRegistry.registerKeyBinding(KeyStateHandler.heel);
		
		MinecraftForge.EVENT_BUS.register(new KeyStateHandler());
	}
	
	@Override
	public void init() {
		Minecraft mc = Minecraft.getMinecraft();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, new RenderDog(mc.getRenderManager(), new ModelDog(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, new RenderSnowball(mc.getRenderManager(), Items.SNOWBALL, mc.getRenderItem()));

		ModelHelper.registerItem(ModItems.THROW_BONE, 0, "doggytalents:throw_bone");
		ModelHelper.registerItem(ModItems.THROW_BONE, 1, "doggytalents:throw_bone_wet");
		ModelBakery.registerItemVariants(ModItems.THROW_BONE, new ResourceLocation(Reference.MOD_ID, "throw_bone"), new ResourceLocation(Reference.MOD_ID, "throw_bone_wet"));
		ModelHelper.registerItem(ModItems.COMMAND_EMBLEM, 0, "doggytalents:command_emblem");
		ModelHelper.registerItem(ModItems.TRAINING_TREAT, 0, "doggytalents:training_treat");
	    ModelHelper.registerItem(ModItems.SUPER_TREAT, 0, "doggytalents:super_treat");
	    ModelHelper.registerItem(ModItems.MASTER_TREAT, 0, "doggytalents:master_treat");
	    ModelHelper.registerItem(ModItems.DIRE_TREAT, 0, "doggytalents:dire_treat");
	    ModelHelper.registerItem(ModItems.BREEDING_BONE, 0, "doggytalents:breeding_bone");
	    ModelHelper.registerItem(ModItems.COLLAR_SHEARS, 0, "doggytalents:collar_shears");
	    ModelHelper.registerItem(ModItems.DOGGY_CHARM, 0, "doggytalents:doggy_charm");
	    ModelHelper.registerItem(ModItems.RADAR, 0, "doggytalents:radar");
	    ModelHelper.registerItem(ModItems.RADIO_COLLAR, 0, "doggytalents:radio_collar");
	    
		ModelHelper.registerBlock(ModBlocks.DOG_BATH, "doggytalents:dog_bath");
		ModelHelper.registerBlock(ModBlocks.DOG_BED, "doggytalents:dog_bed");
		ModelHelper.registerBlock(ModBlocks.FOOD_BOWL, "doggytalents:food_bowl");
		
		//ModelHelper.getBlockModelShapes().registerBuiltInBlocks(ModBlocks.foodBowl);
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
