package doggytalents.proxy;

import doggytalents.ModItems;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.GameOverlay;
import doggytalents.handler.KeyState;
import doggytalents.talent.WorldRender;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new IItemColor() {

			@Override
			public int getColorFromItemstack(ItemStack stack, int tintIndex) {
				if(stack.hasTagCompound())
					if(stack.getTagCompound().hasKey("collar_colour"))
						return stack.getTagCompound().getInteger("collar_colour");
				return -1;
			}
			
		}, ModItems.WOOL_COLLAR);
	}
	
	@Override
    protected void registerEventHandlers() {
        super.registerEventHandlers();
        MinecraftForge.EVENT_BUS.register(new WorldRender());
		MinecraftForge.EVENT_BUS.register(new GameOverlay());
		MinecraftForge.EVENT_BUS.register(new KeyState());
    }
	

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { 
		if(ID == GUI_ID_DOGGY) {
			Entity target = player.world.getEntityByID(x);
            if(!(target instanceof EntityDog))
            	return null;
			EntityDog dog = (EntityDog)target;
			return new GuiDogInfo(dog, player);
		}
		else if(ID == GUI_ID_PACKPUPPY) {
			Entity target = player.world.getEntityByID(x);
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
			return new GuiTreatBag(player, x, player.inventory.getStackInSlot(x));
		}
		return null;
	}
	
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
	
	@Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getMinecraft().player;
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
