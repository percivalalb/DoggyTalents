package doggytalents.client.gui;

import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.lib.ResourceLib;
import doggytalents.tileentity.TileEntityFoodBowl;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFoodBowl extends GuiContainer {
	
    private TileEntityFoodBowl foodBowl;

    public GuiFoodBowl(InventoryPlayer playerInventory, TileEntityFoodBowl foodBowl) {
        super(new ContainerFoodBowl(playerInventory, foodBowl));
        this.foodBowl = foodBowl;
        this.ySize = 127;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	String s = this.foodBowl.inventory.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, 10, 8, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ResourceLib.GUI_FOOD_BOWL);
        int var2 = (this.width - this.xSize) / 2;
        int var3 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var2, var3, 0, 0, this.xSize, this.ySize);
    }
}
