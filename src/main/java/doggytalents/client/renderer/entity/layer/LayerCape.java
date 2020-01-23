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
public class LayerCape extends LayerRenderer<EntityDog, ModelDog> {

    public LayerCape(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(MatrixStack p_225628_1_, IRenderTypeBuffer p_225628_2_, int p_225628_3_, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(dog.hasCape() && !dog.isInvisible()) {
            if(dog.hasCapeColoured()) {
                if(dog.isCapeColoured()) {
                    float[] afloat = dog.getCapeColour();
                    renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_CAPE_COLOURED, p_225628_1_, p_225628_2_, p_225628_3_, dog, afloat[0], afloat[1], afloat[2]);
                } else {
                    renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_CAPE_COLOURED, p_225628_1_, p_225628_2_, p_225628_3_, dog, 1.0f, 1.0f, 1.0f);
                }
            }
            else if(dog.hasFancyCape()) {
                renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_CAPE, p_225628_1_, p_225628_2_, p_225628_3_, dog, 1.0f, 1.0f, 1.0f);
            }
            else if(dog.hasLeatherJacket()) {
                renderCutoutModel(this.getEntityModel(), ResourceLib.MOB_LAYER_LEATHER_JACKET, p_225628_1_, p_225628_2_, p_225628_3_, dog, 1.0f, 1.0f, 1.0f);
            }
        }
    }
}