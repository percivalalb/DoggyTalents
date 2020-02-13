package doggytalents.client.renderer.entity.layer;

import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.client.model.entity.ModelDog;
import doggytalents.client.renderer.entity.RenderDog;
import doggytalents.entity.EntityDog;
import net.minecraft.client.renderer.IRenderTypeBuffer;
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
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityDog dog, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(this.condition.test(dog) && !dog.isInvisible()) {
            this.getEntityModel().copyModelAttributesTo(this.model);
            this.model.setLivingAnimations(dog, limbSwing, limbSwingAmount, partialTicks);
            renderCutoutModel(this.model, this.resource, matrixStackIn, bufferIn, packedLightIn, dog, 1.0f, 1.0f, 1.0f);
        }
    }
}