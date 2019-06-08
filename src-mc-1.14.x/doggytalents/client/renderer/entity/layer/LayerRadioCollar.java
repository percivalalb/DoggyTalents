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
public class LayerRadioCollar extends LayerRenderer<EntityDog, ModelDog> {

    public LayerRadioCollar(RenderDog dogRendererIn) {
     	super(dogRendererIn);
    }

    @Override
    public void func_212842_a_(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    	if(dog.isTamed() && !dog.isInvisible() && dog.hasRadarCollar()) {
        	this.func_215333_a(ResourceLib.MOB_LAYER_RADIO_COLLAR);
        	GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        	this.func_215332_c().render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}