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

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerRadioCollar extends LayerRenderer<EntityDog, ModelDog> {

    public LayerRadioCollar(RenderDog dogRendererIn) {
         super(dogRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.isTamed() && !dog.isInvisible() && dog.hasRadarCollar()) {
            renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_RADIO_COLLAR, matrixStackIn, bufferIn, packedLightIn, dog, 1.0f, 1.0f, 1.0f);
        }
    }
}