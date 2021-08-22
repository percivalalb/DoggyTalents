package doggytalents.client.event;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.DoggyBlocks;
import doggytalents.DoggyTalents2;
import doggytalents.client.block.model.DogBedModel;
import doggytalents.client.screen.widget.DogInventoryButton;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.OpenDogScreenData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.block.BlockModelShaper;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.BlockModelRotation;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
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
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModelRegistry();

        try {
            ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(DoggyBlocks.DOG_BED.get());
            ResourceLocation unbakedModelLoc = new ResourceLocation(resourceLocation.getNamespace(), "block/" + resourceLocation.getPath());

            BlockModel model = (BlockModel) event.getModelLoader().getModel(unbakedModelLoc);
            BakedModel customModel = new DogBedModel(event.getModelLoader(), model, model.bake(event.getModelLoader(), model, ModelLoader.defaultTextureGetter(), BlockModelRotation.X180_Y180, unbakedModelLoc, true));

            // Replace all valid block states
            DoggyBlocks.DOG_BED.get().getStateDefinition().getPossibleStates().forEach(state -> {
                modelRegistry.put(BlockModelShaper.stateToModelLocation(state), customModel);
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
        if (event.getMovementInput().jumping) {
            Entity entity = event.getPlayer().getVehicle();
            if (event.getPlayer().isPassenger() && entity instanceof DogEntity) {
                DogEntity dog = (DogEntity) entity;

                if (dog.canJump()) {
                    dog.setJumpPower(100);
                }
            }
        }
    }

    @SubscribeEvent
    public void onScreenInit(final GuiScreenEvent.InitGuiEvent.Post event) {
        Screen screen = event.getGui();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            boolean creative = screen instanceof CreativeModeInventoryScreen;
            Minecraft mc = Minecraft.getInstance();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();
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
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            boolean creative = screen instanceof CreativeModeInventoryScreen;
            DogInventoryButton btn = null;

            //TODO just create a static variable in this class
            for (AbstractWidget widget : screen.buttons) {
                if (widget instanceof DogInventoryButton) {
                    btn = (DogInventoryButton) widget;
                    break;
                }
            }

            if (btn.visible && btn.isHovered()) {
                Minecraft mc = Minecraft.getInstance();
                int width = mc.getWindow().getGuiScaledWidth();
                int height = mc.getWindow().getGuiScaledHeight();
                int sizeX = creative ? 195 : 176;
                int sizeY = creative ? 136 : 166;
                int guiLeft = (width - sizeX) / 2;
                int guiTop = (height - sizeY) / 2;
                if (!creative) {
                    RecipeBookComponent recipeBook = ((InventoryScreen) screen).getRecipeBookComponent();
                    if (recipeBook.isVisible()) {
                        guiLeft += 76;
                    }
                }
                RenderSystem.translated(-guiLeft, -guiTop, 0);
                btn.renderToolTip(event.getMatrixStack(), event.getMouseX(), event.getMouseY());
                RenderSystem.translated(guiLeft, guiTop, 0);
            }
        }
    }

// TODO Implement patrol item
//    @SubscribeEvent
//    public void onWorldRenderLast(RenderWorldLastEvent event) {
//        Minecraft mc = Minecraft.getInstance();
//        PlayerEntity player = mc.player;
//
//        if (player == null || player.getHeldItem(Hand.MAIN_HAND).getItem() != DoggyItems.PATROL.get()) {
//            return;
//        }
//
//        ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
//
//        RenderSystem.pushMatrix();
//
//        if (stack.hasTag() && stack.getTag().contains("patrolPos", Constants.NBT.TAG_LIST)) {
//            ListNBT list = stack.getTag().getList("patrolPos", Constants.NBT.TAG_COMPOUND);
//            List<BlockPos> poses = new ArrayList<>(list.size());
//            for (int i = 0; i < list.size(); i++) {
//                poses.add(NBTUtil.getBlockPos(list.getCompound(i)));
//            }
//
//            for (BlockPos pos : poses) {
//                this.drawSelectionBox(event.getMatrixStack(), player, event.getPartialTicks(), new AxisAlignedBB(pos));
//            }
//        }
//
//
//        RenderSystem.popMatrix();
//    }

    public void drawSelectionBox(PoseStack matrixStackIn, Player player, float particleTicks, AABB boundingBox) {
        RenderSystem.disableAlphaTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box
        RenderSystem.lineWidth(2.0F);


        RenderSystem.disableTexture();
        Vec3 vec3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double d0 = vec3d.x();
        double d1 = vec3d.y();
        double d2 = vec3d.z();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(GL11.GL_LINES, DefaultVertexFormat.POSITION_COLOR);
        LevelRenderer.renderLineBox(matrixStackIn, buf, boundingBox.move(-d0, -d1, -d2), 1F, 1F, 0F, 0.8F);
        Tesselator.getInstance().end();
        RenderSystem.color4f(0.0F, 0.0F, 0.0F, 0.3F);
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
    }

    @SubscribeEvent
    public void onPreRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        label: if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null || !(mc.player.getVehicle() instanceof DogEntity)) {
                break label;
            }

            PoseStack stack = event.getMatrixStack();

            DogEntity dog = (DogEntity) mc.player.getVehicle();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();
            RenderSystem.pushMatrix();
            mc.getTextureManager().bind(Screen.GUI_ICONS_LOCATION);

            RenderSystem.enableBlend();
            int left = width / 2 + 91;
            int top = height - ForgeIngameGui.right_height;
            ForgeIngameGui.right_height += 10;
            int level = Mth.ceil((dog.getDogHunger() / dog.getMaxHunger()) * 20.0D);

            for (int i = 0; i < 10; ++i) {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;
                int icon = 16;
                byte backgound = 12;

                mc.gui.blit(stack, x, y, 16 + backgound * 9, 27, 9, 9);


                if (idx < level) {
                    mc.gui.blit(stack, x, y, icon + 36, 27, 9, 9);
                } else if (idx == level) {
                    mc.gui.blit(stack, x, y, icon + 45, 27, 9, 9);
                }
            }
            RenderSystem.disableBlend();

            RenderSystem.enableBlend();
            left = width / 2 + 91;
            top = height - ForgeIngameGui.right_height;
            RenderSystem.color4f(1.0F, 1.0F, 0.0F, 1.0F);
            int l6 = dog.getAirSupply();
            int j7 = dog.getMaxAirSupply();

            if (dog.isEyeInFluid(FluidTags.WATER) || l6 < j7) {
                int air = dog.getAirSupply();
                int full = Mth.ceil((air - 2) * 10.0D / 300.0D);
                int partial = Mth.ceil(air * 10.0D / 300.0D) - full;

                for (int i = 0; i < full + partial; ++i) {
                    mc.gui.blit(stack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                }
                ForgeIngameGui.right_height += 10;
            }
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();

            RenderSystem.popMatrix();
        }
    }
}
