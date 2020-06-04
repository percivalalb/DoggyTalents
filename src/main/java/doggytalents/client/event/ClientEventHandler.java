package doggytalents.client.event;

import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyTalents2;
import doggytalents.client.block.model.DogBedModel;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEventHandler {

    @SubscribeEvent
    public void onModelBakeEvent(final ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> modelRegistry = event.getModelRegistry();

        try {
            ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(DoggyBlocks.DOG_BED.get());
            ResourceLocation unbakedModelLoc = new ResourceLocation(resourceLocation.getNamespace(), "block/" + resourceLocation.getPath());

            BlockModel model = (BlockModel) event.getModelLoader().getUnbakedModel(unbakedModelLoc);
            IBakedModel customModel = new DogBedModel(event.getModelLoader(), model, model.bakeModel(event.getModelLoader(), model, ModelLoader.defaultTextureGetter(), ModelRotation.X180_Y180, unbakedModelLoc, true));

            // Replace all valid block states
            DoggyBlocks.DOG_BED.get().getStateContainer().getValidStates().forEach(state -> {
                modelRegistry.put(BlockModelShapes.getModelLocation(state), customModel);
            });

            // Replace inventory model
            modelRegistry.put(new ModelResourceLocation(resourceLocation, "inventory"), customModel);

        }
        catch(Exception e) {
            DoggyTalents2.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onInputEvent(final InputUpdateEvent event) {
        if(event.getMovementInput().jump) {
            Entity entity = event.getPlayer().getRidingEntity();
            if(event.getPlayer().isPassenger() && entity instanceof DogEntity) {
                DogEntity dog = (DogEntity)entity;
//TODO
//                if(dog.canJump()) {
//                    dog.setJumpPower(100);
//                }
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        label:
        if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null || !(mc.player.getRidingEntity() instanceof DogEntity)) {
                break label;
            }

            DogEntity dog = (DogEntity) mc.player.getRidingEntity();
            int width = Minecraft.getInstance().getMainWindow().getScaledWidth();
            int height = Minecraft.getInstance().getMainWindow().getScaledHeight();
            RenderSystem.pushMatrix();
            mc.getTextureManager().bindTexture(Screen.GUI_ICONS_LOCATION);

            RenderSystem.enableBlend();
            int left = width / 2 + 91;
            int top = height - ForgeIngameGui.right_height;
            ForgeIngameGui.right_height += 10;
            int level = MathHelper.ceil(((double)dog.getDogHunger() / (double) dog.getMaxHunger()) * 20.0D);

            for (int i = 0; i < 10; ++i) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte backgound = 12;

                mc.ingameGUI.blit(x, y, 16 + backgound * 9, 27, 9, 9);


                if (idx < level) {
                    mc.ingameGUI.blit(x, y, icon + 36, 27, 9, 9);
                } else if (idx == level) {
                    mc.ingameGUI.blit(x, y, icon + 45, 27, 9, 9);
                }
            }
            RenderSystem.disableBlend();

            RenderSystem.enableBlend();
            left = width / 2 + 91;
            top = height - ForgeIngameGui.right_height;
            RenderSystem.color4f(1.0F, 1.0F, 0.0F, 1.0F);
            int l6 = dog.getAir();
            int j7 = dog.getMaxAir();

            if(dog.areEyesInFluid(FluidTags.WATER) || l6 < j7) {
                int air = dog.getAir();
                int full = MathHelper.ceil((air - 2) * 10.0D / 300.0D);
                int partial = MathHelper.ceil(air * 10.0D / 300.0D) - full;

                for (int i = 0; i < full + partial; ++i) {
                    mc.ingameGUI.blit(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                }
                ForgeIngameGui.right_height += 10;
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();

            RenderSystem.popMatrix();
        }
    }
}
