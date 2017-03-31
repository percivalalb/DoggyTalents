package doggytalents.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceReference;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            float f2 = dog.getBrightness(partialTicks) * dog.getShadingWhileWet(partialTicks);
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
    protected void renderEntityName(EntityDog dog, double x, double y, double z, String name, double distanceFromPlayer)
    {
        //if (distanceFromPlayer < 100.0D)
       // {
       

            y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F);
        	
            String tip = dog.mode.getMode().getTip();
            
            if(dog.isImmortal() && dog.getHealth() <= 1)
            	tip = "(I)";
            
            String label = String.format("%s(%d)", tip, dog.getDogHunger());
            
            double d0 = dog.getDistanceSqToEntity(this.renderManager.renderViewEntity);

            if (d0 <= (double)(64 * 64))
            {
                boolean flag = dog.isSneaking();
                float f = this.renderManager.playerViewY;
                float f1 = this.renderManager.playerViewX;
                boolean flag1 = this.renderManager.options.thirdPersonView == 2;
                float f2 = dog.height + 0.42F - (flag ? 0.25F : 0.0F) - (dog.isPlayerSleeping() ? 0.5F : 0);
    
                func_189692_a(this.getFontRendererFromRenderManager(), label, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag);
            }
  

       // }

        super.renderEntityName(dog, x, y - 0.2, z, name, distanceFromPlayer);
    }
    
    /**
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
    }**/
    
    //TODO
    public static void func_189692_a(FontRenderer fontRenderer, String textToRender, float p_189692_2_, float p_189692_3_, float p_189692_4_, int p_189692_5_, float p_189692_6_, float p_189692_7_, boolean p_189692_8_, boolean p_189692_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(p_189692_2_, p_189692_3_, p_189692_4_);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-p_189692_6_, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(p_189692_8_ ? -1 : 1) * p_189692_7_, 1.0F, 0.0F, 0.0F);
        float scale = 0.010F;
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!p_189692_9_)
        {
            GlStateManager.disableDepth();
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRenderer.getStringWidth(textToRender) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)(-i - 1), (double)(-1 + p_189692_5_), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos((double)(-i - 1), (double)(8 + p_189692_5_), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos((double)(i + 1), (double)(8 + p_189692_5_), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        vertexbuffer.pos((double)(i + 1), (double)(-1 + p_189692_5_), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        if (!p_189692_9_)
        {
            fontRenderer.drawString(textToRender, -fontRenderer.getStringWidth(textToRender) / 2, p_189692_5_, 553648127);
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        fontRenderer.drawString(textToRender, -fontRenderer.getStringWidth(textToRender) / 2, p_189692_5_, p_189692_9_ ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
}