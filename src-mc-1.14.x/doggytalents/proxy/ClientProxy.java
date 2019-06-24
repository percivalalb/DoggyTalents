package doggytalents.proxy;

import java.util.Random;

import doggytalents.DoggyTalentsMod;
import doggytalents.ModBlocks;
import doggytalents.ModContainerTypes;
import doggytalents.ModItems;
import doggytalents.api.inferface.IBedMaterial;
import doggytalents.block.BlockDogBed;
import doggytalents.client.gui.GuiDogInfo;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.client.model.block.IStateParticleModel;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.client.renderer.particle.ParticleCustomLanding;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.GameOverlay;
import doggytalents.handler.InputUpdate;
import doggytalents.tileentity.TileEntityDogBed;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerBlockColours);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerItemColours);
    }
    
    private void clientSetup(FMLClientSetupEvent event) {
        DoggyTalentsMod.LOGGER.debug("ClientProxy clientSetup");
        
        ScreenManager.registerFactory(ModContainerTypes.FOOD_BOWL, GuiFoodBowl::new);
        ScreenManager.registerFactory(ModContainerTypes.PACK_PUPPY, GuiPackPuppy::new);
        ScreenManager.registerFactory(ModContainerTypes.TREAT_BAG, GuiTreatBag::new);
        //ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> GuiConfig.openGui(mc, screen));
        RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, RenderDog::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, RenderDogBeam::new);
    }
    
    private void registerBlockColours(ColorHandlerEvent.Block event) {
        BlockColors blockColors = event.getBlockColors();
        
        blockColors.register((state, world, pos, tintIndex) -> {
            return world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1;
         }, ModBlocks.DOG_BATH);
    }
    
    private void registerItemColours(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        itemColors.register((stack, tintIndex) -> {
            return stack.hasTag() && stack.getTag().contains("collar_colour") ? stack.getTag().getInt("collar_colour") : -1;
          }, ModItems.WOOL_COLLAR);
        
        itemColors.register((stack, tintIndex) -> {
            return stack.hasTag() && stack.getTag().contains("cape_colour") ? stack.getTag().getInt("cape_colour") : -1;
          }, ModItems.CAPE_COLOURED);
        
        itemColors.register((stack, tintIndex) -> {
             return 4159204;
          }, ModBlocks.DOG_BATH);
    }
   
    @Override
    protected void preInit(FMLCommonSetupEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(new GameOverlay());
        MinecraftForge.EVENT_BUS.register(new InputUpdate());
        //DogTextureLoader.loadYourTexures();
    }
    
    @Override
    protected void postInit(InterModProcessEvent event) {
        super.postInit(event);
    }
    
    @Override
    public PlayerEntity getPlayerEntity() {
        return Minecraft.getInstance().player;
    }
    
    @Override
    public void spawnCustomParticle(PlayerEntity player, BlockPos pos, Random rand, float posX, float posY, float posZ, int numberOfParticles, float particleSpeed) {
        TextureAtlasSprite sprite = null;
        BlockState state = player.world.getBlockState((BlockPos)pos);
        IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state);
        if(model instanceof IStateParticleModel) {
            TileEntity tile = player.world.getTileEntity(pos);
            if(tile instanceof TileEntityDogBed) {
                IBedMaterial casing = ((TileEntityDogBed)tile).getCasingId();
                IBedMaterial bedding = ((TileEntityDogBed)tile).getBeddingId();
                sprite = ((IStateParticleModel)model).getParticleTexture(casing, bedding, state.get(BlockDogBed.FACING));
            }
        } 

        if(sprite == null) {
            sprite = model.getParticleTexture();
        }
        
        ParticleManager manager = Minecraft.getInstance().particles;

        for(int i = 0; i < numberOfParticles; i++) {
            double xSpeed = rand.nextGaussian() * particleSpeed;
            double ySpeed = rand.nextGaussian() * particleSpeed;
            double zSpeed = rand.nextGaussian() * particleSpeed;
            
            Particle particle = new ParticleCustomLanding(player.world, posX, posY, posZ, xSpeed, ySpeed, zSpeed, state, pos, sprite);
            manager.addEffect(particle);
        }
    }
    
    @Override
    public void spawnCrit(World world, Entity entity) {
        Minecraft.getInstance().particles.addParticleEmitter(entity, ParticleTypes.CRIT);
    }
    
    @Override
    public void openDoggyInfo(EntityDog dog) {
        Minecraft.getInstance().displayGuiScreen(new GuiDogInfo(dog, this.getPlayerEntity()));
    }
}