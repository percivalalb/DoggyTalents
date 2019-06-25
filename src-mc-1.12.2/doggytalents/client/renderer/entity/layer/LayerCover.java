package doggytalents.client.renderer.entity.layer;

import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.base.Predicates;

import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
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
public class LayerCover implements LayerRenderer<EntityDog> {

    private final RenderDog dogRenderer;
    private final ModelBase model;
    private final Function<EntityDog, ResourceLocation> resource;
    private final Predicate<EntityDog> condition;
    
    public LayerCover(RenderDog dogRendererIn, ModelBase modelIn, Function<EntityDog, ResourceLocation> resourceIn, Predicate<EntityDog> conditionIn) {
        this.dogRenderer = dogRendererIn;
        this.model = modelIn;
        this.resource = resourceIn;
        this.condition = conditionIn;
    }
    
    public LayerCover(RenderDog dogRendererIn, ModelBase modelIn, ResourceLocation resourceIn, Predicate<EntityDog> conditionIn) {
        this(dogRendererIn, modelIn, dog -> resourceIn, conditionIn);
    }
    
    public LayerCover(RenderDog dogRendererIn, ModelBase modelIn, ResourceLocation resourceIn) {
        this(dogRendererIn, modelIn, resourceIn, Predicates.<EntityDog>alwaysTrue());
    }
    
    @Override
    public void doRenderLayer(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(this.condition.test(dog) && !dog.isInvisible()) {
            this.dogRenderer.bindTexture(this.resource.apply(dog));
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