package doggytalents.client.renderer.entity;

import doggytalents.base.ObjectLib;
import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.layer.LayerBone;
import doggytalents.client.renderer.entity.layer.LayerDogCollar;
import doggytalents.client.renderer.entity.layer.LayerDogHurt;
import doggytalents.client.renderer.entity.layer.LayerRadioCollar;
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
        super(renderManagerIn, new ModelDog(), 0.5F);
        this.addLayer(new LayerRadioCollar(this));
        this.addLayer(new LayerDogCollar(this));
        this.addLayer(new LayerDogHurt(this));
        this.addLayer(new LayerBone(this));
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
			return ResourceLib.MOB_DOG_TAME;
    	
        return ResourceLib.MOB_DOG_WILD;
    }
    
    @Override
    protected void renderEntityName(EntityDog dog, double x, double y, double z, String name, double distanceFromPlayer) {

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
    
    		ObjectLib.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), label, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag, 0.01F);
    	
    		if (distanceFromPlayer <= (double)(5 * 5)) {
	    		if(this.renderManager.renderViewEntity.isSneaking()) {
	    			String ownerName = "A Wild Dog";
	    			if(dog.getOwner() != null)
	    				ownerName = dog.getOwner().getDisplayName().getUnformattedText();
	    			else if(dog.getOwnerId() != null)
	          		   	ownerName = dog.getOwnerId().toString();
	    			
	    			ObjectLib.METHODS.renderLabelWithScale(this.getFontRendererFromRenderManager(), ownerName, (float)x, (float)y + f2 - 0.34F, (float)z, 0, f, f1, flag1, flag, 0.01F);
	    		}
    		}
    	}
    	
        super.renderEntityName(dog, x, y - 0.2, z, name, distanceFromPlayer);
    }
}