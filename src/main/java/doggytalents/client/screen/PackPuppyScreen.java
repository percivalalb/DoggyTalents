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
        //TODO this.container.allowUserInput = false;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int par1, int par2) {
        this.font.draw(stack, this.title.getString(), this.imageWidth / 2 - 10, 10, 4210752);
        this.font.draw(stack, this.inventory.getDisplayName().getString(), 8.0F, this.imageHeight - 96 - 2, 4210752);
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int xMouse, int yMouse) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(Resources.GUI_PACK_PUPPY);
        int l = (this.width - this.imageWidth) / 2;
        int i1 = (this.height - this.imageHeight) / 2;
        this.blit(stack, l, i1, 0, 0, this.imageWidth, this.imageHeight);

        for (int j1 = 0; j1 < 3; j1++)
            for (int k1 = 0; k1 < MathHelper.clamp(this.getMenu().getDogLevel(), 0, 5); k1++)
                this.blit(stack, l + 78 + 18 * k1, i1 + 9 + 18 * j1 + 15, 197, 2, 18, 18);

        InventoryScreen.renderEntityInInventory(l + 42, i1 + 51, 30, (float)(l + 51) - xMouse, (float)((i1 + 75) - 50) - yMouse, this.getMenu().getDog());
    }
}
