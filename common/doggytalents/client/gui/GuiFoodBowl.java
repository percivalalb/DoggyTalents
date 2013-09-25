package doggytalents.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;

import doggytalents.inventory.ContainerFoodBowl;
import doggytalents.lib.ResourceReference;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 **/
public class GuiFoodBowl extends GuiContainer {
	
    private TileEntityFoodBowl foodBowl;

    public GuiFoodBowl(InventoryPlayer par1InventoryPlayer, TileEntityFoodBowl par2TileEntityFoodBowl) {
        super(new ContainerFoodBowl(par1InventoryPlayer, par2TileEntityFoodBowl));
        foodBowl = par2TileEntityFoodBowl;
        this.ySize = 127;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int var1, int var2) {
    	String s = this.foodBowl.isInvNameLocalized() ? this.foodBowl.getInvName() : I18n.getString(this.foodBowl.getInvName());
        this.fontRenderer.drawString(s, 10, 8, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ResourceReference.foodBowl);
        int var2 = (this.width - this.xSize) / 2;
        int var3 = (this.height - this.ySize) / 2;
        drawTexturedModalRect(var2, var3, 0, 0, this.xSize, this.ySize);
    }
}
