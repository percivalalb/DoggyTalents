package doggytalents.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import doggytalents.client.DogTextureManager;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.layer.BoneLayer;
import doggytalents.client.entity.render.layer.DogAccessoryLayer;
import doggytalents.client.entity.render.layer.DogTalentLayer;
import doggytalents.common.config.ConfigValues;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.Optional;

public class DogRenderer extends MobRenderer<DogEntity, DogModel<DogEntity>> {

    public DogRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DogModel<>(), 0.5F);
        this.addLayer(new DogTalentLayer(this));
        this.addLayer(new DogAccessoryLayer(this));
        this.addLayer(new BoneLayer(this));
    }

    @Override
    protected float getBob(DogEntity livingBase, float partialTicks) {
        return livingBase.getTailRotation();
    }

    @Override
    public void render(DogEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entityIn.isDogWet()) {
            float f = entityIn.getShadingWhileWet(partialTicks);
            this.model.setColor(f, f, f);
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        if (this.shouldShowName(entityIn)) {

            double d0 = this.entityRenderDispatcher.distanceToSqr(entityIn);
            if (d0 <= 64 * 64) {
                String tip = entityIn.getMode().getTip();
                String label = String.format(ConfigValues.DOG_GENDER ? "%s(%d)%s" : "%s(%d)",
                        new TranslationTextComponent(tip).getString(),
                        MathHelper.ceil(entityIn.getDogHunger()),
                        new TranslationTextComponent(entityIn.getGender().getUnlocalisedTip()).getString());

                RenderUtil.renderLabelWithScale(entityIn, this, label, matrixStackIn, bufferIn, packedLightIn, 0.01F, 0.12F);

                if (d0 <= 5 * 5 && this.entityRenderDispatcher.camera.getEntity().isShiftKeyDown()) {
                    RenderUtil.renderLabelWithScale(entityIn, this, entityIn.getOwnersName().orElseGet(() -> this.getNameUnknown(entityIn)), matrixStackIn, bufferIn, packedLightIn, 0.01F, -0.25F);
                }
            }
        }


        if (entityIn.isDogWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }
    }


    private ITextComponent getNameUnknown(DogEntity dogIn) {
        return new TranslationTextComponent(dogIn.getOwnerUUID() != null ? "entity.doggytalents.dog.unknown_owner" : "entity.doggytalents.dog.untamed");
    }

    @Override
    public ResourceLocation getTextureLocation(DogEntity dogIn) {
        return DogTextureManager.INSTANCE.getTexture(dogIn);
    }

    @Override
    protected void scale(DogEntity dogIn, MatrixStack matrixStackIn, float partialTickTime) {
        float size = dogIn.getDogSize() * 0.3F + 0.1F;
        matrixStackIn.scale(size, size, size);
        this.shadowRadius = size * 0.5F;
    }
}
