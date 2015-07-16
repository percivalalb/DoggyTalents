package doggytalents.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.client.model.ModelDog;
import doggytalents.client.renderer.block.RenderBlockDogBath;
import doggytalents.client.renderer.block.RenderBlockDogBed;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.item.RenderItemDogBath;
import doggytalents.client.renderer.item.RenderItemDogBed;
import doggytalents.client.renderer.item.RenderItemRadar;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.KeyStateHandler;
import doggytalents.handler.ScreenRenderHandler;
import doggytalents.talent.BedFinderHandler;

/**
 * @author ProPercivalalb
 */
public class ClientProxy extends CommonProxy {

	private RenderBlockDogBed renderBlockDogBed = new RenderBlockDogBed();
	private RenderBlockDogBath renderBlockDogBath = new RenderBlockDogBath();
	private RenderItemDogBed  renderItemDogBed  = new RenderItemDogBed();
	private RenderItemDogBath  renderItemDogBath  = new RenderItemDogBath();
	private RenderItemRadar  renderItemRadar  = new RenderItemRadar();
	
	@Override
	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, new RenderDog(new ModelDog(), new ModelDog(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, new RenderSnowball(Items.snowball));
		this.RENDER_ID_DOG_BED = RenderingRegistry.getNextAvailableRenderId();
		this.RENDER_ID_DOG_BATH = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderBlockDogBed);
		RenderingRegistry.registerBlockHandler(renderBlockDogBath);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dogBed), renderItemDogBed);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dogBath), renderItemDogBath);
		MinecraftForgeClient.registerItemRenderer(ModItems.radar, renderItemRadar);
		
		MinecraftForge.EVENT_BUS.register(new BedFinderHandler());
		MinecraftForge.EVENT_BUS.register(new ScreenRenderHandler());
		ClientRegistry.registerKeyBinding(KeyStateHandler.come);
		ClientRegistry.registerKeyBinding(KeyStateHandler.stay);
		ClientRegistry.registerKeyBinding(KeyStateHandler.ok);
		ClientRegistry.registerKeyBinding(KeyStateHandler.heel);
		
		FMLCommonHandler.instance().bus().register(new KeyStateHandler());
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
	public void spawnCrit(World world, Entity entity) {
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCrit2FX(world, entity));
	}
}
