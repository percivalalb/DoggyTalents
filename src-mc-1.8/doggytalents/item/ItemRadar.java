package doggytalents.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import doggytalents.api.DoggyTalentsAPI;
import doggytalents.entity.EntityDog;

/**
 * @author ProPercivalalb
 **/
public class ItemRadar extends ItemMap {
	
	public ItemRadar() {
		this.setCreativeTab(DoggyTalentsAPI.CREATIVE_TAB);
		this.setMaxStackSize(1);
	}

	@Override
	public MapData getMapData(ItemStack p_77873_1_, World p_77873_2_) {
		/**
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = player.worldObj;

		byte b0 = 0;
        byte b1 = 0;
        float f = 0.0F;
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/map/map_background.png"));
        int i = 0;
        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(49F / 255F, 150F / 255F, 49F / 255F, 1.0F);
        OpenGlHelper.glBlendFunc(1, 771, 0, 1);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)((float)(b0 + 0) + f), (double)((float)(b1 + 128) - f), -0.009999999776482582D, 0.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)((float)(b0 + 128) - f), (double)((float)(b1 + 128) - f), -0.009999999776482582D, 1.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)((float)(b0 + 128) - f), (double)((float)(b1 + 0) + f), -0.009999999776482582D, 1.0D, 0.0D);
        worldrenderer.addVertexWithUV((double)((float)(b0 + 0) + f), (double)((float)(b1 + 0) + f), -0.009999999776482582D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        
        GL11.glColor4f(0.0F, 82F / 255F, 0.0F, 1.0F);
        GL11.glTranslatef(0F, 0F, -0.12F);
        GL11.glLineWidth(1.0F);
        GL11.glBegin(1);
        GL11.glVertex2d(0.0D, 0D);
        GL11.glVertex2d(128.0D, 128.0D);
        GL11.glVertex2d(128.0D, 0D);
        GL11.glVertex2d(0.0D, 128.0D);
        GL11.glVertex2d(64.0D, 0D);
        GL11.glVertex2d(64.0D, 128.0D);
        GL11.glVertex2d(0.0D, 64D);
        GL11.glVertex2d(128.0D, 64.0D);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef(64F, 64F, -0.02F);
        GL11.glRotatef((player.rotationYaw % 360), 0.0F, 0.0F, -1.0F);
        GL11.glTranslatef(-64F, -64F, 0F);

		for(Object entity : world.loadedEntityList) {
			if(entity instanceof EntityDog) {
				EntityDog dog = (EntityDog)entity;

				if(dog.hasRadarCollar() && dog.canInteract(player)) {
					int icon = 6;
					if(dog.getOwner() != player)
						GL11.glColor3f(0.0F, 1.0F, 1.0F);
					
					int rotation = (int) dog.rotationYaw ;
					int centerX = (int) (player.posX - dog.posX);
					int centerZ = (int) (player.posZ - dog.posZ);
					if((centerX >= 128 || centerX <= -128) || (centerZ >= 128 || centerZ <= -128))
						continue;	
					
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)b0 + (float)centerX / 2.0F + 64.0F, (float)b1 + (float)centerZ / 2.0F + 64.0F, -0.02F);
                    GL11.glRotatef((dog.rotationYaw % 360), 0.0F, 0.0F, 1.0F);
                    GL11.glScalef(4.0F, 4.0F, 3.0F);
                    GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
                    float f1 = (float)(icon % 4 + 0) / 4.0F;
                    float f2 = (float)(icon / 4 + 0) / 4.0F;
                    float f3 = (float)(icon % 4 + 1) / 4.0F;
                    float f4 = (float)(icon / 4 + 1) / 4.0F;
                    worldrenderer.startDrawingQuads();
                    worldrenderer.addVertexWithUV(-1.0D, 1.0D, (double)((float)i * 0.001F), (double)f1, (double)f2);
                    worldrenderer.addVertexWithUV(1.0D, 1.0D, (double)((float)i * 0.001F), (double)f3, (double)f2);
                    worldrenderer.addVertexWithUV(1.0D, -1.0D, (double)((float)i * 0.001F), (double)f3, (double)f4);
                    worldrenderer.addVertexWithUV(-1.0D, -1.0D, (double)((float)i * 0.001F), (double)f1, (double)f4);
                    tessellator.draw();
                    GL11.glPopMatrix();
                    ++i;
				}
            }
        }
		GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)b0 + 64.0F, (float)b1 + 64.0F, -0.02F);
        //GL11.glRotatef(dog.rotationPitch, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(4.0F, 4.0F, 3.0F);
        GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
        float f1 = (float)(5 % 4 + 0) / 4.0F;
        float f2 = (float)(5 / 4 + 0) / 4.0F;
        float f3 = (float)(5 % 4 + 1) / 4.0F;
        float f4 = (float)(5 / 4 + 1) / 4.0F;
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV(-1.0D, 1.0D, (double)((float)i * 0.001F), (double)f1, (double)f2);
        worldrenderer.addVertexWithUV(1.0D, 1.0D, (double)((float)i * 0.001F), (double)f3, (double)f2);
        worldrenderer.addVertexWithUV(1.0D, -1.0D, (double)((float)i * 0.001F), (double)f3, (double)f4);
        worldrenderer.addVertexWithUV(-1.0D, -1.0D, (double)((float)i * 0.001F), (double)f1, (double)f4);
        tessellator.draw();
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, -0.04F);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        
        return null;**/
    }
}
