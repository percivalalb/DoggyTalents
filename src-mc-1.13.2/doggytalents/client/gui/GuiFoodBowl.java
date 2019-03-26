package doggytalents.client.gui;

import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.lib.ResourceLib;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFoodBowl extends GuiContainer {
	
    private TileEntityFoodBowl foodBowl;

    public GuiFoodBowl(InventoryPlayer playerInventory, TileEntityFoodBowl foodBowl) {
        super(new ContainerFoodBowl(playerInventory, foodBowl));
        this.foodBowl = foodBowl;
        this.ySize = 127;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int var1, int var2) {
    	String s = this.foodBowl.getDisplayName().getString();
        this.fontRenderer.drawString(s, 10, 8, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int mouseX, int mouseY) {
    	GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ResourceLib.GUI_FOOD_BOWL);
        int var2 = (this.width - this.xSize) / 2;
        int var3 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var2, var3, 0, 0, this.xSize, this.ySize);
    }
}
