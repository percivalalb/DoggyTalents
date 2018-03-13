package doggytalents.client.renderer.entity;

import doggytalents.base.ObjectLib;
import doggytalents.base.ObjectLibClient;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.layer.LayerArmor;
import doggytalents.client.renderer.entity.layer.LayerBone;
import doggytalents.client.renderer.entity.layer.LayerCape;
import doggytalents.client.renderer.entity.layer.LayerDogCollar;
import doggytalents.client.renderer.entity.layer.LayerDogHurt;
import doggytalents.client.renderer.entity.layer.LayerRadioCollar;
import doggytalents.client.renderer.entity.layer.LayerSunglasses;
import doggytalents.client.renderer.entity.layer.LayerWings;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class RenderDog extends RenderLiving<EntityDog> {
	
    public RenderDog(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelDog(0.0F), 0.5F);
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerRadioCollar(this));
        this.addLayer(new LayerDogCollar(this));
        this.addLayer(new LayerDogHurt(this));
        this.addLayer(new LayerBone(this));
        this.addLayer(new LayerSunglasses(this));
        //this.addLayer(new LayerSaddle(this));
        //this.addLayer(new LayerArmor(this));
        this.addLayer(new LayerWings(this));
    }

    @Override
    protected float handleRotationFloat(EntityDog dog, float partialTickTime) {
        return dog.getTailRotation();
    }
    
    @Override
    protected void renderLivingAt(EntityDog dog, double x, double y, double z) {
        if(dog.isEntityAlive() && dog.isPlayerSleeping())
            super.renderLivingAt(dog, x, y + 0.5F, z);
        else
            super.renderLivingAt(dog, x, y, z);
    }
    
    @Override
    public void doRender(EntityDog dog, double x, double y, double z, float entityYaw, float partialTicks) {
    	if(dog.isDogWet()) {
            float f2 = ObjectLib.METHODS.getBrightness(dog, partialTicks) * dog.getShadingWhileWet(partialTicks);
            GlStateManager.color(f2, f2, f2);
        }

        super.doRender(dog, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityDog dog) {
        if(dog.isTamed())
			return ResourceLib.getTameSkin(dog.getTameSkin());
    	
        return ResourceLib.MOB_DOG_WILD;
    }
    
    @Override
    public void renderName(EntityDog dog, double x, double y, double z) {
        if(this.canRenderName(dog)) {
        	GlStateManager.alphaFunc(516, 0.1F);
            double d0 = dog.getDistanceSq(this.renderManager.renderViewEntity);
            
            y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * 0.016666668F * 0.7F);
        	
        	String tip = dog.mode.getMode().getTip();
                
        	if(dog.isIncapacicated())
        		tip = "doggui.modetip.incapacitated";
                
        	String label = String.format("%s(%d)", ObjectLib.BRIDGE.translateToLocal(tip), dog.getDogHunger());
        	if(d0 <= (double)(64 * 64)) {
        		boolean flag = dog.isSneaking();
        		float f = this.renderManager.playerViewY;
        		float f1 = this.renderManager.playerViewX;
        		boolean flag1 = this.renderManager.options.thirdPersonView == 2;
        		float f2 = dog.height + 0.42F - (flag ? 0.25F : 0.0F) - (dog.isPlayerSleeping() ? 0.5F : 0);
        
        		ObjectLibClient.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), label, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag, 0.01F);
        		ObjectLibClient.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), dog.getDisplayName().getFormattedText(), (float)x, (float)y + f2 - 0.12F, (float)z, 0, f, f1, flag1, flag, 0.026F);
        		
        		if(d0 <= (double)(5 * 5)) {
    	    		if(this.renderManager.renderViewEntity.isSneaking()) {
    	    			String ownerName = "A Wild Dog";
    	    			if(dog.getOwner() != null)
    	    				ownerName = dog.getOwner().getDisplayName().getUnformattedText();
    	    			else if(dog.getOwnerId() != null)
    	          		   	ownerName = dog.getOwnerId().toString();
    	    			else
    	    				ownerName = ObjectLib.BRIDGE.translateToLocal("entity.doggytalents:dog.lost.name");
    	    			
    	    			ObjectLibClient.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), ownerName, (float)x, (float)y + f2 - 0.34F, (float)z, 0, f, f1, flag1, flag, 0.01F);
    	    		}
        		}
        	}
        }
    }
    
    @Override
    protected void preRenderCallback(EntityDog entitylivingbaseIn, float partialTickTime) {
    	EntityDog dog = (EntityDog)entitylivingbaseIn;
    	float size = dog.getDogSize() * 0.3F + 0.1F;
    	GlStateManager.scale(size, size, size);
    	this.shadowSize = size*0.5F;
	}
}