package doggytalents.client.gui;

import net.minecraft.client.gui.GuiButton;

/**
 * @author ProPercivalalb
 */
public class GuiCustomButton extends GuiButton {
	
	public GuiCustomButton(int id, int xPosition, int yPosition, int width, int height, String text) {
        super(id, xPosition, yPosition, width, height, text);
    }
	
    public boolean isMouseAbove(int par2, int par3) {
    	 return par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
    }
}
