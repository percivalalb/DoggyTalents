package doggytalents.client.renderer.entity.layer;

import java.util.function.Predicate;

import com.google.common.base.Predicates;

import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerModel implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelBase model;
    private final ResourceLocation resource;
    private final Predicate<EntityDog> condition;
    
    public LayerModel(RenderDog dogRendererIn, ModelBase modelIn, ResourceLocation resourceIn, Predicate<EntityDog> condition) {
        this.dogRenderer = dogRendererIn;
        this.model = modelIn;
        this.resource = resourceIn;
        this.condition = condition;
    }
    
    public LayerModel(RenderDog dogRendererIn, ModelBase modelIn, ResourceLocation resourceIn) {
        this(dogRendererIn, modelIn, resourceIn, Predicates.<EntityDog>alwaysTrue());
    }
    
    @Override
    public void render(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(this.condition.test(dog) && !dog.isInvisible()) {
            this.dogRenderer.bindTexture(this.resource);
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            
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