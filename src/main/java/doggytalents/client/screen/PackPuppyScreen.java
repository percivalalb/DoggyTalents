package doggytalents.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.common.inventory.container.PackPuppyContainer;
import doggytalents.common.lib.Resources;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class PackPuppyScreen extends AbstractContainerScreen<PackPuppyContainer> {

    public PackPuppyScreen(PackPuppyContainer packPuppy, Inventory playerInventory, Component displayName) {
        super(packPuppy, playerInventory, displayName);
        //TODO this.container.allowUserInput = false;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack stack, int par1, int par2) {
        this.font.draw(stack, this.title.getString(), this.imageWidth / 2 - 10, 10, 4210752);
        this.font.draw(stack, this.playerInventoryTitle, 8.0F, this.imageHeight - 96 - 2, 4210752);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int xMouse, int yMouse) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Resources.GUI_PACK_PUPPY);
        int l = (this.width - this.imageWidth) / 2;
        int i1 = (this.height - this.imageHeight) / 2;
        this.blit(stack, l, i1, 0, 0, this.imageWidth, this.imageHeight);

        for (int j1 = 0; j1 < 3; j1++)
            for (int k1 = 0; k1 < Mth.clamp(this.getMenu().getDogLevel(), 0, 5); k1++)
                this.blit(stack, l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);

        InventoryScreen.renderEntityInInventory(l + 42, i1 + 51, 30, (float)(l + 51) - xMouse, (float)((i1 + 75) - 50) - yMouse, this.getMenu().getDog());
    }
}
