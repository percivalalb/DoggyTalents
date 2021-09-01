package doggytalents.client;

import com.google.common.eventbus.Subscribe;
import doggytalents.DoggyAccessories;
import doggytalents.DoggyContainerTypes;
import doggytalents.DoggyEntityTypes;
import doggytalents.DoggyTalents;
import doggytalents.DoggyTileEntityTypes;
import doggytalents.client.entity.model.DogBackpackModel;
import doggytalents.client.entity.model.DogModel;
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
import doggytalents.common.lib.Constants;
import doggytalents.common.lib.Resources;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static final ModelLayerLocation DOG = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog"), "main");
    public static final ModelLayerLocation DOG_BACKPACK = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog_backpack"), "main");
    public static final ModelLayerLocation DOG_BEAM = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "dog"), "main");

    public static void setupScreenManagers(final FMLClientSetupEvent event) {
        MenuScreens.register(DoggyContainerTypes.FOOD_BOWL.get(), FoodBowlScreen::new);
        MenuScreens.register(DoggyContainerTypes.PACK_PUPPY.get(), PackPuppyScreen::new);
        MenuScreens.register(DoggyContainerTypes.TREAT_BAG.get(), TreatBagScreen::new);
        MenuScreens.register(DoggyContainerTypes.DOG_INVENTORIES.get(), DogInventoriesScreen::new);
    }

    public static void setupEntityRenderers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DOG, DogModel::createBodyLayer);
        event.registerLayerDefinition(DOG_BACKPACK, DogBackpackModel::createChestLayer);
        // TODO: RenderingRegistry.registerEntityRenderingHandler(DoggyEntityTypes.DOG_BEAM.get(), manager -> new DoggyBeamRenderer<>(manager, event.getMinecraftSupplier().get().getItemRenderer()));
    }

    public static void setupTileEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DoggyEntityTypes.DOG.get(), DogRenderer::new);
        event.registerEntityRenderer(DoggyEntityTypes.DOG_BEAM.get(), DoggyBeamRenderer::new);
        event.registerBlockEntityRenderer(DoggyTileEntityTypes.DOG_BED.get(), DogBedRenderer::new);
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

    public static void addReloadListeners(final AddReloadListenerEvent event) {
        event.addListener(DogTextureManager.INSTANCE);
    }
}
