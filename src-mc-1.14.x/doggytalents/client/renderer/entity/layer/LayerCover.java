package doggytalents.client.renderer.entity.layer;

import java.util.function.Function;
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
public class LayerCover extends LayerRenderer<EntityDog, ModelDog> {

    private final EntityModel<EntityDog> model;
    private final Function<EntityDog, ResourceLocation> resource;
    private final Predicate<EntityDog> condition;
    
    public LayerCover(RenderDog dogRendererIn, EntityModel<EntityDog> modelIn, Function<EntityDog, ResourceLocation> resourceIn, Predicate<EntityDog> conditionIn) {
    	super(dogRendererIn);
        this.model = modelIn;
        this.resource = resourceIn;
        this.condition = conditionIn;
    }
    
    public LayerCover(RenderDog dogRendererIn, EntityModel<EntityDog> modelIn, ResourceLocation resourceIn, Predicate<EntityDog> conditionIn) {
        this(dogRendererIn, modelIn, dog -> resourceIn, conditionIn);
    }
    
    public LayerCover(RenderDog dogRendererIn, EntityModel<EntityDog> modelIn, ResourceLocation resourceIn) {
        this(dogRendererIn, modelIn, resourceIn, Predicates.<EntityDog>alwaysTrue());
    }
    
    @Override
    public void func_212842_a_(EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(this.condition.test(dog) && !dog.isInvisible()) {
            this.func_215333_a(this.resource.apply(dog));
            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
            
        	this.model.func_217111_a(this.func_215332_c());
            this.model.func_212843_a_(dog, limbSwing, limbSwingAmount, partialTicks);
            this.model.render(dog, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}