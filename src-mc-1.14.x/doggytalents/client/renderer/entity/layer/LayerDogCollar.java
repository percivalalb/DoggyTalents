package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerDogCollar extends LayerRenderer<EntityDog, ModelDog> {

    public LayerDogCollar(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.isTamed() && !dog.isInvisible() && dog.hasCollar()) {
            if(dog.hasFancyCollar()) {
                 this.bindTexture(ResourceLib.getFancyCollar(dog.getFancyCollarIndex()));
                 GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            }
            else if(dog.hasCollarColoured()) {
                this.bindTexture(ResourceLib.MOB_LAYER_DOG_COLLAR);
                if(dog.isCollarColoured()) {
                    float[] afloat = dog.getCollar();
                    GlStateManager.color3f(afloat[0], afloat[1], afloat[2]);
                }
            }
            this.getEntityModel().render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}