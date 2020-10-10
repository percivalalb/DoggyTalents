package doggytalents.client;

import doggytalents.DoggyAccessories;
import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyTalents;
import doggytalents.DoggyTileEntityTypes;
import doggytalents.client.entity.render.CollarRenderManager;
import doggytalents.client.entity.render.DogRenderer;
import doggytalents.client.entity.render.DoggyBeamRenderer;
import doggytalents.client.entity.render.layer.PackPuppyRenderer;
import doggytalents.client.entity.render.layer.RescueDogRenderer;
import doggytalents.client.entity.render.layer.accessory.ArmorAccessoryRenderer;
import doggytalents.client.entity.render.layer.accessory.DefaultAccessoryRenderer;
import doggytalents.client.entity.render.layer.accessory.DyeableAccessoryRenderer;
import doggytalents.client.entity.render.layer.accessory.LeatherArmorAccessoryRenderer;
import doggytalents.client.screen.DogInventoriesScreen;
import doggytalents.client.screen.FoodBowlScreen;
import doggytalents.client.screen.PackPuppyScreen;
import doggytalents.client.screen.TreatBagScreen;
import doggytalents.client.tileentity.renderer.DogBedRenderer;
import doggytalents.common.lib.Resources;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void setupScreenManagers(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(DoggyContainerTypes.FOOD_BOWL.get(), FoodBowlScreen::new);
        ScreenManager.registerFactory(DoggyContainerTypes.PACK_PUPPY.get(), PackPuppyScreen::new);
        ScreenManager.registerFactory(DoggyContainerTypes.TREAT_BAG.get(), TreatBagScreen::new);
        ScreenManager.registerFactory(DoggyContainerTypes.DOG_INVENTORIES.get(), DogInventoriesScreen::new);
    }

    public static void setupEntityRenderers(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(DoggyEntityTypes.DOG.get(), DogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(DoggyEntityTypes.DOG_BEAM.get(), manager -> new DoggyBeamRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
    }

    public static void setupTileEntityRenderers(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(DoggyTileEntityTypes.DOG_BED.get(), DogBedRenderer::new);
    }

    public static void setupCollarRenderers(final FMLClientSetupEvent event) {
        CollarRenderManager.registerRenderer(DoggyAccessories.DYEABLE_COLLAR.get(), new DyeableAccessoryRenderer(Resources.COLLAR_DEFAULT));
        CollarRenderManager.registerRenderer(DoggyAccessories.GOLDEN_COLLAR.get(), new DefaultAccessoryRenderer(Resources.COLLAR_GOLDEN));
        CollarRenderManager.registerRenderer(DoggyAccessories.SPOTTED_COLLAR.get(), new DefaultAccessoryRenderer(Resources.COLLAR_SPOTTED));
        CollarRenderManager.registerRenderer(DoggyAccessories.MULTICOLORED_COLLAR.get(), new DefaultAccessoryRenderer(Resources.COLLAR_MULTICOLORED));

        CollarRenderManager.registerRenderer(DoggyAccessories.CAPE.get(), new DefaultAccessoryRenderer(Resources.CAPE));
        CollarRenderManager.registerRenderer(DoggyAccessories.DYEABLE_CAPE.get(), new DyeableAccessoryRenderer(Resources.DYEABLE_CAPE));
        CollarRenderManager.registerRenderer(DoggyAccessories.LEATHER_JACKET_CLOTHING.get(), new DefaultAccessoryRenderer(Resources.CLOTHING_LEATHER_JACKET));
        CollarRenderManager.registerRenderer(DoggyAccessories.GUARD_SUIT.get(), new DefaultAccessoryRenderer(Resources.GUARD_SUIT));

        CollarRenderManager.registerRenderer(DoggyAccessories.SUNGLASSES.get(), new DefaultAccessoryRenderer(Resources.GLASSES_SUNGLASSES));

        CollarRenderManager.registerRenderer(DoggyAccessories.RADIO_BAND.get(), new DefaultAccessoryRenderer(Resources.RADIO_BAND));
        CollarRenderManager.registerRenderer(DoggyAccessories.DIAMOND_HELMET.get(), new ArmorAccessoryRenderer(Resources.DIAMOND_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.IRON_HELMET.get(), new ArmorAccessoryRenderer(Resources.IRON_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.GOLDEN_HELMET.get(), new ArmorAccessoryRenderer(Resources.GOLDEN_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.LEATHER_HELMET.get(), new LeatherArmorAccessoryRenderer(Resources.LEATHER_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.CHAINMAIL_HELMET.get(), new ArmorAccessoryRenderer(Resources.CHAINMAIL_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.TURTLE_HELMET.get(), new ArmorAccessoryRenderer(Resources.TURTLE_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.NETHERITE_HELMET.get(), new ArmorAccessoryRenderer(Resources.NETHERITE_HELMET));

        CollarRenderManager.registerRenderer(DoggyTalents.PACK_PUPPY.get(), new PackPuppyRenderer());
        CollarRenderManager.registerRenderer(DoggyTalents.RESCUE_DOG.get(), new RescueDogRenderer());
    }
}
