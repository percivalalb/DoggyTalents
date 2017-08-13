package doggytalents.base.b;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class WorldRender {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
		Entity passenger = player.riddenByEntity;
			if(passenger instanceof EntityDog) {
				EntityDog dog = (EntityDog)passenger;
				//if(dog.coords.hasBedPos()) {
					int level = dog.talents.getLevel("bedfinder");
					double distance = (level * 200D) - dog.getDistance(dog.coords.getBedX(), dog.coords.getBedY(), dog.coords.getBedZ());
				    if(level == 5 || distance >= 0.0D) {
						
						
				    	GL11.glPushMatrix();
						
						AxisAlignedBB boundingBox = new AxisAlignedBB(dog.coords.getBedX(), dog.coords.getBedY(), dog.coords.getBedZ(), dog.coords.getBedX() + 1, dog.coords.getBedY() + 1, dog.coords.getBedZ() + 1).grow(0.5D, 0.5D, 0.5D);
						this.drawSelectionBox(player, event.partialTicks, boundingBox);
						GL11.glPopMatrix();
				    }
				//}
			}
		
	}
	
	public void drawSelectionBox(EntityPlayer player, float particleTicks, AxisAlignedBB boundingBox) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.7F);
        //TODO Used when drawing outline of bounding box 
        GL11.glLineWidth(2.0F);
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    	double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)particleTicks;
    	double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)particleTicks;
    	double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)particleTicks;

        RenderGlobal.drawOutlinedBoundingBox(boundingBox.offset(-d0, -d1, -d2), 255, 255, 0, 255);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
        GL11.glEnable(GL11.GL_DEPTH_TEST); //Make the line see thought blocks
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING); //Make the line see thought blocks
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }
}
