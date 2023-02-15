package doggytalents.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import doggytalents.DoggyBlocks;
import doggytalents.DoggyTalents2;
import doggytalents.client.block.model.DogBedModel;
import doggytalents.client.screen.widget.DogInventoryButton;
import doggytalents.common.entity.DogEntity;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.OpenDogScreenData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ClientEventHandler {

    public static void onRegisterAdditionalModel(final ModelEvent.RegisterAdditional event) {
        try {
            ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(DoggyBlocks.DOG_BED.get());
            ResourceLocation unbakedModelLoc = new ResourceLocation(resourceLocation.getNamespace(), "block/" + resourceLocation.getPath());
            event.register(unbakedModelLoc);
        }
        catch(Exception e) {
            DoggyTalents2.LOGGER.warn("Could not get base Dog Bed model. Reverting to default textures...");
            e.printStackTrace();
        }
    }

    public static void onModelBakeEvent(final ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();

        try {
            ResourceLocation resourceLocation = ForgeRegistries.BLOCKS.getKey(DoggyBlocks.DOG_BED.get());
            ResourceLocation modelLoc = new ResourceLocation(resourceLocation.getNamespace(), "block/" + resourceLocation.getPath());

            BakedModel model = modelRegistry.get(modelLoc);

            BlockModel modelUnbaked = (BlockModel) event.getModelBakery().getModel(modelLoc);

            BakedModel customModel = new DogBedModel(event.getModelBakery(), modelUnbaked, model);

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
    public void onInputEvent(final MovementInputUpdateEvent event) {
        if (event.getInput().jumping) {
            Entity entity = event.getEntity().getVehicle();
            if (event.getEntity().isPassenger() && entity instanceof DogEntity) {
                DogEntity dog = (DogEntity) entity;

                if (dog.canJump()) {
                    dog.setJumpPower(100);
                }
            }
        }
    }

    @SubscribeEvent
    public void onScreenInit(final ScreenEvent.Init.Post event) {
        if (!ConfigHandler.CLIENT.DOG_INV_BUTTON_IN_INV.get())
            return;
        Screen screen = event.getScreen();
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

            event.addListener(new DogInventoryButton(x, y, screen, (btn) -> {
                PacketHandler.send(PacketDistributor.SERVER.noArg(), new OpenDogScreenData());
                btn.active = false;
            }));
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
//        if (stack.hasTag() && stack.getTag().contains("patrolPos", Tag.TAG_LIST)) {
//            ListNBT list = stack.getTag().getList("patrolPos", Tag.TAG_COMPOUND);
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
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // RenderSystem.disableAlphaTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box
        RenderSystem.lineWidth(2.0F);


        RenderSystem.disableTexture();
        Vec3 vec3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double d0 = vec3d.x();
        double d1 = vec3d.y();
        double d2 = vec3d.z();

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        LevelRenderer.renderLineBox(matrixStackIn, bufferbuilder, boundingBox.move(-d0, -d1, -d2), 1F, 1F, 0F, 0.8F);
        Tesselator.getInstance().end();
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.3F);
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        //RenderSystem.enableAlphaTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

//    @SubscribeEvent
//    public void onPreRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
//        // TODO
//        label: if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTHMOUNT) {
//            Minecraft mc = Minecraft.getInstance();
//
//            if (mc.player == null || !(mc.player.getVehicle() instanceof DogEntity)) {
//                break label;
//            }
//
//            PoseStack stack = event.getMatrixStack();
//
//            DogEntity dog = (DogEntity) mc.player.getVehicle();
//            int width = mc.getWindow().getGuiScaledWidth();
//            int height = mc.getWindow().getGuiScaledHeight();
//            RenderSystem.pushMatrix();
//            RenderSystem.setShader(GameRenderer::getPositionTexShader);
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            RenderSystem.setShaderTexture(0, Screen.GUI_ICONS_LOCATION;
//            RenderSystem.enableBlend();
//            int left = width / 2 + 91;
//            int top = height - ((ForgeIngameGui) mc.gui).right_height;
//            ((ForgeIngameGui) mc.gui).right_height += 10;
//            int level = Mth.ceil((dog.getDogHunger() / dog.getMaxHunger()) * 20.0D);
//
//            for (int i = 0; i < 10; ++i) {
//                int idx = i * 2 + 1;
//                int x = left - i * 8 - 9;
//                int y = top;
//                int icon = 16;
//                byte backgound = 12;
//
//                mc.gui.blit(stack, x, y, 16 + backgound * 9, 27, 9, 9);
//
//
//                if (idx < level) {
//                    mc.gui.blit(stack, x, y, icon + 36, 27, 9, 9);
//                } else if (idx == level) {
//                    mc.gui.blit(stack, x, y, icon + 45, 27, 9, 9);
//                }
//            }
//            RenderSystem.disableBlend();
//
//            RenderSystem.enableBlend();
//            left = width / 2 + 91;
//            top = height - ForgeIngameGui.right_height;
//            RenderSystem.color4f(1.0F, 1.0F, 0.0F, 1.0F);
//            int l6 = dog.getAirSupply();
//            int j7 = dog.getMaxAirSupply();
//
//            if (dog.isEyeInFluid(FluidTags.WATER) || l6 < j7) {
//                int air = dog.getAirSupply();
//                int full = Mth.ceil((air - 2) * 10.0D / 300.0D);
//                int partial = Mth.ceil(air * 10.0D / 300.0D) - full;
//
//                for (int i = 0; i < full + partial; ++i) {
//                    mc.gui.blit(stack, left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
//                }
//                ForgeIngameGui.right_height += 10;
//            }
//            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//            RenderSystem.disableBlend();
//
//            RenderSystem.popMatrix();
////        }
//    }
}
