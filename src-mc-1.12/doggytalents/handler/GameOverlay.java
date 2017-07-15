package doggytalents.handler;

import doggytalents.entity.EntityDog;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 **/
public class GameOverlay {

	private static Minecraft mc = Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onPreRenderGameOverlay(RenderGameOverlayEvent.Post event) {
		ElementType type = event.getType();
		ScaledResolution scaling = event.getResolution();
		
		
		
		if(type == RenderGameOverlayEvent.ElementType.HEALTHMOUNT && mc.player != null && mc.player.getRidingEntity() instanceof EntityDog) {
			EntityDog dog = (EntityDog)mc.player.getRidingEntity();
			int width = scaling.getScaledWidth();
			int height = scaling.getScaledHeight();
			GlStateManager.pushMatrix();
			mc.renderEngine.bindTexture(Gui.ICONS);
			
			GlStateManager.enableBlend();
	        int left = width / 2 + 91;
	        int top = height - GuiIngameForge.right_height;
	        GuiIngameForge.right_height += 10;
	        int level = MathHelper.ceil(((double)dog.getDogHunger() / 120.0D) * 20.0D);

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
	        GlStateManager.disableBlend();

	        GlStateManager.enableBlend();
	        left = width / 2 + 91;
	        top = height - GuiIngameForge.right_height;
	        GlStateManager.color(1.0F, 1.0F, 0.0F, 1.0F);
	        if (dog.isInsideOfMaterial(Material.WATER)) {
	            int air = dog.getAir();
	            int full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
	            int partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;

	            for (int i = 0; i < full + partial; ++i) {
	                drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
	            }
	            GuiIngameForge.right_height += 10;
	        }
	        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.disableBlend();
	        
	        GlStateManager.popMatrix();
		}
	}
	
	public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
		double zLevel = 0.0D;
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
		tessellator.draw();
	}

}