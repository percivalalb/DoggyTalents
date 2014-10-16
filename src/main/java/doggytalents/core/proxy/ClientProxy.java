package doggytalents.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.client.model.ModelDTDoggy;
import doggytalents.client.render.RenderDTDoggy;
import doggytalents.client.render.RenderItemDogBed;
import doggytalents.client.render.RenderItemRadar;
import doggytalents.client.render.RenderWorldDogBed;
import doggytalents.core.handler.KeyStateHandler;
import doggytalents.entity.EntityDTDoggy;

/**
 * @author ProPercivalalb
 */
public class ClientProxy extends CommonProxy {

	private Minecraft mc = Minecraft.getMinecraft();
	private RenderWorldDogBed renderWorldDogBed = new RenderWorldDogBed();
	private RenderItemDogBed  renderItemDogBed  = new RenderItemDogBed();
	private RenderItemRadar   renderItemRadar   = new RenderItemRadar();
	
	@Override
	public void onPreLoad() {
		RenderingRegistry.registerEntityRenderingHandler(EntityDTDoggy.class, new RenderDTDoggy(new ModelDTDoggy(), new ModelDTDoggy(), 0.5F));
		this.RENDER_ID_DOG_BED = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderWorldDogBed);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.dogBed), renderItemDogBed);
		MinecraftForgeClient.registerItemRenderer(ModItems.radar, renderItemRadar);
	}
	
	@Override
	public void registerHandlers() {
		ClientRegistry.registerKeyBinding(KeyStateHandler.come);
		ClientRegistry.registerKeyBinding(KeyStateHandler.stay);
		ClientRegistry.registerKeyBinding(KeyStateHandler.ok);
		ClientRegistry.registerKeyBinding(KeyStateHandler.heel);
		
		FMLCommonHandler.instance().bus().register(new KeyStateHandler());
	}
	
	@Override
	public EntityPlayer getClientPlayer() {
		return FMLClientHandler.instance().getClientPlayerEntity();
	}
	
	@Override
	public void spawnCrit(World world, Entity entity) {
		this.mc.effectRenderer.addEffect(new EntityCrit2FX(this.mc.theWorld, entity));
	}
}
