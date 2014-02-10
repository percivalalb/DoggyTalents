package doggytalents.client.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import doggytalents.core.helper.TextureHelper;
import doggytalents.inventory.ContainerEmpty;

/**
 * @author ProPercivalalb
 */
public class GuiDTDoggy extends GuiScreenDT {
	
    public GuiDTDoggy() {
		super(new ContainerEmpty());
	}
	
	@Override
    public void initGui() {
		super.initGui();
	    int scaleX = width / 2;
	    int scaleY = height / 2;
		this.addDecreaseBtn(0, scaleX - 19, scaleY - 46, "-");
		this.addIncreaseBtn(1, scaleX - 4, scaleY - 46, "+");
		
    }
	
	public void addIncreaseBtn(int id, int x, int y, String text) {
		this.buttonList.add(new GuiTalentIncrease(id, x, y, 13, 12, text));
	}
	
	public void addDecreaseBtn(int id, int x, int y, String text) {
		this.buttonList.add(new GuiTalentDecrease(id, x, y, 13, 12, text));
	}

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	     for (int k = 0; k < this.buttonList.size(); ++k) {
	    	 GuiButton guibutton = (GuiButton)this.buttonList.get(k);
	    	 if(guibutton instanceof GuiSmallButton) {
	    		 GuiSmallButton btn = (GuiSmallButton)guibutton;
	    		 if(btn.isMouseAbove(mouseX, mouseY)) {
	    			 this.drawHoveringText(btn.getToolTip(), mouseX, mouseY, this.mc.fontRenderer);
	         	}
	    	 }
	     }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float particleTicks, int mouseX, int mouseZ) {
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	
        this.mc.renderEngine.bindTexture(TextureHelper.getGuiTexturePath("gui.png"));
        int scaleX = width / 2;
        int scaleY = height / 2;
        this.drawTexturedModalRect(scaleX - 40, scaleY - 50, 0, 0, 51, 19);
        this.itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, new ItemStack(Item.fishRaw), scaleX - 39, scaleY - 48);
    }
}
