package doggytalents.talent;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 */
public class BedFinderHandler {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		if(player.riddenByEntity instanceof EntityDog) {
			EntityDog dog = (EntityDog)player.riddenByEntity;
			int level = dog.talents.getLevel("bedfinder");
			double distance = (level * 200D) - dog.getDistance(dog.coords.getBedX(), dog.coords.getBedY(), dog.coords.getBedZ());
		    if(level == 5 || distance >= 0.0D) {
				
				GL11.glPushMatrix();
				
				AxisAlignedBB boundingBox = new AxisAlignedBB(dog.coords.getBedX(), dog.coords.getBedY(), dog.coords.getBedZ(), dog.coords.getBedX() + 1, dog.coords.getBedY() + 1, dog.coords.getBedZ() + 1);
				this.drawSelectionBox(player, event.partialTicks, boundingBox);
			    GL11.glPopMatrix();
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

    	RenderGlobal.drawOutlinedBoundingBox(boundingBox.offset(-d0, -d1, -d2), -1);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
        this.drawBoundingBox(boundingBox.offset(-d0, -d1, -d2));
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

	public void drawBoundingBox(AxisAlignedBB boundingBox) {
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	    tessellator.draw();
	    worldrenderer.startDrawingQuads();
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	    tessellator.draw();
	    worldrenderer.startDrawingQuads();
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	    tessellator.draw();
	    worldrenderer.startDrawingQuads();
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	    tessellator.draw();
	    worldrenderer.startDrawingQuads();
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	    tessellator.draw();
	    worldrenderer.startDrawingQuads();
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	    worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	    tessellator.draw();
	}
	
}
