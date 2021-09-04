package doggytalents.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.common.inventory.container.FoodBowlContainer;
import doggytalents.common.lib.Resources;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FoodBowlScreen extends AbstractContainerScreen<FoodBowlContainer> {

    public FoodBowlScreen(FoodBowlContainer foodBowl, Inventory playerInventory, Component displayName) {
        super(foodBowl, playerInventory, displayName);
        this.imageHeight = 127;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack stack, int mouseX, int mouseY) {
        this.font.draw(stack, this.title.getString(), 10.0F, 8.0F, 4210752);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Resources.GUI_FOOD_BOWL);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(stack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}
