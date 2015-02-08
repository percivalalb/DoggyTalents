package doggytalents.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.DoggyUtil;
import doggytalents.entity.EntityDTDoggy;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceReference;

/**
 * @author ProPercivalalb
 */
public class RenderDTDoggy extends RenderLiving {
	
    public RenderDTDoggy(ModelBase baseModel, ModelBase renderPassModel, float shadowSize) {
        super(baseModel, shadowSize);
        this.setRenderPassModel(renderPassModel);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entityLivingBase, int renderPass, float partialTickTime) {
    	EntityDTDoggy dog = (EntityDTDoggy)entityLivingBase;
    	
    	float brightness = dog.getBrightness(partialTickTime) * dog.getShadingWhileShaking(partialTickTime);

        if(renderPass == 0 && dog.getWolfShaking()) {
            this.bindTexture(this.getEntityTexture(dog));
            GL11.glColor3f(brightness, brightness, brightness);
            return 1;
        }
        else if(renderPass == 1 && (dog.getHealth() == 1 && dog.isImmortal() && Constants.bloodWhenIncapacitated)) {
        	this.bindTexture(ResourceReference.doggyHurt);
            GL11.glColor3f(brightness, brightness, brightness);
            return 1;
        }
        else if(renderPass == 2 && dog.hasRadarCollar()) {
        	this.bindTexture(ResourceReference.doggyRadioCollar);
            GL11.glColor3f(brightness, brightness, brightness);
            return 1;
        }
        else
             return -1;
    }
    
    @Override
    protected void passSpecialRender(EntityLivingBase par1EntityLiving, double x, double y, double z) {
        this.renderName((EntityDTDoggy)par1EntityLiving, x, y, z);
        super.passSpecialRender(par1EntityLiving, x, y, z);
    }
    
    protected void renderName(EntityLiving entityliving, double x, double y, double z) {
        EntityDTDoggy dog = (EntityDTDoggy)entityliving;

        if(Minecraft.isGuiEnabled() && !dog.getWolfName().equals("") && dog != renderManager.livingPlayer) {
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            float currentDistance = dog.getDistanceToEntity(renderManager.livingPlayer);
            float nameVisableDistance = dog.isSneaking() ? 32F : 64F;
            byte hungerVisableDistance = ((byte)(!Constants.isHungerOn ? 0 : 4));

            if (currentDistance < nameVisableDistance)
            {
               	String modeTip = dog.mode.getMode().getTip();
                
                if (dog.getHealth() == 1)
                    modeTip = "(I)";

                String dogName = dog.getWolfName();
                String dogTummy = String.format("(%d)", dog.getDogTummy());

                if (!dog.isSneaking()) {
                    float f4 = 1.0F;
                    double d3 = 0.0D;

                    if (dog.riddenByEntity != null)
                        d3 = -0.20000000000000001D;

                    if (dog.riddenByEntity == null)
                        this.renderLivingLabel(dog, dogName, x, y - (double)f4, z, 64, 1.6F);

                    this.renderLivingLabel(dog, modeTip + "" + dogTummy, x, (y - (double)f4) + 0.11000000000000001D + d3, z, hungerVisableDistance, 0.7F);
                
                    String ownerName = DoggyUtil.getOwnerName(dog);
                    
                    if(this.renderManager.livingPlayer.isSneaking() && dog.riddenByEntity == null && !ownerName.equals(""))
                    	 this.renderLivingLabel(dog, ownerName, x, (y - (double)f4) + 0.21000000000000001D + d3, z, hungerVisableDistance, 0.7F);
                }
            }
        }
    }
    
    protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9, float size) {
        double var10 = par1EntityLiving.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (var10 <= (double)(par9 * par9))
        {
            FontRenderer var12 = this.getFontRendererFromRenderManager();
            float var13 = size;
            float var14 = 0.016666668F * var13;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var14, -var14, var14);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator var15 = Tessellator.instance;
            byte var16 = 0;

            if (par2Str.equals("deadmau5"))
            {
                var16 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var15.startDrawingQuads();
            int var17 = var12.getStringWidth(par2Str) / 2;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
            var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
            var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
            var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
            var15.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected float handleRotationFloat(EntityLivingBase entityLivingBase, float partialTickTime) {
    	EntityDTDoggy dog = (EntityDTDoggy)entityLivingBase;
        return dog.getTailRotation();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
    	EntityDTDoggy dog = (EntityDTDoggy)entity;
        if(dog.isTamed())
			return ResourceReference.getTameSkin(dog.getTameSkin());
    	
        return ResourceReference.doggyWild;
    }
}
