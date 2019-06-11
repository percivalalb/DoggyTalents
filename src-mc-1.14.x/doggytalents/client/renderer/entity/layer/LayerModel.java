package doggytalents.client.renderer.entity.layer;

import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.platform.GlStateManager;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author ProPercivalalb
 */
@OnlyIn(Dist.CLIENT)
public class LayerModel extends LayerRenderer<EntityDog, ModelDog> {

    private final EntityModel<EntityDog> model;
    private final ResourceLocation resource;
    private final Predicate<EntityDog> condition;
    
    public LayerModel(RenderDog dogRendererIn, EntityModel<EntityDog> modelIn, ResourceLocation resourceIn, Predicate<EntityDog> condition) {
     	super(dogRendererIn);
        this.model = modelIn;
        this.resource = resourceIn;
        this.condition = condition;
    }
    
    public LayerModel(RenderDog dogRendererIn, EntityModel<EntityDog> modelIn, ResourceLocation resourceIn) {
        this(dogRendererIn, modelIn, resourceIn, Predicates.<EntityDog>alwaysTrue());
    }
    
    @Override
    public void func_212842_a_(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(this.condition.test(dog) && !dog.isInvisible()) {
            this.func_215333_a(this.resource);
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            
            this.func_215332_c().func_217111_a(this.model);
            this.model.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}