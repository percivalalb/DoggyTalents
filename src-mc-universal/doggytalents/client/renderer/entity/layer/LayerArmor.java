package doggytalents.client.renderer.entity.layer;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.Constants;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerArmor implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    public final ModelDog armorModel;

    public LayerArmor(RenderDog dogRendererIn) {
        this.dogRenderer = dogRendererIn;
        this.armorModel = new ModelDog(0.1F);
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(dog.talents.getLevel("guarddog") > 0) {
        	this.dogRenderer.bindTexture(ResourceLib.MOB_LAYER_ARMOR);
        	GlStateManager.color(1.0F, 1.0F, 1.0F);

        	this.armorModel.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}