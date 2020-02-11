package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerDogCollar extends LayerRenderer<EntityDog, ModelDog> {

    public LayerDogCollar(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.isTamed() && !dog.isInvisible() && dog.hasCollar()) {
            if(dog.hasFancyCollar()) {
                 renderCutoutModel(this.getEntityModel(), ResourceLib.getFancyCollar(dog.getFancyCollarIndex()), matrixStackIn, bufferIn, packedLightIn, dog, 1.0f, 1.0f, 1.0f);
            }
            else if(dog.hasCollarColoured()) {
                if(dog.isCollarColoured()) {
                    float[] afloat = dog.getCollar();
                    renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_DOG_COLLAR, matrixStackIn, bufferIn, packedLightIn, dog, afloat[0], afloat[1], afloat[2]);
                } else {
                    renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_DOG_COLLAR, matrixStackIn, bufferIn, packedLightIn, dog, 1.0f, 1.0f, 1.0f);
                }
            }
        }
    }
}