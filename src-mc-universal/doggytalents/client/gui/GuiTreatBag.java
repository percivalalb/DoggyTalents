package doggytalents.client.gui;

import doggytalents.base.ObjectLib;
import doggytalents.inventory.ContainerTreatBag;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class GuiTreatBag extends GuiContainer {

	public GuiTreatBag(EntityPlayer playerIn, int slotIn, ItemStack itemstackIn) {
		super(new ContainerTreatBag(playerIn, slotIn, itemstackIn));
		this.ySize = 127;
	}
	
	@Override
    protected void drawGuiContainerForegroundLayer(int var1, int var2) {
        this.fontRenderer.drawString(ObjectLib.BRIDGE.translateToLocal("container.doggytalents.treatbag"), 10, 8, 4210752);
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ResourceLib.GUI_TREAT_BAG);
        int var2 = (this.width - this.xSize) / 2;
        int var3 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var2, var3, 0, 0, this.xSize, this.ySize);
	}

}
