package doggytalents.handler;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 **/
public class ScreenRenderHandler {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		int mouseX = event.mouseX;
		int mouseY = event.mouseY;
		ElementType type = event.type;
		ScaledResolution scaling = event.resolution;
		
		
		
		if (event.type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT && mc.thePlayer != null && mc.thePlayer.ridingEntity instanceof EntityDog) {
			EntityDog dog = (EntityDog)mc.thePlayer.ridingEntity;
			int width = scaling.getScaledWidth();
			int height = scaling.getScaledHeight();
			GL11.glPushMatrix();
			mc.renderEngine.bindTexture(Gui.icons);
			
			GL11.glEnable(GL11.GL_BLEND);
	        int left = width / 2 + 91;
	        int top = height - GuiIngameForge.right_height;
	        GuiIngameForge.right_height += 10;
	        int level = MathHelper.ceiling_double_int(((double)dog.getDogHunger() / 120.0D) * 20.0D);

	        for (int i = 0; i < 10; ++i) {
	            int idx = i * 2 + 1;
	            int x = left - i * 8 - 9;
	            int y = top;
	            int icon = 16;
	            byte backgound = 12;

	            drawTexturedModalRect(x, y, 16 + backgound * 9, 27, 9, 9);


	            if (idx < level)
	                drawTexturedModalRect(x, y, icon + 36, 27, 9, 9);
	            else if (idx == level)
	                drawTexturedModalRect(x, y, icon + 45, 27, 9, 9);
	        }
	        GL11.glDisable(GL11.GL_BLEND);

	        GL11.glEnable(GL11.GL_BLEND);
	        left = width / 2 + 91;
	        top = height - GuiIngameForge.right_height;
	        GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
	        if (dog.isInsideOfMaterial(Material.water)) {
	            int air = dog.getAir();
	            int full = MathHelper.ceiling_double_int((double)(air - 2) * 10.0D / 300.0D);
	            int partial = MathHelper.ceiling_double_int((double)air * 10.0D / 300.0D) - full;

	            for (int i = 0; i < full + partial; ++i) {
	                drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
	            }
	            GuiIngameForge.right_height += 10;
	        }
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_BLEND);
	        
    		GL11.glPopMatrix();
		}
		
	}
	
	protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
	    float f = (float)(par5 >> 24 & 255) / 255.0F;
	    float f1 = (float)(par5 >> 16 & 255) / 255.0F;
	    float f2 = (float)(par5 >> 8 & 255) / 255.0F;
	    float f3 = (float)(par5 & 255) / 255.0F;
	    float f4 = (float)(par6 >> 24 & 255) / 255.0F;
	    float f5 = (float)(par6 >> 16 & 255) / 255.0F;
	    float f6 = (float)(par6 >> 8 & 255) / 255.0F;
	    float f7 = (float)(par6 & 255) / 255.0F;
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glDisable(GL11.GL_ALPHA_TEST);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glShadeModel(GL11.GL_SMOOTH);
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.startDrawingQuads();
	    tessellator.setColorRGBA_F(f1, f2, f3, f);
	    tessellator.addVertex((double)par3, (double)par2, (double)300.0F);
	    tessellator.addVertex((double)par1, (double)par2, (double)300.0F);
	    tessellator.setColorRGBA_F(f5, f6, f7, f4);
	    tessellator.addVertex((double)par1, (double)par4, (double)300.0F);
	    tessellator.addVertex((double)par3, (double)par4, (double)300.0F);
	    tessellator.draw();
	    GL11.glShadeModel(GL11.GL_FLAT);
	    GL11.glDisable(GL11.GL_BLEND);
	    GL11.glEnable(GL11.GL_ALPHA_TEST);
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	 
	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
	    float f = 0.00390625F;
	    float f1 = 0.00390625F;
	    Tessellator tessellator = Tessellator.instance;
	    tessellator.startDrawingQuads();
	    tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)0, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
	    tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)0, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
	    tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)0, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
	    tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)0, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
	    tessellator.draw();
	}

}
