package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerCape extends LayerRenderer<EntityDog, ModelDog> {

    public LayerCape(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.hasCape() && !dog.isInvisible()) {
            if(dog.hasCapeColoured()) {
                this.bindTexture(ResourceLib.MOB_LAYER_CAPE_COLOURED);
                if(dog.isCapeColoured()) {
                    float[] afloat = dog.getCapeColour();
                    GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);
                }
            }
            else if(dog.hasFancyCape()) {
                this.bindTexture(ResourceLib.MOB_LAYER_CAPE);
                GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            }
            else if(dog.hasLeatherJacket()) {
                this.bindTexture(ResourceLib.MOB_LAYER_LEATHER_JACKET);
                GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            }

            this.getEntityModel().render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}