package doggytalents.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import doggytalents.DoggyTalents;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class RenderDog extends RenderLiving {
	
    public RenderDog() {
        super(new ModelDog(), 0.5F);
        this.setRenderPassModel(new ModelDog());
        //this.addLayer(new LayerRadioCollar(this));
        //this.addLayer(new LayerDogCollar(this));
        //this.addLayer(new LayerDogHurt(this));
        //this.addLayer(new LayerBone(this));
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entity, float partialTickTime) {
    	EntityDog dog = (EntityDog)entity;
        return dog.getTailRotation();
    }
    
    @Override
    protected void renderLivingAt(EntityLivingBase entity, double x, double y, double z) {
    	EntityDog dog = (EntityDog)entity;
    	
        if(dog.isEntityAlive() && dog.isPlayerSleeping())
            super.renderLivingAt(dog, x, y + 0.5F, z);
        else
            super.renderLivingAt(dog, x, y, z);
    }
    
    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
    	EntityDog dog = (EntityDog)entity;
    	
    	if(dog.isDogWet()) {
            float f2 = dog.getBrightness(partialTicks) * dog.getShadingWhileWet(partialTicks);
            GL11.glColor3f(f2, f2, f2);
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entity, int renderPass, float partialTickTime) {
    	EntityDog dog = (EntityDog)entity;
    	
        float brightness = dog.getBrightness(partialTickTime) * dog.getShadingWhileWet(partialTickTime);
    	
        if(renderPass == 0 && dog.isShaking) {
            this.bindTexture(this.getEntityTexture(dog));
            GL11.glColor3f(brightness, brightness, brightness);
            return 1;
        }
        else if(renderPass == 1 && (dog.getHealth() == 1 && dog.isImmortal() && Constants.RENDER_BLOOD)) {
        	this.bindTexture(ResourceLib.MOB_LAYER_DOG_HURT);
            GL11.glColor3f(brightness, brightness, brightness);
            return 1;
        }
        else if(renderPass == 2 && dog.hasRadarCollar()) {
        	this.bindTexture(ResourceLib.MOB_LAYER_RADIO_COLLAR);
            GL11.glColor3f(brightness, brightness, brightness);
            return 1;
        }
        else if(renderPass == 3 && dog.hasCollar()) {
        	this.bindTexture(ResourceLib.MOB_LAYER_DOG_COLLAR);
            if(!dog.hasNoColour()) {
	            float[] afloat = dog.getCollar();
	            GL11.glColor3f(afloat[0], afloat[1], afloat[2]);
            }
            return 1;
        }
        else
            return -1;
    }
    
    protected ResourceLocation getEntityTexture(Entity entity) {
    	EntityDog dog = (EntityDog)entity;
    	
        if(dog.isTamed())
			return ResourceLib.MOB_DOG_TAME;
    	
        return ResourceLib.MOB_DOG_WILD;
    }
    //TODO
    
    
    @Override
    protected void func_96449_a(EntityLivingBase entityLivingBase, double x, double y, double z, String displayName, float scale, double distanceFromPlayer) {
    	super.func_96449_a(entityLivingBase, x, y, z, displayName, scale, distanceFromPlayer);
        
    	EntityDog dog = (EntityDog)entityLivingBase;
    	
    	if (distanceFromPlayer < 100.0D) {
        	
            y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F);
        	
            String tip = dog.mode.getMode().getTip();
            
            if(dog.isImmortal() && dog.getHealth() <= 1)
            	tip = "(I)";
            
            String label = String.format("%s(%d)", tip, dog.getDogHunger());
            
            if (entityLivingBase.isPlayerSleeping())
                this.renderLivingLabel(entityLivingBase, label,  x, y - 0.5D, z, 64, 0.7F);
            else
                this.renderLivingLabel(entityLivingBase, label, x, y, z, 64, 0.7F);
        }
    	
    	if (distanceFromPlayer < 5 * 5) {
              
           if(this.renderManager.livingPlayer.isSneaking()) {
        	   String ownerName = "A Wild Dog";
        	   if(dog.getOwner() != null)
        		   ownerName = dog.getOwner().func_145748_c_().getUnformattedText();
   				else if(dog.func_152113_b() != null)
   					ownerName = dog.func_152113_b().toString();
        	   
        	   this.renderLivingLabel(dog, ownerName, x, y - 0.34F, z, 5, 0.5F);
           }
    	}
    }
    
    protected void renderLivingLabel(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_, float scale) {
        double d3 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (d3 <= (double)(p_147906_9_ * p_147906_9_)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f1 = 0.016666668F * scale;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)p_147906_3_ + 0.0F, (float)p_147906_5_ + p_147906_1_.height + 0.5F, (float)p_147906_7_);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.instance;
            byte b0 = 0;

            if (p_147906_2_.equals("deadmau5"))
                b0 = -10;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int j = fontrenderer.getStringWidth(p_147906_2_) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
            tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
    /**
    @Override
    protected void func_96449_a(EntityLivingBase entity, double x, double y, double z, String name, float scale, double distanceFromPlayer) {
    	EntityDog dog = (EntityDog)entity;
    	
    	y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F);
        	
    	String tip = dog.mode.getMode().getTip();
            
    	if(dog.isImmortal() && dog.getHealth() <= 1)
    		tip = "(I)";
            
    	String label = String.format("%s(%d)", tip, dog.getDogHunger());
    	if (distanceFromPlayer <= (double)(64 * 64)) {
    		boolean flag = dog.isSneaking();
    		float f = this.renderManager.playerViewY;
    		float f1 = this.renderManager.playerViewX;
    		boolean flag1 = this.renderManager.options.thirdPersonView == 2;
    		float f2 = dog.height + 0.42F - (flag ? 0.25F : 0.0F) - (dog.isPlayerSleeping() ? 0.5F : 0);
    
    		ObjectLibClient.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), label, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag, 0.01F);
    	
    		if (distanceFromPlayer <= (double)(5 * 5)) {
	    		if(this.renderManager.renderViewEntity.isSneaking()) {
	    			String ownerName = "A Wild Dog";
	    			if(dog.getOwner() != null)
	    				ownerName = dog.getOwner().getDisplayName().getUnformattedText();
	    			else if(dog.getOwnerId() != null)
	          		   	ownerName = dog.getOwnerId().toString();
	    			
	    			ObjectLibClient.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), ownerName, (float)x, (float)y + f2 - 0.34F, (float)z, 0, f, f1, flag1, flag, 0.01F);
	    		}
    		}
    	}
    	
        super.renderEntityName(dog, x, y - 0.2, z, name, distanceFromPlayer);
    }**/
}