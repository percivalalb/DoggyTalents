package doggytalents.client.event;

import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyTalents2;
import doggytalents.client.block.model.DogBedModel;
import doggytalents.client.screen.widget.DogInventoryButton;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.OpenDogScreenData;
import doggytalents.common.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.model.ModelRotation;
import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEventHandler {

    public static void onModelBakeEvent(final ModelBakeEvent event) {
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
    @SubscribeEvent
    public void onScreenInit(final GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (screen instanceof InventoryScreen || screen instanceof CreativeScreen) {
            boolean creative = screen instanceof CreativeScreen;
            Minecraft mc = Minecraft.getInstance();
            int width = mc.getMainWindow().getScaledWidth();
            int height = mc.getMainWindow().getScaledHeight();
            int sizeX = creative ? 195 : 176;
            int sizeY = creative ? 136 : 166;
            int guiLeft = (width - sizeX) / 2;
            int guiTop = (height - sizeY) / 2;

            int x = guiLeft + (creative ? 36 : sizeX / 2 - 10);
            int y = guiTop + (creative ? 7 : 48);

            event.addWidget(new DogInventoryButton(x, y, screen, (btn) -> {
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new OpenDogScreenData());
                btn.active = false;
            }));
        }
    }

    @SubscribeEvent
    public void onScreenDrawForeground(final GuiContainerEvent.DrawForeground event) {
        Screen screen = event.getGuiContainer();
        if (screen instanceof InventoryScreen || screen instanceof CreativeScreen) {
            boolean creative = screen instanceof CreativeScreen;
            DogInventoryButton btn = null;

            //TODO just create a static variable in this class
            for (Widget widget : screen.buttons) {
                if (widget instanceof DogInventoryButton) {
                    btn = (DogInventoryButton) widget;
                    break;
                }
            }

            if (btn.visible && Util.isPointInRegion(btn.x, btn.y, btn.getWidth(), btn.getHeight(), event.getMouseX(), event.getMouseY())) {
                Minecraft mc = Minecraft.getInstance();
                int width = mc.getMainWindow().getScaledWidth();
                int height = mc.getMainWindow().getScaledHeight();
                int sizeX = creative ? 195 : 176;
                int sizeY = creative ? 136 : 166;
                int guiLeft = (width - sizeX) / 2;
                int guiTop = (height - sizeY) / 2;
                if (!creative) {
                    RecipeBookGui recipeBook = ((InventoryScreen) screen).getRecipeGui();
                    if (recipeBook.isVisible()) {
                        guiLeft += 76;
                    }
                }
                RenderSystem.translated(-guiLeft, -guiTop, 0);
                btn.renderToolTip(event.getMouseX(), event.getMouseY());
                RenderSystem.translated(guiLeft, guiTop, 0);
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
