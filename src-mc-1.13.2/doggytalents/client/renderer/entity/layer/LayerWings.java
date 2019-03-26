package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelWings;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.configuration.ConfigHandler;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerWings implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelWings wingsModel = new ModelWings();

    public LayerWings(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(ConfigHandler.CONFIG.doggyWings() && dog.TALENTS.getLevel("pillowpaw") == 5) {
        	this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_WINGS);
        	GlStateManager.color3f(1.0F, 1.0F, 1.0F);

        	this.wingsModel.setModelAttributes(this.dogRenderer.getMainModel());
            this.wingsModel.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.wingsModel.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}