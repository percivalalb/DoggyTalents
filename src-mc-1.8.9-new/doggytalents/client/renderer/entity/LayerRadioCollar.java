package doggytalents.client.renderer.entity;

import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceReference;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerRadioCollar implements LayerRenderer {

    private final RenderDog renderDog;

    public LayerRadioCollar(RenderDog renderDog) {
        this.renderDog = renderDog;
    }

    public void func_177145_a(EntityDog dog, float p_177145_2_, float p_177145_3_, float p_177145_4_, float p_177145_5_, float p_177145_6_, float p_177145_7_, float p_177145_8_) {
        if(dog.isTamed() && !dog.isInvisible() && dog.hasRadarCollar()) {
            this.renderDog.bindTexture(ResourceReference.doggyRadioCollar);
            this.renderDog.getMainModel().render(dog, p_177145_2_, p_177145_3_, p_177145_5_, p_177145_6_, p_177145_7_, p_177145_8_);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    @Override
    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.func_177145_a((EntityDog)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}