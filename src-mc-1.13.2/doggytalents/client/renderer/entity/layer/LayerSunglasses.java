package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerSunglasses implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelDog sunglassesModel;
    
    public LayerSunglasses(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
        this.sunglassesModel = new ModelDog(0.4F);
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.hasSunglasses()) {
        	if(dog.world.getWorldInfo().getDayTime() < 12000)
        		this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_SUNGLASSES);
        	else
        		this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_SUNGLASSES_NIGHT);
        	GlStateManager.color3f(1.0F, 1.0F, 1.0F);
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