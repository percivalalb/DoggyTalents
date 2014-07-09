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

import doggytalents.entity.EntityDTDoggy;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceReference;

/**
 * @author ProPercivalalb
 */
public class RenderDTDoggy extends RenderLiving
{
    public RenderDTDoggy(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3)
    {
        super(par1ModelBase, par3);
        this.setRenderPassModel(par2ModelBase);
    }

    protected float getTailRotation(EntityDTDoggy par1EntityDTDoggy, float par2)
    {
        return par1EntityDTDoggy.getTailRotation();
    }

    protected int func_82447_a(EntityDTDoggy par1EntityDTDoggy, int par2, float par3)
    {
        float f1;

        if (par2 == 0 && par1EntityDTDoggy.getWolfShaking())
        {
            f1 = par1EntityDTDoggy.getBrightness(par3) * par1EntityDTDoggy.getShadingWhileShaking(par3);
            this.bindTexture(this.getDogTexture(par1EntityDTDoggy));
            GL11.glColor3f(f1, f1, f1);
            return 1;
        }
        else if (par2 == 1 && (par1EntityDTDoggy.getHealth() == 1 && par1EntityDTDoggy.isImmortal() && Constants.bloodWhenIncapacitated)) {
        	this.bindTexture(ResourceReference.doggyHurt);
            f1 = par1EntityDTDoggy.getBrightness(par3) * par1EntityDTDoggy.getShadingWhileShaking(par3);
            GL11.glColor3f(f1, f1, f1);
            return 1;
        }
        else
        {
            return -1;
        }
    }

    protected ResourceLocation getDogTexture(EntityDTDoggy dog) {
    	if(dog.isTamed()) {
			switch(dog.getTameSkin()) {
			case 0: return ResourceReference.doggyTame0;
			case 1: return ResourceReference.doggyTame1;
			case 2: return ResourceReference.doggyTame2;
			case 3: return ResourceReference.doggyTame3;
			case 4: return ResourceReference.doggyTame4;
			case 5: return ResourceReference.doggyTame5;
			case 6: return ResourceReference.doggyTame6;
			case 7: return ResourceReference.doggyTame7;
			case 8: return ResourceReference.doggyTame8;
			case 9: return ResourceReference.doggyTame9;
			case 10: return ResourceReference.doggyTame10;
			case 11: return ResourceReference.doggyTame11;
			case 12: return ResourceReference.doggyTame12;
			case 13: return ResourceReference.doggyTame13;
			case 14: return ResourceReference.doggyTame14;
			case 15: return ResourceReference.doggyTame15;
			case 16: return ResourceReference.doggyTame16;
			}
        }
    	
        return ResourceReference.doggyWild;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return this.func_82447_a((EntityDTDoggy)par1EntityLivingBase, par2, par3);
    }
    
    @Override
    protected void passSpecialRender(EntityLivingBase par1EntityLiving, double par2, double par4, double par6)
    {
        this.renderName((EntityDTDoggy)par1EntityLiving, par2, par4, par6);
        super.passSpecialRender(par1EntityLiving, par2, par4, par6);
    }
    
    protected void renderName(EntityLiving entityliving, double d, double d1, double d2)
    {
        EntityDTDoggy dog = (EntityDTDoggy)entityliving;

        if (Minecraft.isGuiEnabled() && !dog.getWolfName().equals("") && dog != renderManager.livingPlayer)
        {
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            float currentDistance = dog.getDistanceToEntity(renderManager.livingPlayer);
            float nameVisableDistance = dog.isSneaking() ? 32F : 64F;
            byte hungerVisableDistance = ((byte)(!Constants.isHungerOn ? 0 : 4));

            if (currentDistance < nameVisableDistance)
            {
               	String modeTip = dog.mode.getMode().getTip();
                
                if (dog.getHealth() == 1) {
                    modeTip = "(I)";
                }

                String dogName = dog.getWolfName();
                String dogTummy = String.format("(%d)", dog.getDogTummy());

                if (!dog.isSneaking())
                {
                    float f4 = 1.0F;
                    double d3 = 0.0D;

                    if (dog.riddenByEntity != null)
                    {
                        d3 = -0.20000000000000001D;
                    }

                    if (dog.riddenByEntity == null)
                    {
                        renderLivingLabel(dog, dogName, d, d1 - (double)f4, d2, 64, 1.6F);
                    }

                    renderLivingLabel(dog, modeTip + "" + dogTummy, d, (d1 - (double)f4) + 0.11000000000000001D + d3, d2, hungerVisableDistance, 0.7F);
                
                    if(renderManager.livingPlayer.isSneaking() && dog.riddenByEntity == null && !dog.func_152113_b().equals(""))
                    {
                    	renderLivingLabel(dog, dog.func_152113_b(), d, (d1 - (double)f4) + 0.21000000000000001D + d3, d2, hungerVisableDistance, 0.7F);
                    }
                }
                else
                {
                    FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)d + 0.0F, (float)d1, (float)d2);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDepthMask(false);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator tessellator = Tessellator.instance;
                    int i = 0;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int j = fontrenderer.getStringWidth(dogName) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
                    tessellator.addVertex(-j - 1, -1 + i, 0.0D);
                    tessellator.addVertex(-j - 1, 8 + i, 0.0D);
                    tessellator.addVertex(j + 1, 8 + i, 0.0D);
                    tessellator.addVertex(j + 1, -1D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(dogName, -fontrenderer.getStringWidth(dogName) / 2, 0, 0x20c82536);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
            }
        }
    }
    
    protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9, float size)
    {
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

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2)
    {
        return this.getTailRotation((EntityDTDoggy)par1EntityLivingBase, par2);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.getDogTexture((EntityDTDoggy)par1Entity);
    }
}
