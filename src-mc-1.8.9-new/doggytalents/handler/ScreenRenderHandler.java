package doggytalents.handler;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDog;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 **/
public class ScreenRenderHandler {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		ElementType type = event.type;
		ScaledResolution scaling = event.resolution;
		
		
		
		if(type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT && mc.thePlayer != null && mc.thePlayer.ridingEntity instanceof EntityDog) {
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
	
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
	    Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((double)(x + 0), (double)(y + height), 0.0D).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
	 	worldrenderer.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
	  	worldrenderer.pos((double)(x + width), (double)(y + 0), 0.0D).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
		worldrenderer.pos((double)(x + 0), (double)(y + 0), 0.0D).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
	   	tessellator.draw();
	}


}