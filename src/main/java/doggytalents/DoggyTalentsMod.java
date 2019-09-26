package doggytalents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doggytalents.addon.AddonManager;
import doggytalents.api.BeddingRegistryEvent;
import doggytalents.api.inferface.Talent;
import doggytalents.block.DogBedRegistry;
import doggytalents.client.gui.GuiFoodBowl;
import doggytalents.client.gui.GuiPackPuppy;
import doggytalents.client.gui.GuiTreatBag;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.client.renderer.entity.RenderDogBeam;
import doggytalents.client.renderer.world.WorldRender;
import doggytalents.configuration.ConfigHandler;
import doggytalents.data.DTItemTagsProvider;
import doggytalents.data.DTRecipeProvider;
import doggytalents.entity.EntityDog;
import doggytalents.entity.EntityDoggyBeam;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.EntitySpawn;
import doggytalents.handler.GameOverlay;
import doggytalents.handler.InputUpdate;
import doggytalents.handler.LivingDrops;
import doggytalents.handler.MissingMappings;
import doggytalents.handler.PlayerConnection;
import doggytalents.lib.Reference;
import doggytalents.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DataSerializerEntry;

/**
 * @author ProPercivalalb
 */
@Mod(value = Reference.MOD_ID)
public class DoggyTalentsMod {
    
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);
    
    private static final String PROTOCOL_VERSION = Integer.toString(2);
    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Reference.MOD_ID, "channel"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();
    
    public static DoggyTalentsMod INSTANCE;
    
    public DoggyTalentsMod() {
        INSTANCE = this;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addGenericListener(Item.class, ModItems::registerItems);
        modEventBus.addGenericListener(Block.class, ModBlocks::registerBlocks);
        modEventBus.addGenericListener(Item.class, ModBlocks::registerItemBlocks);
        modEventBus.addGenericListener(EntityType.class, ModEntities::registerEntities);
        modEventBus.addGenericListener(ContainerType.class, ModContainerTypes::registerContainers);
        modEventBus.addGenericListener(DataSerializerEntry.class, ModSerializers::registerSerializers);
        modEventBus.addGenericListener(SoundEvent.class, ModSounds::registerSoundEvents);
        modEventBus.addGenericListener(IRecipeSerializer.class, ModRecipes::registerRecipes);
        modEventBus.addGenericListener(TileEntityType.class, ModTileEntities::registerTileEntities);
        modEventBus.addGenericListener(Talent.class, ModTalents::registerTalents);
        modEventBus.addListener(ModRegistries::newRegistry);
        modEventBus.addListener(ModBeddings::registerBeddingMaterial);
        
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::interModProcess);
        
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(new PlayerConnection());
        forgeEventBus.register(new MissingMappings());
        forgeEventBus.register(new EntityInteract());
        forgeEventBus.register(new LivingDrops());
        forgeEventBus.register(new EntitySpawn());
        
        // Client Events
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ModBlocks::registerBlockColours);
            modEventBus.addListener(ModItems::registerItemColours);
            forgeEventBus.addListener(GameOverlay::onPreRenderGameOverlay);
            forgeEventBus.addListener(InputUpdate::on);
            forgeEventBus.addListener(WorldRender::onWorldRenderLast);
        });

        //DogTextureLoader.loadYourTexures();
        
        ConfigHandler.init();
    }
    
    public void commonSetup(FMLCommonSetupEvent event) {
        PacketHandler.register();

        
        ConfigHandler.initTalentConfig();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.FOOD_BOWL, GuiFoodBowl::new);
        ScreenManager.registerFactory(ModContainerTypes.PACK_PUPPY, GuiPackPuppy::new);
        ScreenManager.registerFactory(ModContainerTypes.TREAT_BAG, GuiTreatBag::new);
        
        RenderingRegistry.registerEntityRenderingHandler(EntityDog.class, RenderDog::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDoggyBeam.class, RenderDogBeam::new);
    }
    
    protected void interModProcess(InterModProcessEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().post(new BeddingRegistryEvent(DogBedRegistry.CASINGS, DogBedRegistry.BEDDINGS));
        AddonManager.runRegisteredAddons();
    }

    public void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeServer()) {
            gen.addProvider(new DTItemTagsProvider(gen));
            gen.addProvider(new DTRecipeProvider(gen));
        }
    }
}
