package doggytalents.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;

import doggytalents.common.ContainerFoodBowl;
import doggytalents.tileentity.TileEntityFoodBowl;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/

public class GuiFoodBowl extends GuiContainer
{
    private TileEntityFoodBowl relatedTileEntityDogFoodBowl;

    public GuiFoodBowl(InventoryPlayer par1InventoryPlayer, TileEntityFoodBowl par2TileEntityFoodBowl)
    {
        super(new ContainerFoodBowl(par1InventoryPlayer, par2TileEntityFoodBowl));
        relatedTileEntityDogFoodBowl = par2TileEntityFoodBowl;
        this.ySize = 127;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int var1, int var2)
    {
    	this.fontRenderer.drawString("Food Bowl", 10, 8, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.func_98187_b("/doggytalents/foodBowl.png");
        int var2 = (this.width - this.xSize) / 2;
        int var3 = (this.height - this.ySize) / 2;
        drawTexturedModalRect(var2, var3, 0, 0, this.xSize, this.ySize);
    }
}
