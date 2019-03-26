package doggytalents.client.renderer.entity.layer;

import doggytalents.client.renderer.entity.RenderDog;
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
public class LayerRadioCollar implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;

    public LayerRadioCollar(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.isTamed() && !dog.isInvisible() && dog.hasRadarCollar()) {
        	this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_RADIO_COLLAR);
        	GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        	this.dogRenderer.getMainModel().render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}