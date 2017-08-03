package doggytalents.proxy;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.client.renderer.block.RenderBlockDogBath;
import doggytalents.client.renderer.block.RenderBlockDogBed;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.client.renderer.item.RenderItemDogBath;
import doggytalents.client.renderer.item.RenderItemDogBed;
import doggytalents.client.renderer.item.RenderItemRadar;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.GameOverlay;
import doggytalents.handler.KeyState;
import doggytalents.talent.WorldRender;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	private RenderBlockDogBed 	renderBlockDogBed = new RenderBlockDogBed();
	private RenderBlockDogBath 	renderBlockDogBath = new RenderBlockDogBath();
	private RenderItemDogBed  	renderItemDogBed  = new RenderItemDogBed();
	private RenderItemDogBath  	renderItemDogBath  = new RenderItemDogBath();
	private RenderItemRadar  	renderItemRadar  = new RenderItemRadar();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ClientRegistry.registerKeyBinding(KeyState.come);
		ClientRegistry.registerKeyBinding(KeyState.stay);
		ClientRegistry.registerKeyBinding(KeyState.ok);
		ClientRegistry.registerKeyBinding(KeyState.heel);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, new RenderDog());
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, new RenderDogBeam());
		
		this.RENDER_ID_DOG_BED = RenderingRegistry.getNextAvailableRenderId();
		this.RENDER_ID_DOG_BATH = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(this.renderBlockDogBed);
		RenderingRegistry.registerBlockHandler(this.renderBlockDogBath);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.DOG_BED), this.renderItemDogBed);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.DOG_BATH), this.renderItemDogBath);
		MinecraftForgeClient.registerItemRenderer(ModItems.RADAR, this.renderItemRadar);
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
		FMLCommonHandler.instance().bus().register(new KeyState());
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
			TileEntity target = world.getTileEntity(x, y, z);
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
	public void spawnCrit(World world, Entity entity) {
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCrit2FX(world, entity));
	}
}
