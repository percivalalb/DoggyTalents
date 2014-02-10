package doggytalents.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

/**
 * @author ProPercivalalb
 */
public abstract class GuiScreenDT extends GuiContainer {

    public GuiScreenDT(Container par1Container) {
		super(par1Container);
	}

	protected List textFieldList = new ArrayList();
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.textFieldList.clear();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
    	for (int k = 0; k < this.textFieldList.size(); ++k) {
        	GuiTextField guibutton = (GuiTextField)this.textFieldList.get(k);
            guibutton.updateCursorCounter();
        }
    }
    
    @Override
    protected void keyTyped(char var1, int var2) {
        if (var2 == Keyboard.KEY_ESCAPE) { //Exit Screen
        	this.mc.displayGuiScreen((GuiScreen)null);
           	this.mc.setIngameFocus();
           	return;
        }
    	for (int k = 0; k < this.textFieldList.size(); ++k) {
        	GuiTextField guibutton = (GuiTextField)this.textFieldList.get(k);
            guibutton.textboxKeyTyped(var1, var2);
        }
    }

    @Override
    protected void mouseClicked(int var1, int var2, int var3) {
        super.mouseClicked(var1, var2, var3);
        for (int k = 0; k < this.textFieldList.size(); ++k) {
        	GuiTextField guibutton = (GuiTextField)this.textFieldList.get(k);
            guibutton.mouseClicked(var1, var2, var3);
        }
    }
    
    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        for (int k = 0; k < this.textFieldList.size(); ++k) {
        	GuiTextField guibutton = (GuiTextField)this.textFieldList.get(k);
            guibutton.drawTextBox();
        }
    }
}
