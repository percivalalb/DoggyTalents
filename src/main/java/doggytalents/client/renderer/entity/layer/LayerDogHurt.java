package doggytalents.client.renderer.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ConfigValues;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerDogHurt extends LayerRenderer<EntityDog, ModelDog> {

    public LayerDogHurt(RenderDog dogRendererIn) {
        super(dogRendererIn);
    }

    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.isTamed() && !dog.isInvisible() && (dog.getHealth() == 1 && dog.isImmortal() && ConfigValues.RENDER_BLOOD)) {
            this.bindTexture(ResourceLib.MOB_LAYER_DOG_HURT);
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            this.getEntityModel().render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}