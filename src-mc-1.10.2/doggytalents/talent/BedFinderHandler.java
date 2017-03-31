package doggytalents.talent;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author ProPercivalalb
 */
public class BedFinderHandler {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		if(player.getRidingEntity() instanceof EntityDog) {
			EntityDog dog = (EntityDog)player.getRidingEntity();
			if(dog.coords.hasBedPos()) {
				int level = dog.talents.getLevel("bedfinder");
				double distance = (level * 200D) - Math.sqrt(dog.getDistanceSq(dog.coords.getBedPos()));
			    if(level == 5 || distance >= 0.0D) {
					
					GL11.glPushMatrix();
					
					AxisAlignedBB boundingBox = new AxisAlignedBB(dog.coords.getBedPos());
					//TODO this.drawSelectionBox(player, event.getPartialTicks(), boundingBox);
				    GL11.glPopMatrix();
			    }
			}
		}
		 
	}
	public void drawSelectionBox(EntityPlayer player, float particleTicks, AxisAlignedBB boundingBox) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box 
        GL11.glLineWidth(2.0F);
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    	double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTicks;
    	double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTicks;
    	double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTicks;

    	//RenderGlobal.drawOutlinedBoundingBox(boundingBox.offset(-d0, -d1, -d2), 0, 0, 0, 255);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
        this.drawBoundingBox(boundingBox.offset(-d0, -d1, -d2), 0, 0, 0, 255);
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

	public void drawBoundingBox(AxisAlignedBB boundingBox, int p_181563_1_, int p_181563_2_, int p_181563_3_, int p_181563_4_) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    tessellator.draw();
	     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    tessellator.draw();
	     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    tessellator.draw();
	     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    tessellator.draw();
	     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    tessellator.draw();
	     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).color(p_181563_1_, p_181563_2_, p_181563_3_, p_181563_4_).endVertex();
	    tessellator.draw();
	}
	
}
