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
public class LayerSunglasses implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelDog sunglassesModel;
    
    public LayerSunglasses(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
        this.sunglassesModel = new ModelDog(0.4F);
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.hasSunglasses()) {
        	if(dog.world.getWorldTime() >= 12000)
        		this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_SUNGLASSES_NIGHT);
        	else
        		this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_SUNGLASSES);
        	GlStateManager.color(1.0F, 1.0F, 1.0F);
        	this.sunglassesModel.setModelAttributes(this.dogRenderer.getMainModel());
        	this.sunglassesModel.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
        	this.sunglassesModel.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}