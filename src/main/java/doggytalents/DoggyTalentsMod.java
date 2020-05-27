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
import doggytalents.data.DTBlockstateProvider;
import doggytalents.data.DTItemModelProvider;
import doggytalents.data.DTItemTagsProvider;
import doggytalents.data.DTLootTableProvider;
import doggytalents.data.DTRecipeProvider;
import doggytalents.handler.EntityInteract;
import doggytalents.handler.EntitySpawn;
import doggytalents.handler.GameOverlay;
import doggytalents.handler.InputUpdate;
import doggytalents.handler.LivingDrops;
import doggytalents.handler.MissingMappings;
import doggytalents.handler.PlayerConnection;
import doggytalents.helper.Compatibility;
import doggytalents.lib.Reference;
import doggytalents.network.PacketHandler;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

        ModBlocks.BLOCKS.register(modEventBus);
        ModTileEntities.TILE_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModContainerTypes.CONTAINERS.register(modEventBus);
        ModSerializers.SERIALIZERS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        modEventBus.addGenericListener(Talent.class, ModTalents::registerTalents); // ModTalents.TALENTS.register(modEventBus);

        modEventBus.addListener(ModRegistries::newRegistry);
        modEventBus.addListener(ModBeddings::registerBeddingMaterial);

        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::interModProcess);

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        forgeEventBus.register(new PlayerConnection());
        forgeEventBus.register(new MissingMappings());
        forgeEventBus.register(new EntityInteract());
        forgeEventBus.register(new LivingDrops());
        forgeEventBus.register(new EntitySpawn());

        // Client Events
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(this::clientSetup);

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

    @OnlyIn(Dist.CLIENT)
    public void clientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(ModContainerTypes.FOOD_BOWL.get(), GuiFoodBowl::new);
        ScreenManager.registerFactory(ModContainerTypes.PACK_PUPPY.get(), GuiPackPuppy::new);
        ScreenManager.registerFactory(ModContainerTypes.TREAT_BAG.get(), GuiTreatBag::new);

        RenderingRegistry.registerEntityRenderingHandler(ModEntities.DOG.get(), RenderDog::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.DOG_BEAM.get(), RenderDogBeam::new);
    }

    protected void interModProcess(InterModProcessEvent event) {
        Compatibility.init();
        FMLJavaModLoadingContext.get().getModEventBus().post(new BeddingRegistryEvent(DogBedRegistry.CASINGS, DogBedRegistry.BEDDINGS));
        AddonManager.runRegisteredAddons();
    }

    private void gatherData(final GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            DTBlockstateProvider blockstates = new DTBlockstateProvider(gen, event.getExistingFileHelper());
            gen.addProvider(blockstates);
            gen.addProvider(new DTItemModelProvider(gen, blockstates.getExistingHelper()));
        }

        if (event.includeServer()) {
            // gen.addProvider(new DTBlockTagsProvider(gen));
            gen.addProvider(new DTItemTagsProvider(gen));
            gen.addProvider(new DTRecipeProvider(gen));
            gen.addProvider(new DTLootTableProvider(gen));
        }
    }
}
