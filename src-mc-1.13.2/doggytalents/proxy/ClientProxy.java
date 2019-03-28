package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModItems;
import doggytalents.client.model.block.IStateParticleModel;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.client.renderer.particle.ParticleCustomLanding;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Particles;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientProxy extends CommonProxy {
	
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