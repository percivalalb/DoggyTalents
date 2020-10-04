package doggytalents.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import doggytalents.common.inventory.container.PackPuppyContainer;
import doggytalents.common.lib.Resources;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class PackPuppyScreen extends ContainerScreen<PackPuppyContainer> {

    public PackPuppyScreen(PackPuppyContainer packPuppy, PlayerInventory playerInventory, ITextComponent displayName) {
        super(packPuppy, playerInventory, displayName);
        //TODO this.field_147002_h.allowUserInput = false;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int par1, int par2) {
        this.font.drawString(stack, this.title.getString(), this.xSize / 2 - 10, 10, 4210752);
        this.font.drawString(stack, this.playerInventory.getDisplayName().getString(), 8.0F, this.ySize - 96 - 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int xMouse, int yMouse) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(Resources.GUI_PACK_PUPPY);
        int l = (this.width - this.xSize) / 2;
        int i1 = (this.height - this.ySize) / 2;
        this.blit(stack, l, i1, 0, 0, this.xSize, this.ySize);

        for (int j1 = 0; j1 < 3; j1++)
            for (int k1 = 0; k1 < MathHelper.clamp(this.getContainer().getDogLevel(), 0, 5); k1++)
                this.blit(stack, l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);

        InventoryScreen.drawEntityOnScreen(l + 42, i1 + 51, 30, (float)(l + 51) - xMouse, (float)((i1 + 75) - 50) - yMouse, this.getContainer().getDog());
    }
}
