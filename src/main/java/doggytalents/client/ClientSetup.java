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
        CollarRenderManager.registerRenderer(DoggyAccessories.DYEABLE_COLLAR, new DyeableAccessoryRenderer(Resources.COLLAR_DEFAULT));
        CollarRenderManager.registerRenderer(DoggyAccessories.GOLDEN_COLLAR, new DefaultAccessoryRenderer(Resources.COLLAR_GOLDEN));
        CollarRenderManager.registerRenderer(DoggyAccessories.SPOTTED_COLLAR, new DefaultAccessoryRenderer(Resources.COLLAR_SPOTTED));
        CollarRenderManager.registerRenderer(DoggyAccessories.MULTICOLORED_COLLAR, new DefaultAccessoryRenderer(Resources.COLLAR_MULTICOLORED));

        CollarRenderManager.registerRenderer(DoggyAccessories.CAPE, new DefaultAccessoryRenderer(Resources.CAPE));
        CollarRenderManager.registerRenderer(DoggyAccessories.DYEABLE_CAPE, new DyeableAccessoryRenderer(Resources.DYEABLE_CAPE));
        CollarRenderManager.registerRenderer(DoggyAccessories.LEATHER_JACKET_CLOTHING, new DefaultAccessoryRenderer(Resources.CLOTHING_LEATHER_JACKET));
        CollarRenderManager.registerRenderer(DoggyAccessories.GUARD_SUIT, new DefaultAccessoryRenderer(Resources.GUARD_SUIT));

        CollarRenderManager.registerRenderer(DoggyAccessories.SUNGLASSES, new DefaultAccessoryRenderer(Resources.GLASSES_SUNGLASSES));

        CollarRenderManager.registerRenderer(DoggyAccessories.RADIO_BAND, new DefaultAccessoryRenderer(Resources.RADIO_BAND));

        CollarRenderManager.registerRenderer(DoggyAccessories.DIAMOND_HELMET, new ArmorAccessoryRenderer(Resources.DIAMOND_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.IRON_HELMET, new ArmorAccessoryRenderer(Resources.IRON_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.GOLDEN_HELMET, new ArmorAccessoryRenderer(Resources.GOLDEN_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.CHAINMAIL_HELMET, new ArmorAccessoryRenderer(Resources.CHAINMAIL_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.TURTLE_HELMET, new ArmorAccessoryRenderer(Resources.TURTLE_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.NETHERITE_HELMET, new ArmorAccessoryRenderer(Resources.NETHERITE_HELMET));

        CollarRenderManager.registerRenderer(DoggyAccessories.IRON_BODY_PIECE, new ArmorAccessoryRenderer(Resources.IRON_BODY_PIECE));
        CollarRenderManager.registerRenderer(DoggyAccessories.DIAMOND_BODY_PIECE, new ArmorAccessoryRenderer(Resources.DIAMOND_BODY_PIECE));
        CollarRenderManager.registerRenderer(DoggyAccessories.GOLDEN_BODY_PIECE, new ArmorAccessoryRenderer(Resources.GOLDEN_BODY_PIECE));
        CollarRenderManager.registerRenderer(DoggyAccessories.CHAINMAIL_BODY_PIECE, new ArmorAccessoryRenderer(Resources.CHAINMAIL_BODY_PIECE));
        CollarRenderManager.registerRenderer(DoggyAccessories.NETHERITE_BODY_PIECE, new ArmorAccessoryRenderer(Resources.NETHERITE_BODY_PIECE));

        CollarRenderManager.registerRenderer(DoggyAccessories.IRON_BOOTS, new ArmorAccessoryRenderer(Resources.IRON_BOOTS));
        CollarRenderManager.registerRenderer(DoggyAccessories.DIAMOND_BOOTS, new ArmorAccessoryRenderer(Resources.DIAMOND_BOOTS));
        CollarRenderManager.registerRenderer(DoggyAccessories.GOLDEN_BOOTS, new ArmorAccessoryRenderer(Resources.GOLDEN_BOOTS));
        CollarRenderManager.registerRenderer(DoggyAccessories.CHAINMAIL_BOOTS, new ArmorAccessoryRenderer(Resources.CHAINMAIL_BOOTS));
        CollarRenderManager.registerRenderer(DoggyAccessories.NETHERITE_BOOTS, new ArmorAccessoryRenderer(Resources.NETHERITE_BOOTS));

        CollarRenderManager.registerRenderer(DoggyAccessories.LEATHER_HELMET, new ArmorAccessoryRenderer(Resources.LEATHER_HELMET));
        CollarRenderManager.registerRenderer(DoggyAccessories.LEATHER_BODY_PIECE, new ArmorAccessoryRenderer(Resources.LEATHER_BODY_PIECE));
        CollarRenderManager.registerRenderer(DoggyAccessories.LEATHER_BOOTS, new ArmorAccessoryRenderer(Resources.LEATHER_BOOTS));

        CollarRenderManager.registerRenderer(DoggyTalents.PACK_PUPPY, new PackPuppyRenderer());
        CollarRenderManager.registerRenderer(DoggyTalents.RESCUE_DOG, new RescueDogRenderer());
    }
}
