package doggytalents.core.proxy;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import doggytalents.ModBlocks;
import doggytalents.client.model.ModelDTDoggy;
import doggytalents.client.render.RenderDTDoggy;
import doggytalents.client.render.RenderItemDogBed;
import doggytalents.client.render.RenderWorldDogBed;
import doggytalents.core.handler.DTKeyHandler;
import doggytalents.entity.EntityDTDoggy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	private Minecraft mc = Minecraft.getMinecraft();
	private RenderWorldDogBed renderWorldDogBed = new RenderWorldDogBed();
	private RenderItemDogBed  renderItemDogBed  = new RenderItemDogBed();
	
	@Override
	public void onPreLoad() {
		RenderingRegistry.registerEntityRenderingHandler(EntityDTDoggy.class, new RenderDTDoggy(new ModelDTDoggy(), new ModelDTDoggy(), 0.5F));
		this.RENDER_ID_DOG_BED = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(renderWorldDogBed);
		MinecraftForgeClient.registerItemRenderer(ModBlocks.dogBed.blockID, renderItemDogBed);
	}
	
	@Override
	public void registerHandlers() {
		KeyBindingRegistry.registerKeyBinding(new DTKeyHandler());
		//TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
	}
	
	@Override
	public void spawnCrit(World world, Entity entity) {
		this.mc.effectRenderer.addEffect(new EntityCrit2FX(this.mc.theWorld, entity));
	}
}
