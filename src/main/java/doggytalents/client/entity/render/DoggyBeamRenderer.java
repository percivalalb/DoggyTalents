package doggytalents.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DoggyBeamRenderer<T extends Entity> extends EntityRenderer<T> {

    private final net.minecraft.client.renderer.entity.ItemRenderer itemRenderer;
    private final float scale;
    private final boolean fullBright;

    public DoggyBeamRenderer(EntityRendererProvider.Context ctx, float p_i226035_3_, boolean p_i226035_4_) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
        this.scale = p_i226035_3_;
        this.fullBright = p_i226035_4_;
    }

    public DoggyBeamRenderer(EntityRendererProvider.Context ctx) {
        this(ctx, 1.0F, false);
    }

    @Override
    protected int getBlockLightLevel(T entityIn, BlockPos posIn) {
        return this.fullBright ? 15 : super.getBlockLightLevel(entityIn, posIn);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.scale(this.scale, this.scale, this.scale);
        matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        this.itemRenderer.renderStatic(new ItemStack(Items.SNOWBALL), ItemTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entityIn.getId());
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
