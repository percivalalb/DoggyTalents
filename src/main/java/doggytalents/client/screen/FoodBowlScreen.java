package doggytalents.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.common.inventory.container.FoodBowlContainer;
import doggytalents.common.lib.Resources;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class FoodBowlScreen extends ContainerScreen<FoodBowlContainer> {

    public FoodBowlScreen(FoodBowlContainer foodBowl, PlayerInventory playerInventory, ITextComponent displayName) {
        super(foodBowl, playerInventory, displayName);
        this.ySize = 127;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        this.font.drawString(stack, this.title.getString(), 10.0F, 8.0F, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(Resources.GUI_FOOD_BOWL);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.blit(stack, x, y, 0, 0, this.xSize, this.ySize);
    }
}
