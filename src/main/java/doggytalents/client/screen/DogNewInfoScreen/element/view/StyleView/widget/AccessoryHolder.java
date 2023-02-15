package doggytalents.client.screen.DogNewInfoScreen.element.view.StyleView.widget;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import doggytalents.common.entity.DogEntity;
import doggytalents.common.lib.Resources;
import doggytalents.common.network.PacketHandler;
import doggytalents.common.network.packet.data.ChangeAccessoriesData;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ContainerScreenEvent.Render;
import net.minecraftforge.network.PacketDistributor;

public class AccessoryHolder extends AbstractWidget {

    ItemRenderer itemRenderer;
    DogEntity dog;

    public static final int ITEM_SIZE_ORG = 16;
    public static final int WIDGET_SIZE = 18;
    public static final int ICON_ADD_X = 11;
    public static final int ICON_REM_X = 0;
    
    private static final int BKGCOL_ADD = 0x57009e05;
    private static final int BKGCOL_REM = 0x579c0202;

    ItemStack itemStack = ItemStack.EMPTY;
    boolean add;
    int inventorySlotId = 0;

    public AccessoryHolder(int x, int y, ItemRenderer renderer, DogEntity dog, boolean add) {
        super(x, y, WIDGET_SIZE, WIDGET_SIZE, Component.empty());
        this.itemRenderer = renderer;
        this.add = add;
        this.dog = dog;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float pTicks) {
        this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
        this.active = !this.itemStack.isEmpty();
        if (!this.active) return;
        if (this.isHovered) {
            int bkg_col = this.add ? BKGCOL_ADD : BKGCOL_REM;
            fill(stack, this.getX(), this.getY(), this.getX()+this.width, this.getY()+this.height, bkg_col);
        }
        
        this.itemRenderer.renderGuiItem(itemStack, this.getX()+1, this.getY()+1);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, Resources.STYLE_ADD_REMOVE);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int iX = add ? ICON_ADD_X : ICON_REM_X;
        this.blit(stack, getX()+14, getY()+14, iX, 0, 9, 9);
    }

    @Override
    public void onClick(double x, double y) {
        PacketHandler.send(PacketDistributor.SERVER.noArg(), 
            new ChangeAccessoriesData(this.dog.getId(), add, inventorySlotId));
    }

    public void setStack(@Nonnull ItemStack stack) {
        this.itemStack = stack;
    }

    public int getInventorySlotId() {
        return this.inventorySlotId;
    }
    
    public void setInventorySlotId(int id) {
        this.inventorySlotId = id;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
        // TODO Auto-generated method stub
        
    }
    
}
