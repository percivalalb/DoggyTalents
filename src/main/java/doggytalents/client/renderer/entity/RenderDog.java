package doggytalents.client.renderer.entity;

import java.util.UUID;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
public class RenderDog extends RenderLiving {
	
    public RenderDog(RenderManager renderManager, ModelBase model, float shadowSize) {
        super(renderManager, model, shadowSize);
    }

    protected float handleRotationFloat(EntityDog dog, float partialTickTime) {
        return dog.getTailRotation();
    }
    
    @Override
    protected void renderLivingAt(EntityLivingBase p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_)
    {
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
    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        if (p_77043_1_.isEntityAlive() && p_77043_1_.isPlayerSleeping())
        {
            //GL11.glRotatef(p_77043_1_.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            super.rotateCorpse(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
        }
    }

    @Override
    public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks) {
    	EntityDog dog = (EntityDog)entity;
    	if(dog.getDogShaking()) {
            float f2 = dog.getBrightness(partialTicks) * dog.getShadingWhileShaking(partialTicks);
            GlStateManager.color(f2, f2, f2);
        }

        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityDog dog) {
        if(dog.isTamed())
			return ResourceReference.getTameSkin(dog.getTameSkin());
    	
        return ResourceReference.doggyWild;
    }
    
    @Override
	public void passSpecialRender(EntityLivingBase entityLivingBase, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
    	EntityDog dog = (EntityDog)entityLivingBase;
        
        if(!dog.getDogName().isEmpty())
        	super.passSpecialRender(entityLivingBase, p_77033_2_, p_77033_4_, p_77033_6_);
    }
    
    @Override
    protected void func_177069_a(Entity entity, double x, double y, double z, String displayName, float scale, double distanceFromPlayer) {
    	super.func_177069_a(entity, x, y, z, displayName, scale, distanceFromPlayer);
        
    	EntityDog dog = (EntityDog)entity;
    	
    	if (distanceFromPlayer < 100.0D) {
        	
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
    	
    	if (distanceFromPlayer < 100.0D) {
    		y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.5F);
              
           if(this.renderManager.livingPlayer.isSneaking()) {
        	   EntityLivingBase owner = dog.getOwnerEntity();
        	   if(owner != null)
        		   this.renderLivingLabel(dog, owner.getDisplayName().getUnformattedText(), x, y, z, 5, 0.5F);
        	   else
        		   this.renderLivingLabel(dog, dog.getOwnerId(), x, y, z, 5, 0.5F);
           }
    	}
    }
    
    protected void renderLivingLabel(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_, float scale) {
    	 double d3 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);

         if (d3 <= (double)(p_147906_9_ * p_147906_9_)) {
             FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
             float f1 = 0.016666668F * scale;
             GlStateManager.pushMatrix();
             GlStateManager.translate((float)p_147906_3_ + 0.0F, (float)p_147906_5_ + p_147906_1_.height + 0.5F, (float)p_147906_7_);
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
             byte b0 = 0;

             if (p_147906_2_.equals("deadmau5"))
                 b0 = -10;

             GlStateManager.disableTexture2D();
             worldrenderer.startDrawingQuads();
             int j = fontrenderer.getStringWidth(p_147906_2_) / 2;
             worldrenderer.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
             worldrenderer.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
             worldrenderer.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
             worldrenderer.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
             worldrenderer.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
             tessellator.draw();
             GlStateManager.enableTexture2D();
             fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, 553648127);
             GlStateManager.enableDepth();
             GlStateManager.depthMask(true);
             fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, -1);
             GlStateManager.enableLighting();
             GlStateManager.disableBlend();
             GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
             GlStateManager.popMatrix();
         }
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entityLivingBase, float partialTickTime) {
        return this.handleRotationFloat((EntityDog)entityLivingBase, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((EntityDog)entity);
    }
}