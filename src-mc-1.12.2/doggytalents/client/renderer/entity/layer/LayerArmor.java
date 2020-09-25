package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerArmor implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    public final ModelDog armorModel;

    public LayerArmor(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
        this.armorModel = new ModelDog(0.4F);
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    	if(dog.hasArmor()) {
    		if(dog.hasArmorColoured()) {
    			if(dog.getArmorData() == dog.BONE_ARMOR) {
    				this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_BONE_ARMOR);
    			}
    			else if(dog.getArmorData() == dog.WOODEN_ARMOR) {
    				this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_WOODEN_ARMOR);
    			} 
    			else {
    				this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_ARMOR);

    				float[] afloat = dog.getArmorColour();
    				GlStateManager.color(afloat[0], afloat[1], afloat[2]);
    			}
    		} 
        	this.armorModel.setModelAttributes(this.dogRenderer.getMainModel());
        	this.armorModel.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
        	this.armorModel.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        } 
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}