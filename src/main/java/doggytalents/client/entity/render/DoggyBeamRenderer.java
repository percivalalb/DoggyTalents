package doggytalents.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class DoggyBeamRenderer<T extends Entity> extends EntityRenderer<T> {

    private final net.minecraft.client.renderer.ItemRenderer itemRenderer;
    private final float scale;
    private final boolean field_229126_f_;

    public DoggyBeamRenderer(EntityRendererManager rendererManager, net.minecraft.client.renderer.ItemRenderer p_i226035_2_, float p_i226035_3_, boolean p_i226035_4_) {
        super(rendererManager);
        this.itemRenderer = p_i226035_2_;
        this.scale = p_i226035_3_;
        this.field_229126_f_ = p_i226035_4_;
    }

    public DoggyBeamRenderer(EntityRendererManager renderManagerIn, net.minecraft.client.renderer.ItemRenderer itemRendererIn) {
        this(renderManagerIn, itemRendererIn, 1.0F, false);
    }

    @Override
    protected int getBlockLight(T entityIn, BlockPos posIn) {
        return this.field_229126_f_ ? 15 : super.getBlockLight(entityIn, posIn);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.scale(this.scale, this.scale, this.scale);
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        this.itemRenderer.renderItem(new ItemStack(Items.SNOWBALL), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }
}