package doggytalents.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
import doggytalents.talent.BedFinderHandler;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 */
public class ClientProxy extends CommonProxy {

	
	@Override
	public void preInit() {
		Minecraft mc = Minecraft.getMinecraft();
	
		//this.RENDER_ID_DOG_BED = RenderingRegistry.getNextAvailableRenderId();
		//this.RENDER_ID_DOG_BATH = RenderingRegistry.getNextAvailableRenderId();
		//RenderingRegistry.registerBlockHandler(renderBlockDogBed);
		//RenderingRegistry.registerBlockHandler(renderBlockDogBath);
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dogBed), renderItemDogBed);
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dogBath), renderItemDogBath);
		//MinecraftForgeClient.registerItemRenderer(ModItems.radar, renderItemRadar);
		
		MinecraftForge.EVENT_BUS.register(new BedFinderHandler());
		MinecraftForge.EVENT_BUS.register(new ClientHandler());
		ClientRegistry.registerKeyBinding(KeyStateHandler.come);
		ClientRegistry.registerKeyBinding(KeyStateHandler.stay);
		ClientRegistry.registerKeyBinding(KeyStateHandler.ok);
		ClientRegistry.registerKeyBinding(KeyStateHandler.heel);
		
		FMLCommonHandler.instance().bus().register(new KeyStateHandler());

    
	}
	
	@Override
	public void init() {
		Minecraft mc = Minecraft.getMinecraft();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, new RenderDog(mc.getRenderManager(), new ModelDog(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, new RenderSnowball(mc.getRenderManager(), Items.snowball, mc.getRenderItem()));

		ModelHelper.registerItem(ModItems.throwBone, 0, "doggytalents:throw_bone");
		ModelHelper.registerItem(ModItems.throwBone, 1, "doggytalents:throw_bone_wet");
		ModelBakery.addVariantName(ModItems.throwBone, new String[] {"doggytalents:throw_bone", "doggytalents:throw_bone_wet"});
		ModelHelper.registerItem(ModItems.commandEmblem, 0, "doggytalents:command_emblem");
		ModelHelper.registerItem(ModItems.trainingTreat, 0, "doggytalents:training_treat");
	    ModelHelper.registerItem(ModItems.superTreat, 0, "doggytalents:super_treat");
	    ModelHelper.registerItem(ModItems.masterTreat, 0, "doggytalents:master_treat");
	    ModelHelper.registerItem(ModItems.direTreat, 0, "doggytalents:dire_treat");
	    ModelHelper.registerItem(ModItems.breedingBone, 0, "doggytalents:breeding_bone");
	    ModelHelper.registerItem(ModItems.collarShears, 0, "doggytalents:collar_shears");
	    ModelHelper.registerItem(ModItems.doggyCharm, 0, "doggytalents:doggy_charm");
	    ModelHelper.registerItem(ModItems.radar, 0, "doggytalents:radar");
	    ModelHelper.registerItem(ModItems.radioCollar, 0, "doggytalents:radio_collar");
	    
		ModelHelper.registerBlock(ModBlocks.dogBath, "doggytalents:dog_bath");
		ModelHelper.registerBlock(ModBlocks.dogBed, "doggytalents:dog_bed");
		ModelHelper.registerBlock(ModBlocks.foodBowl, "doggytalents:food_bowl");
		
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
	public EntityPlayer getClientPlayer() {
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
	
	@Override
	public void spawnCrit(World world, Entity entity) {
		FMLClientHandler.instance().getClient().effectRenderer.func_178926_a(entity, EnumParticleTypes.CRIT);
	}
}
