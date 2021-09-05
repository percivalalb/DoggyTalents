package doggytalents.client;

import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyTileEntityTypes;
import doggytalents.client.entity.model.DogBackpackModel;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.model.DogRescueModel;
import doggytalents.client.entity.render.CollarRenderManager;
import doggytalents.client.entity.render.DogScreenOverlays;
import doggytalents.client.entity.render.DogRenderer;
import doggytalents.client.entity.render.DoggyBeamRenderer;
import doggytalents.client.entity.render.layer.PackPuppyRenderer;
import doggytalents.client.entity.render.layer.RescueDogRenderer;
import doggytalents.client.entity.render.layer.accessory.ArmorAccessoryRenderer;
import doggytalents.client.entity.render.layer.accessory.DefaultAccessoryRenderer;
import doggytalents.client.screen.DogInventoriesScreen;
import doggytalents.client.screen.FoodBowlScreen;
import doggytalents.client.screen.PackPuppyScreen;
import doggytalents.client.screen.TreatBagScreen;
import doggytalents.client.tileentity.renderer.DogBedRenderer;
import doggytalents.common.lib.Constants;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static final ModelLayerLocation DOG = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog"), "main");
    public static final ModelLayerLocation DOG_ARMOR = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog"), "armor");
    public static final ModelLayerLocation DOG_BACKPACK = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog_backpack"), "main");
    public static final ModelLayerLocation DOG_RESCUE_BOX = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog_rescue_box"), "main");
    public static final ModelLayerLocation DOG_BEAM = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog"), "main");

    public static void setupScreenManagers(final FMLClientSetupEvent event) {
        MenuScreens.register(DoggyContainerTypes.FOOD_BOWL.get(), FoodBowlScreen::new);
        MenuScreens.register(DoggyContainerTypes.PACK_PUPPY.get(), PackPuppyScreen::new);
        MenuScreens.register(DoggyContainerTypes.TREAT_BAG.get(), TreatBagScreen::new);
        MenuScreens.register(DoggyContainerTypes.DOG_INVENTORIES.get(), DogInventoriesScreen::new);
    }

    public static void setupEntityRenderers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DOG, DogModel::createBodyLayer);
        event.registerLayerDefinition(DOG_ARMOR, DogModel::createArmorLayer);
        event.registerLayerDefinition(DOG_BACKPACK, DogBackpackModel::createChestLayer);
        event.registerLayerDefinition(DOG_RESCUE_BOX, DogRescueModel::createRescueBoxLayer);
        // TODO: RenderingRegistry.registerEntityRenderingHandler(DoggyEntityTypes.DOG_BEAM.get(), manager -> new DoggyBeamRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
    }

    public static void setupTileEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DoggyEntityTypes.DOG.get(), DogRenderer::new);
        event.registerEntityRenderer(DoggyEntityTypes.DOG_BEAM.get(), DoggyBeamRenderer::new);
        event.registerBlockEntityRenderer(DoggyTileEntityTypes.DOG_BED.get(), DogBedRenderer::new);
    }

    public static void setupCollarRenderers(final FMLClientSetupEvent event) {
        CollarRenderManager.registerLayer(DefaultAccessoryRenderer::new);
        CollarRenderManager.registerLayer(ArmorAccessoryRenderer::new);
        CollarRenderManager.registerLayer(PackPuppyRenderer::new);
        CollarRenderManager.registerLayer(RescueDogRenderer::new);

        OverlayRegistry.registerOverlayTop("Dog Food Level", DogScreenOverlays.FOOD_LEVEL_ELEMENT);
        OverlayRegistry.registerOverlayTop("Dog Air Level", DogScreenOverlays.AIR_LEVEL_ELEMENT);
    }

    public static void addClientReloadListeners(final RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(DogTextureManager.INSTANCE);
    }
}
