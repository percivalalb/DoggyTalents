package doggytalents.proxy;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy extends SideProxy {
	
    public ClientProxy() {
    	super();
        DoggyTalentsMod.LOGGER.debug("Client Proxy");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        DoggyTalentsMod.LOGGER.debug("ClientProxy clientSetup");
        
        RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, RenderDog::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, RenderDogBeam::new);
        
        DoggyTalentsMod.LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }
   
    @Override
    protected void postInit(InterModProcessEvent event) {
    	super.postInit(event);
        Minecraft.getInstance().getItemColors().register(new IItemColor() {

			@Override
			public int getColor(ItemStack stack, int tintIndex) {
				if(stack.hasTag() && stack.getTag().hasKey("collar_colour"))
					return stack.getTag().getInt("collar_colour");
				return -1;
			}
			
		}, ModItems.WOOL_COLLAR);
		
		Minecraft.getInstance().getItemColors().register(new IItemColor() {

		@Override
			public int getColor(ItemStack stack, int tintIndex) {
				if(stack.hasTag() && stack.getTag().hasKey("cape_colour"))
					return stack.getTag().getInt("cape_colour");
				return -1;
			}
			
		}, ModItems.CAPE_COLOURED);
    }
    
    @Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getInstance().player;
	}
    
    //TODO @Override
	//public void spawnCrit(World world, Entity entity) {
	//	Minecraft.getInstance().renderManager.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
	//}
}