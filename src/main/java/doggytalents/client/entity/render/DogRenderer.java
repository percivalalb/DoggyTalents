package doggytalents.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import doggytalents.client.ClientSetup;
import doggytalents.client.DogTextureManager;
import doggytalents.client.entity.model.DogModel;
import doggytalents.client.entity.render.layer.BoneLayer;
import doggytalents.client.entity.render.layer.LayerFactory;
import doggytalents.common.config.ConfigHandler;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DogRenderer extends MobRenderer<DogEntity, DogModel<DogEntity>> {

    public DogRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new DogModel(ctx.bakeLayer(ClientSetup.DOG)), 0.5F);
//        this.addLayer(new DogTalentLayer(this, ctx));
//        this.addLayer(new DogAccessoryLayer(this, ctx));
        this.addLayer(new BoneLayer(this, ctx.getItemInHandRenderer()));
        for (LayerFactory<DogEntity, DogModel<DogEntity>> layer : CollarRenderManager.getLayers()) {
            this.addLayer(layer.createLayer(this, ctx));
        }
    }

    @Override
    protected float getBob(DogEntity livingBase, float partialTicks) {
        return livingBase.getTailRotation();
    }

    @Override
    public void render(DogEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.isDogWet()) {
            float f = entityIn.getShadingWhileWet(partialTicks);
            this.model.setColor(f, f, f);
        }

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        if (this.shouldShowName(entityIn)) {

            double d0 = this.entityRenderDispatcher.distanceToSqr(entityIn);
            if (d0 <= 64 * 64) {
                String tip = entityIn.getMode().getTip();
                String label = String.format(ConfigHandler.SERVER.DOG_GENDER.get() ? "%s(%d)%s" : "%s(%d)",
                        Component.translatable(tip).getString(),
                        Mth.ceil(entityIn.getDogHunger()),
                        Component.translatable(entityIn.getGender().getUnlocalisedTip()).getString());

                RenderUtil.renderLabelWithScale(entityIn, this, this.entityRenderDispatcher, label, matrixStackIn, bufferIn, packedLightIn, 0.01F, 0.12F);

                if (d0 <= 5 * 5 && this.entityRenderDispatcher.camera.getEntity().isShiftKeyDown()) {
                    RenderUtil.renderLabelWithScale(entityIn, this, this.entityRenderDispatcher, entityIn.getOwnersName().orElseGet(() -> this.getNameUnknown(entityIn)), matrixStackIn, bufferIn, packedLightIn, 0.01F, -0.25F);
                }
            }
        }


        if (entityIn.isDogWet()) {
            this.model.setColor(1.0F, 1.0F, 1.0F);
        }
    }


    private Component getNameUnknown(DogEntity dogIn) {
        return Component.translatable(dogIn.getOwnerUUID() != null ? "entity.doggytalents.dog.unknown_owner" : "entity.doggytalents.dog.untamed");
    }

    @Override
    public ResourceLocation getTextureLocation(DogEntity dogIn) {
        return DogTextureManager.INSTANCE.getTexture(dogIn);
    }

    @Override
    protected void scale(DogEntity dogIn, PoseStack matrixStackIn, float partialTickTime) {
        float size = dogIn.getDogSize() * 0.3F + 0.1F;
        matrixStackIn.scale(size, size, size);
        this.shadowRadius = size * 0.5F;
    }
}
