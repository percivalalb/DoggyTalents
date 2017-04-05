package doggytalents.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderHandHandler {

	private static final ResourceLocation field_148253_a = new ResourceLocation("textures/map/map_icons.png");
	
	@SubscribeEvent
	public void renderHand(RenderHandEvent event) {
		/**
		Minecraft mc = Minecraft.getMinecraft();
		ItemRenderer itemRenderer = mc.getItemRenderer();
		EntityRenderer entityRenderer = mc.entityRenderer;
		TextureManager textureManager = mc.getTextureManager();
		EntityPlayer player = mc.thePlayer;
		World world = player.worldObj;
		
		int renderPass = event.renderPass;
		float partialTicks = event.partialTicks;
	
		if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() == ModItems.radar) {
			GL11.glPushMatrix();
	        GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
	        Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();

	        float f = 0.0F;
	        textureManager.bindTexture(this.field_148253_a);
	        int i = 0;

	        //GL11.glDisable(GL11.GL_TEXTURE_2D);
	        //GL11.glDisable(GL11.GL_LIGHTING);
	        //GL11.glEnable(GL11.GL_BLEND);
	       // GL11.glColor4f(49F / 255F, 150F / 255F, 49F / 255F, 1.0F);
	       // OpenGlHelper.glBlendFunc(1, 771, 0, 1);
	        //GL11.glDisable(GL11.GL_ALPHA_TEST);
	        worldrenderer.startDrawingQuads();
	        worldrenderer.addVertexWithUV(0, 128, -0.009999999776482582D, 0.0D, 1.0D);
	        worldrenderer.addVertexWithUV(128, 128, -0.009999999776482582D, 1.0D, 1.0D);
	        worldrenderer.addVertexWithUV(128, 0, -0.009999999776482582D, 1.0D, 0.0D);
	        worldrenderer.addVertexWithUV(0, 0, -0.009999999776482582D, 0.0D, 0.0D);
	        tessellator.draw();
	       
	        
	        GL11.glPopMatrix();
		}**/

		//event.setCanceled(true);
	}
}
