package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.model.entity.ModelWings;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerWings implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelWings wingsModel = new ModelWings();

    public LayerWings(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(Constants.DOGGY_WINGS && dog.talents.getLevel("pillowpaw") == 5) {
        	this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_WINGS);
        	GlStateManager.color(1.0F, 1.0F, 1.0F);

        	this.wingsModel.setModelAttributes(this.dogRenderer.getMainModel());
            this.wingsModel.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.wingsModel.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}