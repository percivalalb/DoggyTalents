package doggytalents.client.renderer.entity.layer;

import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerDogHurt implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;

    public LayerDogHurt(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.isTamed() && !dog.isInvisible() && (dog.getHealth() == 1 && dog.isImmortal() && Constants.RENDER_BLOOD)) {
            this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_DOG_HURT);
        	this.dogRenderer.getMainModel().render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}