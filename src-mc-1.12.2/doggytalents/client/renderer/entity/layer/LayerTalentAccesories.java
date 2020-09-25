package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelTalentAccesories;
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
public class LayerTalentAccesories implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelTalentAccesories saddleModel = new ModelTalentAccesories();

    public LayerTalentAccesories(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.isTamed() && !dog.isInvisible()) {            
        	this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_ACCESORIES);
        	GlStateManager.color(1.0F, 1.0F, 1.0F);

        	this.saddleModel.setModelAttributes(this.dogRenderer.getMainModel());
            this.saddleModel.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.saddleModel.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
	}

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}