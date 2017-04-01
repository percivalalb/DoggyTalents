package doggytalents.client.renderer.entity;

import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceReference;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerDogHurt implements LayerRenderer {

    private final RenderDog renderDog;

    public LayerDogHurt(RenderDog renderDog) {
        this.renderDog = renderDog;
    }

    public void func_177145_a(EntityDog dog, float p_177145_2_, float p_177145_3_, float p_177145_4_, float p_177145_5_, float p_177145_6_, float p_177145_7_, float p_177145_8_) {
        if(dog.isTamed() && !dog.isInvisible() && (dog.getHealth() == 1 && dog.isImmortal() && Constants.RENDER_BLOOD)) {
            this.renderDog.bindTexture(ResourceReference.doggyHurt);
            this.renderDog.getMainModel().render(dog, p_177145_2_, p_177145_3_, p_177145_5_, p_177145_6_, p_177145_7_, p_177145_8_);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.func_177145_a((EntityDog)entity, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}