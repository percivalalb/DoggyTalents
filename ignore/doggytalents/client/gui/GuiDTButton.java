package doggytalents.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;

/**
 * @author ProPercivalalb
 * You may look at this file to gain knowledge of javascript
 * but must not copy any features or code directly.
 **/
public class GuiDTButton extends GuiButton {

	public GuiDTButton(int par1, int par2, int par3, String par4Str) {
		super(par1, par2, par3, par4Str);
	}
	
	public GuiDTButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}
	
    private int xPos = 0;
    private int yPos = 0;

    /** The width of this text field. */
    private final int width_2 = 250;
    private final int height_2 = 50;
	
    protected long lastClicked = 0L;
    private boolean canShow = false;
    
	public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
        	boolean flag1 = Minecraft.getSystemTime() - this.lastClicked < 250L;
            this.field_82253_i = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            if(flag1 == true) {
            	canShow = true;
            }
            
            if(field_82253_i && canShow) {
            	ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
                int j = scaledresolution.getScaledWidth();
                int k = scaledresolution.getScaledHeight();
        	    this.xPos = j / 2 - 125;
        	    this.yPos = k / 2 - 50 + 47 + 60;
            	drawRect(this.xPos - 1, this.yPos - 1, this.xPos + this.width_2 + 1, this.yPos + this.height_2 + 1, -6250336);
                drawRect(this.xPos, this.yPos, this.xPos + this.width_2, this.yPos + this.height_2, -16777216);
            }
            else if(!field_82253_i && canShow) {
            	canShow = false;
            }
 
            FontRenderer fontrenderer = par1Minecraft.fontRenderer;
            par1Minecraft.renderEngine.func_98187_b("/gui/gui.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            int k = this.getHoverState(this.field_82253_i);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.mouseDragged(par1Minecraft, par2, par3);
            int l = 14737632;

            if (!this.enabled)
            {
                l = -6250336;
            }
            else if (this.field_82253_i)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
	
	public boolean rightClick(Minecraft par1Minecraft, int par2, int par3) {
        return this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
	}
}
