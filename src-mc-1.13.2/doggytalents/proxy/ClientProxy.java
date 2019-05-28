package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.ModItems;
import doggytalents.client.gui.GuiHandler;
import doggytalents.client.model.block.IStateParticleModel;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.client.renderer.particle.ParticleCustomLanding;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.DogTextureLoader;
import doggytalents.handler.KeyState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
	
    public ClientProxy() {
    	super();
        DoggyTalentsMod.LOGGER.debug("Client Proxy");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }
    
    private void clientSetup(FMLClientSetupEvent event) {
        DoggyTalentsMod.LOGGER.debug("ClientProxy clientSetup");
        
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
        //ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> GuiConfig.openGui(mc, screen));
        RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, RenderDog::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, RenderDogBeam::new);
		
    	ClientRegistry.registerKeyBinding(KeyState.come);
    	ClientRegistry.registerKeyBinding(KeyState.stay);
    	ClientRegistry.registerKeyBinding(KeyState.ok);
    	ClientRegistry.registerKeyBinding(KeyState.heel);
    }
   
    @Override
    protected void preInit(FMLCommonSetupEvent event) {
    	super.preInit(event);
    }
    
    @Override
    protected void postInit(InterModProcessEvent event) {
    	super.postInit(event);

        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
        	return stack.hasTag() && stack.getTag().contains("collar_colour") ? stack.getTag().getInt("collar_colour") : -1;
          }, ModItems.WOOL_COLLAR);
        
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
        	return stack.hasTag() && stack.getTag().contains("cape_colour") ? stack.getTag().getInt("cape_colour") : -1;
          }, ModItems.CAPE_COLOURED);
        
		Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
	         return 4159204;
	      }, ModBlocks.DOG_BATH);
		
		Minecraft.getInstance().getBlockColors().register((state, blockAccess, pos, tintIndex) -> {
	         return blockAccess != null && pos != null ? BiomeColors.getWaterColor(blockAccess, pos) : -1;
	      }, ModBlocks.DOG_BATH);
    }
    
    @Override
	public EntityPlayer getPlayerEntity() {
		return Minecraft.getInstance().player;
	}
    
    @Override
	public void spawnCustomParticle(EntityPlayer player, Object pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {
		TextureAtlasSprite sprite;
		IBlockState state = player.world.getBlockState((BlockPos)pos);
		IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
		if(model instanceof IStateParticleModel) {
			state = state.getExtendedState(player.world, (BlockPos)pos);
			sprite = ((IStateParticleModel)model).getParticleTexture(state);
		} 
		else
			sprite = model.getParticleTexture();
		
		ParticleManager manager = Minecraft.getInstance().particles;

		for(int i = 0; i < numberOfParticles; i++) {
			double xSpeed = rand.nextGaussian() * particleSpeed;
			double ySpeed = rand.nextGaussian() * particleSpeed;
			double zSpeed = rand.nextGaussian() * particleSpeed;
			
			Particle particle = new ParticleCustomLanding(player.world, posX, posY, posZ, xSpeed, ySpeed, zSpeed, state, (BlockPos)pos, sprite);
			manager.addEffect(particle);
		}
	}
    
    @Override
	public void spawnCrit(World world, Entity entity) {
		Minecraft.getInstance().particles.addParticleEmitter(entity, Particles.CRIT);
	}
}