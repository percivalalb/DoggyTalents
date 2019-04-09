package doggytalents.client.renderer.entity.layer;

import java.util.function.Predicate;

import doggytalents.client.model.entity.ModelSaddle;
import doggytalents.client.model.entity.ModelWings;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import doggytalents.lib.ResourceLib;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ProPercivalalb
 */
@SideOnly(Side.CLIENT)
public class LayerModel implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelBase model;
    private final ResourceLocation resource;
    private final Predicate<EntityDog> condition;
    
    public LayerModel(RenderDog dogRendererIn, ModelBase model, ResourceLocation resource, Predicate<EntityDog> condition) {
        this.dogRenderer = dogRendererIn;
        this.model = model;
        this.resource = resource;
        this.condition = condition;
    }

    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(this.condition.test(dog)) {
            this.dogRenderer.bindTexture(this.resource);
            GlStateManager.color(1.0F, 1.0F, 1.0F);
            
        	this.model.setModelAttributes(this.dogRenderer.getMainModel());
            this.model.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}