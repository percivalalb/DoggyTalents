package doggytalents.client.renderer.entity;

import java.util.UUID;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;

import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceReference;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class RenderDog extends RenderLiving<EntityDog> {
	
    public RenderDog(RenderManager renderManager, ModelBase model, float shadowSize) {
        super(renderManager, model, shadowSize);
        this.addLayer(new LayerRadioCollar(this));
        this.addLayer(new LayerDogHurt(this));
    }

    @Override
    protected float handleRotationFloat(EntityDog dog, float partialTickTime) {
        return dog.getTailRotation();
    }
    
    @Override
    protected void renderLivingAt(EntityDog p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_) {
        if (p_77039_1_.isEntityAlive() && p_77039_1_.isPlayerSleeping())
        {
            super.renderLivingAt(p_77039_1_, p_77039_2_, p_77039_4_ + 0.5F, p_77039_6_);
        }
        else
        {
            super.renderLivingAt(p_77039_1_, p_77039_2_, p_77039_4_, p_77039_6_);
        }
    }

    @Override
    protected void rotateCorpse(EntityDog bat, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        if (bat.isEntityAlive() && bat.isPlayerSleeping())
        {
           // GlStateManager.rotate(bat.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
        }
    }

    @Override
    public void doRender(EntityDog dog, double x, double y, double z, float entityYaw, float partialTicks) {
    	
    	if(dog.isDogWet()) {
            float f2 = dog.getBrightness(partialTicks) * dog.getShadingWhileShaking(partialTicks);
            GlStateManager.color(f2, f2, f2);
        }

        super.doRender(dog, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityDog dog) {
        if(dog.isTamed())
			return ResourceReference.getTameSkin(dog.getTameSkin());
    	
        return ResourceReference.doggyWild;
    }
    
    @Override
	public void renderName(EntityDog dog, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
        
        if(!dog.getDogName().isEmpty())
        	super.renderName(dog, p_77033_2_, p_77033_4_, p_77033_6_);
    }
    
    @Override
    protected void renderOffsetLivingLabel(EntityDog dog, double x, double y, double z, String displayName, float scale, double distanceFromPlayer) {
    	super.renderOffsetLivingLabel(dog, x, y, z, displayName, scale, distanceFromPlayer);
    	
    	if(distanceFromPlayer < 100.0D) {
        	
            y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F);
        	
            String tip = dog.mode.getMode().getTip();
            
            if(dog.isImmortal() && dog.getHealth() <= 1)
            	tip = "(I)";
            
            String label = String.format("%s(%d)", tip, dog.getDogHunger());
            
            if (dog.isPlayerSleeping())
                this.renderLivingLabel(dog, label,  x, y - 0.5D, z, 64, 0.7F);
            else
                this.renderLivingLabel(dog, label, x, y, z, 64, 0.7F);
        }
    	
    	if(distanceFromPlayer < 100.0D) {
    		y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.5F);
              
           if(this.renderManager.livingPlayer.isSneaking()) {
        	   EntityLivingBase owner = dog.getOwner();
        	   if(owner != null)
        		   this.renderLivingLabel(dog, owner.getDisplayName().getUnformattedText(), x, y, z, 5, 0.5F);
        	   else
        		   this.renderLivingLabel(dog, dog.getOwnerId(), x, y, z, 5, 0.5F);
           }
    	}
    }
    
    protected void renderLivingLabel(EntityDog entityIn, String str, double x, double y, double z, int maxDistance, float scale) {
    	 double d0 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);

    	 if(d0 <= (double)(maxDistance * maxDistance)) {
        	 FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
             float f1 = 0.016666668F * scale;
             GlStateManager.pushMatrix();
             GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
             GL11.glNormal3f(0.0F, 1.0F, 0.0F);
             GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
             GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
             GlStateManager.scale(-f1, -f1, f1);
             GlStateManager.disableLighting();
             GlStateManager.depthMask(false);
             GlStateManager.disableDepth();
             GlStateManager.enableBlend();
             GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
             Tessellator tessellator = Tessellator.getInstance();
             WorldRenderer worldrenderer = tessellator.getWorldRenderer();
             int i = 0;

             if (str.equals("deadmau5"))
             {
                 i = -10;
             }

             int j = fontrenderer.getStringWidth(str) / 2;
             GlStateManager.disableTexture2D();
             worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
             worldrenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
             worldrenderer.pos((double)(-j - 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
             worldrenderer.pos((double)(j + 1), (double)(8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
             worldrenderer.pos((double)(j + 1), (double)(-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
             tessellator.draw();
             GlStateManager.enableTexture2D();
             fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
             GlStateManager.enableDepth();
             GlStateManager.depthMask(true);
             fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
             GlStateManager.enableLighting();
             GlStateManager.disableBlend();
             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
             GlStateManager.popMatrix();
         }
    }
}